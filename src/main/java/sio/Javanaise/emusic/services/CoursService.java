package sio.Javanaise.emusic.services;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sio.Javanaise.emusic.models.Cour;
import sio.Javanaise.emusic.models.Eleve;
import sio.Javanaise.emusic.models.Planning;
import sio.Javanaise.emusic.repositories.ICoursRepository;
import sio.Javanaise.emusic.repositories.IPlanningRepository;

@Service
public class CoursService {

	private LocalDate date = LocalDate.now();

	@Autowired
	private ICoursRepository courRepository;

	@Autowired
	private IPlanningRepository planningRepository;



	public boolean valideAge(Eleve eleve, int id) {

		int age = date.getYear() - eleve.getDate_naiss().getYear();

		Optional<Planning> opt = planningRepository.findById(id);
		if (opt.isPresent()) {
			Cour cour = opt.get().getCour();
			if (cour.getAgeMin() <= age && age <= cour.getAgeMAx()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

}
