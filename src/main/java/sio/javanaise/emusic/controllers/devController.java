package sio.javanaise.emusic.controllers;

import java.sql.Time;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.javafaker.Faker;

import sio.javanaise.emusic.enumeration.TypeCourEnum;
import sio.javanaise.emusic.models.Cour;
import sio.javanaise.emusic.models.Eleve;
import sio.javanaise.emusic.models.Facture;
import sio.javanaise.emusic.models.Inscription;
import sio.javanaise.emusic.models.Instrument;
import sio.javanaise.emusic.models.Paiement;
import sio.javanaise.emusic.models.Planning;
import sio.javanaise.emusic.models.Prof;
import sio.javanaise.emusic.models.Responsable;
import sio.javanaise.emusic.models.TypeCour;
import sio.javanaise.emusic.models.User;
import sio.javanaise.emusic.repositories.ICoursRepository;
import sio.javanaise.emusic.repositories.IEleveRepository;
import sio.javanaise.emusic.repositories.IFactureRepository;
import sio.javanaise.emusic.repositories.IInscriptionRepository;
import sio.javanaise.emusic.repositories.IInstrumentRepository;
import sio.javanaise.emusic.repositories.IPaiementRepository;
import sio.javanaise.emusic.repositories.IPlanningRepository;
import sio.javanaise.emusic.repositories.IProfRepository;
import sio.javanaise.emusic.repositories.IResponsableRepository;
import sio.javanaise.emusic.repositories.ITypeCoursRepository;
import sio.javanaise.emusic.repositories.IUserDAO;
import sio.javanaise.emusic.services.TokenGenerator;
import sio.javanaise.emusic.services.UserService;

@Controller
@RequestMapping("/data")
public class devController {

	private LocalDate dateDebut;

	private Time heureDebut;

	private Time duree;
	@Autowired
	Environment environment;
	@DateTimeFormat(pattern = "yyy-MM-dd")
	private LocalDate date_naissance;

	@Autowired
	private IUserDAO userrepo;

	@Autowired
	private IProfRepository profRepo;

	@Autowired
	private IEleveRepository eleveRepo;

	@Autowired
	private IInstrumentRepository instruRepo;

	@Autowired
	private ITypeCoursRepository typeCourRepo;

	@Autowired
	private ICoursRepository courRepo;

	@Autowired
	private IPlanningRepository planningRepo;

	@Autowired
	private IResponsableRepository responRepo;

	@Autowired
	private IInscriptionRepository inscritRepo;

	@Autowired
	private IPaiementRepository payeRepo;

	@Autowired
	IFactureRepository factRepo;

	@Autowired
	private TokenGenerator token;


	@Autowired
	private UserDetailsService uService;

	@GetMapping("/{nb}")
	private @ResponseBody String ajoutData(@PathVariable int nb) {

		// type de Cour

		TypeCour typeCour1 = new TypeCour(TypeCourEnum.Collectif);
		typeCour1.setId(1);
		typeCourRepo.save(typeCour1);
		TypeCour typeCour2 = new TypeCour(TypeCourEnum.Individuel);
		typeCour2.setId(2);
		typeCourRepo.save(typeCour2);

		
		//admin

		User userAdmin = ((UserService) uService).createUser("Admin", "Admin");
		userAdmin.setAuthorities("ADMIN");
		userAdmin.setToken(token.generateToken());
		userrepo.save(userAdmin);


		for (int o = 0; o < nb; o++) {

			// prof
			Prof prof = new Prof();
			Faker fake = new Faker();
			prof.setEmail(fake.internet().emailAddress());
			prof.setNom(fake.name().lastName());
			prof.setPrenom(fake.leagueOfLegends().champion());
			prof.setToken(token.generateToken());
			profRepo.save(prof);
			User user = ((UserService) uService).createUser(prof.getPrenom() + "-" + prof.getNom(), "Azerty1234");
			user.setAuthorities("PROF");
			user.setToken(prof.getToken());
			userrepo.save(user);

			// responsable
			Responsable respon = new Responsable();
			fake = new Faker();

			respon.setAdresse(fake.address().streetName());
			respon.setCode_postal("14000");
			respon.setEmail(fake.internet().emailAddress());
			respon.setNom(fake.pokemon().name());
			respon.setPrenom(fake.ancient().primordial());
			respon.setQuotient_familial(10);
			respon.setTel1("0123456789");
			respon.setToken(token.generateToken());
			respon.setVille(fake.address().cityName());
			responRepo.save(respon);
			user = ((UserService) uService).createUser(respon.getPrenom() + "-" + respon.getNom(), "Azerty1234");
			user.setAuthorities("PARENT");
			user.setToken(respon.getToken());
			userrepo.save(user);

			// eleve
			fake = new Faker();
			Eleve eleve = new Eleve();

			eleve.setNom(fake.lordOfTheRings().character());
			eleve.setPrenom(fake.witcher().character());
			eleve.setDateNaiss(date_naissance.parse("2007-12-03"));
			eleve.setToken(token.generateToken());
			eleve.setResponsable(respon);
			eleveRepo.save(eleve);
			user = ((UserService) uService).createUser(eleve.getPrenom() + "-" + eleve.getNom(), "Azerty1234");
			user.setAuthorities("ELEVE");
			user.setToken(eleve.getToken());
			userrepo.save(user);

			// instru
			fake = new Faker();
			Instrument instru = new Instrument();
			instru.setIntitule(fake.music().instrument());
			instruRepo.save(instru);

			// cour
			fake = new Faker();
			Cour cour = new Cour();
			cour.setAgeMAx(15);
			cour.setAgeMin(9);
			cour.setLibelle(fake.music().genre());
			cour.setNbPlace(5);
			cour.setProf(prof);
			cour.setTypeCour(typeCour1);

			courRepo.save(cour);

			// planning
			Planning planning = new Planning();
			planning.setCour(cour);
			planning.setDateDebut(dateDebut.parse("2022-12-01"));
			planning.setHeureDebut(heureDebut.valueOf("10:30:00"));
			planning.setHeureFin(heureDebut.valueOf("12:00:00"));
			planning.setStatus("ok");
			planning.setJourSemaine("Lun");
			planning.setDateFin(dateDebut.parse("2025-11-01"));

			planningRepo.save(planning);

			// inscrit
			Inscription inscrit = new Inscription();
			inscrit.setEleve(eleve);
			inscrit.setPlanning(planning);
			inscritRepo.save(inscrit);

			// facture
			Facture fact = new Facture();
			fake = new Faker();
			fact.setDateFacture(dateDebut.parse("2022-11-01"));
			fact.setInscription(inscrit);
			fact.setPrix(fake.number().numberBetween(10, 500));
			factRepo.save(fact);

			// paiemment
			Paiement paye = new Paiement();
			paye.setDateTransmission(dateDebut.parse("2022-11-11"));
			paye.setFacture(fact);
			paye.setPrix(5);
			payeRepo.save(paye);

		}
		return "init ok";
	}
}