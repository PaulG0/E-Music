package sio.javanaise.emusic.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import io.github.jeemv.springboot.vuejs.VueJS;
import sio.javanaise.emusic.models.Cour;
import sio.javanaise.emusic.models.Prof;
import sio.javanaise.emusic.models.TypeCour;
import sio.javanaise.emusic.models.User;
import sio.javanaise.emusic.repositories.ICoursRepository;
import sio.javanaise.emusic.repositories.IPlanningRepository;
import sio.javanaise.emusic.repositories.IProfRepository;
import sio.javanaise.emusic.repositories.ITypeCoursRepository;
import sio.javanaise.emusic.services.CoursService;

@Controller
@RequestMapping({ "/cours", "/cours/" })
public class CoursController {

	@Autowired

	private ICoursRepository courRepo;

	@Autowired
	private IPlanningRepository planningRepository;


	private ICoursRepository courRepository;



	@Autowired
	private ITypeCoursRepository typeCoursRepository;

	@Autowired
	private IProfRepository profRepository;

	@Autowired
	private CoursService courService;

	@Autowired(required = true)
	private VueJS vue;

	@ModelAttribute("vue")
	public VueJS getVue() {
		return this.vue;
	}


//List Cours
	@GetMapping("")
	public String indexCoursCAction(@AuthenticationPrincipal User authUser, ModelMap model) {

		Iterable<Cour> cours = courRepository.findAll();
		model.put("cours", cours);
		Iterable<TypeCour> typeCours = typeCoursRepository.findAll();
		Iterable<Prof> profs = profRepository.findAll();
		model.put("cour", new Cour());
		model.put("typeCours", typeCours);
		model.put("profs", profs);

		vue.addData("cours", courRepository.findAll());
		vue.addData("coursEdit");
		vue.addData("idProf");
		vue.addData("idTypeCours");

		vue.addMethod("popupEditCours", "this.coursEdit=coursEdit; this.idProf=idProf; this.idTypeCours=idTypeCours",
				"coursEdit, idProf, idTypeCours");

		model.put("authUser", authUser);
		vue.addData("authUser", authUser);
		return "/cours/indexCours";

	}

// detail Cours
	@GetMapping("/{id}")
	public String detailCoursCAction(@AuthenticationPrincipal User authUser, @PathVariable int id, ModelMap model,
			ModelMap model2) {
		courRepository.findById(id).ifPresent(cour -> model.put("cour", cour));
		model2.put("authUser", authUser);
		vue.addData("authUser", authUser);
		return "/cours/detail";

	}

//add / modify Cours

	@PostMapping("/new")
	public RedirectView newCoursAction(@AuthenticationPrincipal User authUser, ModelMap model,
			@ModelAttribute Cour cour) {

		Optional<Cour> opt = courRepository.findById(cour.getId());
		model.put("authUser", authUser);
		vue.addData("authUser", authUser);
		if (opt.isPresent()) {

			if (cour.getProf() == null) {
				cour.setProf(opt.get().getProf());
			}

			if (cour.getTypeCour() == null) {
				cour.setTypeCour(opt.get().getTypeCour());
			}

		}

		courRepository.save(cour);
		return new RedirectView("../cours");

	}

//edit Cours
	@GetMapping("/edit/{id}")
	public String editCoursAction(@AuthenticationPrincipal User authUser, ModelMap model, ModelMap model2,
			ModelMap model3, @PathVariable int id) {

		Iterable<TypeCour> typeCours = typeCoursRepository.findAll();
		Iterable<Prof> profs = profRepository.findAll();
		courRepository.findById(id).ifPresent(cour -> model.put("cour", cour));
		model2.put("typeCours", typeCours);
		model3.put("profs", profs);
		model.put("authUser", authUser);
		vue.addData("authUser", authUser);
		return "/cours/formCours";

	}

}
