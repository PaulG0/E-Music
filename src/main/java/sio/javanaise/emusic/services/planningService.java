package sio.javanaise.emusic.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sio.javanaise.emusic.models.Cour;
import sio.javanaise.emusic.models.Planning;
import sio.javanaise.emusic.models.Prof;
import sio.javanaise.emusic.repositories.ICoursRepository;
import sio.javanaise.emusic.repositories.IPlanningRepository;
import sio.javanaise.emusic.repositories.IProfRepository;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class planningService {

	@Autowired
	private IProfRepository profRepo;

	@Autowired
	private ICoursRepository courRepo;

	@Autowired
	private IPlanningRepository planningRepo;

	@Autowired
	private FormatService formatService;


	private LocalDate date;



	public Iterable<Cour> courProf(int id) {
		Prof prof = new Prof();
		Iterable<Cour> cour;
		Optional<Prof> opt = profRepo.findById(id);
		if (opt.isPresent()) {
			prof = opt.get();
		}
		cour = courRepo.findAllByProf(prof);
		return cour;
	}

	public List<Planning> planningProf(int id) {
		Iterable<Planning> plannings = planningRepo.findAllByOrderByDateDebut();
		Iterable<Cour> cours = courProf(id);

		List<Planning> planningProf = new ArrayList<>();
		for (Planning planning : plannings) {
			for (Cour cour : cours) {
				if (planning.getCour().equals(cour)) {
					planningProf.add(planning);
				}
			}
		}
		return planningProf;
	}

	public ArrayList<Planning> planningPeriodeProf(int idProf, LocalDate date) {
		List<Planning> plannings = planningProf(idProf);
		ArrayList<Planning> newPlanning = new ArrayList<>() {
		};


		for (Planning planning : plannings) {
			System.out.println(planning.getId());
			if (planning.getDateDebut().isBefore(date) && planning.getDateFin().isAfter(date)) {
				System.out.println(planning.getId()+" : ok");
				newPlanning.add(planning);
			}

		}
		return newPlanning;
	}


	public ArrayList<Planning> planningJourSemaine(int idProf , LocalDate date){
		ArrayList<Planning> newPlanning = new ArrayList<>();
		ArrayList<Planning> plannings = planningPeriodeProf(idProf, date);
		String Jour = formatService.formatJourSemaine(date);

		for(Planning pl : plannings){
			if (pl.getJourSemaine().equals(Jour)) {
				newPlanning.add(pl);
			}
		}
		return newPlanning;
	}

	public ArrayList<Planning> planingfinal(int idProf, LocalDate date){
		ArrayList<Planning> plannings = planningJourSemaine(idProf, date);
	    ArrayList<Planning> newPlanning = planningJourSemaine(idProf, date);
		for(Planning pl : plannings){
			if (!pl.getStatus().equals("ok")){

				int status = Integer.parseInt(pl.getStatus());

				Optional< Planning> opt = planningRepo.findById(status);
				if (opt.isPresent()) {
					Planning rmPlanning = opt.get();
					newPlanning.remove(rmPlanning);
				}

			}


		}
		return newPlanning;
	}



}
