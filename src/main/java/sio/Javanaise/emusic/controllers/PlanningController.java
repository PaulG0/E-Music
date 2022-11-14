package sio.Javanaise.emusic.controllers;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.jeemv.springboot.vuejs.VueJS;
import sio.Javanaise.emusic.models.Planning;
import sio.Javanaise.emusic.repositories.ICoursRepository;
import sio.Javanaise.emusic.repositories.IPlanningRepository;
import sio.Javanaise.emusic.repositories.IProfRepository;
import sio.Javanaise.emusic.services.planningService;

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
		vue.addData("vodk");
		vue.addData("date", new Date());
		vue.addMethod("message",
				"this.vodk=1,this.date=new Date(today.getFullYear(), today.getMonth(), today.getDate())");

		ArrayList<Planning> planning = planService.planningProf(id);
		model.put("planning", planning);
		return "/planning/prof";
	}

}
