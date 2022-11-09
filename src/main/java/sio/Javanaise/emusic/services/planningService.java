package sio.Javanaise.emusic.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sio.Javanaise.emusic.models.Cour;
import sio.Javanaise.emusic.models.Planning;
import sio.Javanaise.emusic.models.Prof;
import sio.Javanaise.emusic.repositories.ICoursRepository;
import sio.Javanaise.emusic.repositories.IPlanningRepository;
import sio.Javanaise.emusic.repositories.IProfRepository;

@Service
public class planningService {

	@Autowired
	private IProfRepository profRepo;

	@Autowired
	private ICoursRepository courRepo;

	@Autowired
	private IPlanningRepository planningRepo;

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

	public Iterable<Planning> planningProf(int id) {
		Iterable<Planning> planning;
		Optional<Prof> opt = profRepo.findById(id);
		Prof prof = new Prof();
		if (opt.isPresent()) {
			prof = opt.get();
		}
		planning = planningRepo.findallByProf(prof);
		return planning;

	}
}
