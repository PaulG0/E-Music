package sio.javanaise.emusic.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import io.github.jeemv.springboot.vuejs.VueJS;
import sio.javanaise.emusic.models.Planning;
import sio.javanaise.emusic.repositories.ICoursRepository;
import sio.javanaise.emusic.repositories.IPlanningRepository;
import sio.javanaise.emusic.repositories.IProfRepository;
import sio.javanaise.emusic.services.planningService;

@Controller
@RequestMapping({ "/planning", "/planning/" })
public class PlanningController {

	@Autowired
	private IProfRepository profRepository;

	@Autowired
	private IPlanningRepository planningRepository;

	@Autowired
	private ICoursRepository coursRepository;

	@Autowired
	private planningService planService;

	@Autowired(required = true)
	private VueJS vue;

	@ModelAttribute("vue")
	public VueJS getVue() {
		return this.vue;
	}

//affichage du planning prof
	@GetMapping("/prof/{id}")
	private String planingProfAction(@PathVariable int id, ModelMap model) {
		vue.addData("vodk", 0);
		vue.addMethod("message", "this.vodk=1");

		ArrayList<Planning> planning = planService.planningProf(id);
		model.put("planning", planning);
		return "/planning/prof";
	}
	
	@PostMapping("/prof/{id}")
	private String planingProfAction(@PathVariable int id, ModelMap model, @RequestParam String date) {
		
		ArrayList<Planning> planning = planService.planningProf(id);
		model.put("planning", planning);
		return "/planning/prof";
		
	}

}
