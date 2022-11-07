package sio.Javanaise.emusic.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import io.github.jeemv.springboot.vuejs.VueJS;
import sio.Javanaise.emusic.models.Cour;
import sio.Javanaise.emusic.models.Planning;
import sio.Javanaise.emusic.repositories.ICoursRepository;
import sio.Javanaise.emusic.repositories.IPlanningRepository;
import sio.Javanaise.emusic.ui.UILink;
import sio.Javanaise.emusic.ui.UIMessage;

@Controller
@RequestMapping({ "/cours", "/cours/" })
public class CoursController {

	@Autowired
	private ICoursRepository courRepo;
	
	@Autowired
	private IPlanningRepository planningRepository;
	
	
	@Autowired(required = true)
	private VueJS vue;
	
	@ModelAttribute("vue")
	public VueJS getVue() {
		return this.vue;
	}
	
//liste
	@RequestMapping("")
	public String indexCoursAction(ModelMap model) {
		Iterable<Planning> plannings = planningRepository.findAll();
		model.put("plannings", plannings);
		return "/cours/index";
	}
//detail
	@GetMapping("/{id}")
	public String detailCoursAction(@PathVariable int id, ModelMap model) {
		planningRepository.findById(id).ifPresent(planning -> model.put("planning", planning));
		return "/cours/detail";
	}
	//delete
		@GetMapping("/delete/{id}")
		public RedirectView DeleteCourAction(@PathVariable int id, RedirectAttributes attrs) {

			Optional<Planning> opt = planningRepository.findById(id);
			if (opt.isPresent()) {
				attrs.addFlashAttribute("msg",
						UIMessage.error("Suppression", "Voulez vous supprimer " + opt.get() + " ?")
								.addLinks(new UILink("oui", "/cours/delete/force/" + id), new UILink("non", "")));
			}

			return new RedirectView("/cours");
		}

		@GetMapping("delete/force/{id}")
		public RedirectView deleteAction(@PathVariable int id, ModelMap model) {
			planningRepository.deleteById(id);
			return new RedirectView("/cours");
		}
}
