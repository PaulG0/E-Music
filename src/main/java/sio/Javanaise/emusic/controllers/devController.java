package sio.Javanaise.emusic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.javafaker.Faker;

import sio.Javanaise.emusic.models.Eleve;
import sio.Javanaise.emusic.models.Instrument;
import sio.Javanaise.emusic.models.Prof;
import sio.Javanaise.emusic.repositories.IEleveRepository;
import sio.Javanaise.emusic.repositories.IInstrumentRepository;
import sio.Javanaise.emusic.repositories.IProfRepository;

@Controller
@RequestMapping("/data")
public class devController {

	@Autowired
	private IProfRepository profRepo;

	@Autowired
	private IEleveRepository eleveRepo;

	@Autowired
	private IInstrumentRepository instruRepo;

	@GetMapping("/{nb}")
	private @ResponseBody String ajoutData(@PathVariable int nb) {

		for (int o = 0; o < nb; o++) {

			Prof prof = new Prof();
			Faker fake = new Faker();
			prof.setEmail(fake.internet().emailAddress());
			prof.setNom(fake.name().lastName());
			prof.setPrenom(fake.leagueOfLegends().champion());
			profRepo.save(prof);

			// eleve
			fake = new Faker();
			Eleve eleve = new Eleve();

			eleve.setNom(fake.lordOfTheRings().character());
			eleve.setPrenom(fake.witcher().character());
			eleveRepo.save(eleve);
			// instru

			fake = new Faker();
			Instrument instru = new Instrument();
			instru.setIntitule(fake.music().instrument());
			instruRepo.save(instru);

		}
		return "init ok";
	}
}
