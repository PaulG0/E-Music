package sio.Javanaise.emusic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.jeemv.springboot.vuejs.VueJS;
import sio.Javanaise.emusic.models.Cour;
import sio.Javanaise.emusic.repositories.ICoursRepository;

@Controller
@RequestMapping({ "/cours", "/cours/" })
public class CoursController {

	@Autowired
	private ICoursRepository courRepo;

	@Autowired(required = true)
	private VueJS vue;

	@ModelAttribute("vue")
	public VueJS getVue() {
		return this.vue;
	}

	@RequestMapping("")
	public String indexCoursAction(ModelMap model) {
		Iterable<Cour> cour = courRepo.findAll();
		model.put("cours", cour);
		return "/cours/index";
	}

	@GetMapping("/{id}")
	public String detailCoursAction(@PathVariable int id, ModelMap model) {
		courRepo.findById(id).ifPresent(cour -> model.put("cour", cour));
		return "/cours/detail";
	}

}
