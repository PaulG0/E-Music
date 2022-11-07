package sio.Javanaise.emusic.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sio.Javanaise.emusic.models.Cour;
import sio.Javanaise.emusic.models.Eleve;
import sio.Javanaise.emusic.models.Planning;
import sio.Javanaise.emusic.repositories.ICoursRepository;
import sio.Javanaise.emusic.repositories.IEleveRepository;
import sio.Javanaise.emusic.repositories.IPlanningRepository;

@Service
public class CoursService {

	private LocalDate date = LocalDate.now();

	@Autowired
	private ICoursRepository courRepository;

	@Autowired
	private IPlanningRepository planningRepository;

	@Autowired
	private IEleveRepository eleveRepository;

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

	public List<Eleve> listeleve(int id) {

		List<Eleve> eleves = new ArrayList<>();
		Iterable<Eleve> eleve = eleveRepository.findAll();

		for (Eleve verifeleve : eleve) {
			if (valideAge(verifeleve, id)) {
				eleves.add(verifeleve);
			}
		}

		return eleves;
	}

	public String getFormValidation() {
		return "$('.ui.form').form({on: 'blur', 'inline': true, fields:{ name:"
				+ " ['empty','maxLength[20]'], aliases: 'empty', domain: 'empty'}});";
	}

	public String ifFormIsValid(String code) {
		return "if($('.ui.form').form('validate form')){" + code + "}";
	}

	public String getURL(String url, String idStr) {
		return "'" + url + "'+" + idStr;
	}

	public String toast(String type, String message) {
		return "$.toast({ class: '" + type + "', message: `" + message + "`});";
	}
}
