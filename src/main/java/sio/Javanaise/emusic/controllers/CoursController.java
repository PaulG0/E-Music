package sio.Javanaise.emusic.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import io.github.jeemv.springboot.vuejs.VueJS;
import sio.Javanaise.emusic.models.Cour;
import sio.Javanaise.emusic.models.Eleve;
import sio.Javanaise.emusic.models.Inscription;
import sio.Javanaise.emusic.models.Planning;
import sio.Javanaise.emusic.models.Prof;
import sio.Javanaise.emusic.models.TypeCour;
import sio.Javanaise.emusic.repositories.ICoursRepository;
import sio.Javanaise.emusic.repositories.IEleveRepository;
import sio.Javanaise.emusic.repositories.IInscriptionRepository;
import sio.Javanaise.emusic.repositories.IPlanningRepository;
import sio.Javanaise.emusic.repositories.IProfRepository;
import sio.Javanaise.emusic.repositories.ITypeCoursRepository;
import sio.Javanaise.emusic.services.CoursService;
import sio.Javanaise.emusic.ui.UILink;
import sio.Javanaise.emusic.ui.UIMessage;

@Controller
@RequestMapping({ "/cours", "/cours/" })
public class CoursController {

	@Autowired
	private ICoursRepository courRepo;

	@Autowired
	private IPlanningRepository planningRepository;

	@Autowired
	private IInscriptionRepository inscriptionRepository;

	@Autowired
	private IEleveRepository eleveRepository;
	
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
		Iterable<Eleve> eleve = courService.listeleve(id);
		model.put("eleve", eleve);
		return "/cours/detail";
	}
	
//delete Cours
	@GetMapping("/delete/{id}")
	public RedirectView DeleteCourAction(@PathVariable int id, RedirectAttributes attrs) {

		Optional<Planning> opt = planningRepository.findById(id);
		if (opt.isPresent()) {
			attrs.addFlashAttribute("msg", UIMessage.error("Suppression", "Voulez vous supprimer " + opt.get() + " ?")
					.addLinks(new UILink("oui", "/cours/delete/force/" + id), new UILink("non", "")));
		}

		return new RedirectView("/cours");
	}

	@GetMapping("delete/force/{id}")
	public RedirectView deleteAction(@PathVariable int id, ModelMap model) {
		planningRepository.deleteById(id);
		return new RedirectView("/cours");
	}
	
//List Cours
	@GetMapping("/cours")
	public String indexCoursCAction(ModelMap model) {
		
		Iterable<Cour> cours = courRepo.findAll();
		model.put("cours", cours);
		return "/cours/indexCours";
		
	}
	
//new Cours
	@GetMapping("/cours/new")
	public String newCoursAction(ModelMap model, ModelMap model2, ModelMap model3) {
		
		Iterable<TypeCour> typeCours = typeCoursRepository.findAll();
		Iterable<Prof> profs = profRepository.findAll();
		model.put("cour", new Cour());
		model2.put("typeCours", typeCours);
		model3.put("profs", profs);
		return "/cours/formCours";
		
	}
	
//add / modify Cours
	@PostMapping("/cours/new")
	public RedirectView newCoursAction(@ModelAttribute Cour cour) {
		
		courRepo.save(cour);
		return new RedirectView("/cours");
		
	}
	
//edit Cours
	@GetMapping("/cours/edit/{id}")
	public String editCoursAction(ModelMap model, ModelMap model2, ModelMap model3, @PathVariable int id) {
		
		Iterable<TypeCour> typeCours = typeCoursRepository.findAll();
		Iterable<Prof> profs = profRepository.findAll();
		courRepo.findById(id).ifPresent(cour -> model.put("cour", cour));
		model2.put("typeCours", typeCours);
		model3.put("profs", profs);
		return "/cours/formCours";
		
	}

//Delete inscrit

	@GetMapping("delete/inscrit/{id}/{idCour}")
	public RedirectView DeleteInscritAction(@PathVariable int id, @PathVariable int idCour, RedirectAttributes attrs) {
		Optional<Inscription> opt = inscriptionRepository.findById(id);
		if (opt.isPresent()) {
			Eleve eleve = opt.get().getEleve();
			attrs.addFlashAttribute("inscrit", UIMessage
					.error("Suppression", "Voulez vous supprimer " + eleve.getPrenom() + " " + eleve.getNom() + " ?")
					.addLinks(new UILink("oui", "delete/inscrit/force/" + id + "/" + idCour), new UILink("non", "")));
		}

		return new RedirectView("/cours/" + idCour);
	}

	@GetMapping("delete/inscrit/force/{id}/{idCour}")
	public RedirectView deleteInscritAction(@PathVariable int id, @PathVariable int idCour, ModelMap model) {
		inscriptionRepository.deleteById(id);
		return new RedirectView("/cours/" + idCour);
	}

}
