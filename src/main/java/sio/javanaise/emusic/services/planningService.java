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

@Service
public class planningService {

	@Autowired
	private IProfRepository profRepo;

	@Autowired
	private ICoursRepository courRepo;

	@Autowired
	private IPlanningRepository planningRepo;



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

	public List<Planning> planningJour(int idProf, LocalDate date) {
		List<Planning> plannings = planningProf(idProf);
		List<Planning> newPlanning = new ArrayList<>();


		for (Planning planning : plannings) {
			if (planning.getDateDebut().equals(date)) {
				newPlanning.add(planning);
			}
		}

		return newPlanning;
	}


}
