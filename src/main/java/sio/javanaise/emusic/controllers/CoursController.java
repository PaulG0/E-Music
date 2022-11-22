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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import io.github.jeemv.springboot.vuejs.VueJS;
import sio.javanaise.emusic.models.Cour;
import sio.javanaise.emusic.models.Eleve;
import sio.javanaise.emusic.models.Inscription;
import sio.javanaise.emusic.models.Planning;
import sio.javanaise.emusic.models.Prof;
import sio.javanaise.emusic.models.TypeCour;
import sio.javanaise.emusic.models.User;
import sio.javanaise.emusic.repositories.ICoursRepository;
import sio.javanaise.emusic.repositories.IInscriptionRepository;
import sio.javanaise.emusic.repositories.IPlanningRepository;
import sio.javanaise.emusic.repositories.IProfRepository;
import sio.javanaise.emusic.repositories.ITypeCoursRepository;
import sio.javanaise.emusic.services.CoursService;
import sio.javanaise.emusic.ui.UILink;
import sio.javanaise.emusic.ui.UIMessage;

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
	public String indexCoursAction(@AuthenticationPrincipal User authUser, ModelMap model) {
		Iterable<Planning> plannings = planningRepository.findAll();
		model.put("plannings", plannings);
		model.put("authUser", authUser);
		vue.addData("authUser", authUser);
		return "/cours/index";
	}

//detail
	@GetMapping("/{id}")
	public String detailCoursAction(@AuthenticationPrincipal User authUser, @PathVariable int id, ModelMap model) {

		planningRepository.findById(id).ifPresent(planning -> model.put("planning", planning));
		Iterable<Eleve> eleve = courService.listeleve(id);
		model.put("eleve", eleve);
		model.put("authUser", authUser);
		vue.addData("authUser", authUser);
		return "/cours/detail";
	}

//delete Cours
	@GetMapping("/delete/{id}")
	public RedirectView DeleteCourAction(@AuthenticationPrincipal User authUser, ModelMap model, @PathVariable int id,
			RedirectAttributes attrs) {

		Optional<Planning> opt = planningRepository.findById(id);
		if (opt.isPresent()) {
			attrs.addFlashAttribute("msg", UIMessage.error("Suppression", "Voulez vous supprimer " + opt.get() + " ?")
					.addLinks(new UILink("oui", "/cours/delete/force/" + id), new UILink("non", "")));
		}
		model.put("authUser", authUser);
		vue.addData("authUser", authUser);
		return new RedirectView("/cours");
	}

	@GetMapping("delete/force/{id}")
	public RedirectView deleteAction(@AuthenticationPrincipal User authUser, @PathVariable int id, ModelMap model) {
		planningRepository.deleteById(id);
		model.put("authUser", authUser);
		vue.addData("authUser", authUser);
		return new RedirectView("/cours");
	}

//List Cours
	@GetMapping("/cours")
	public String indexCoursCAction(@AuthenticationPrincipal User authUser, ModelMap model) {

		Iterable<Cour> cours = courRepo.findAll();
		model.put("cours", cours);
		model.put("authUser", authUser);
		vue.addData("authUser", authUser);
		return "/cours/indexCours";

	}

//new Cours
	@GetMapping("/cours/new")
	public String newCoursAction(@AuthenticationPrincipal User authUser, ModelMap model, ModelMap model2,
			ModelMap model3) {

		Iterable<TypeCour> typeCours = typeCoursRepository.findAll();
		Iterable<Prof> profs = profRepository.findAll();
		model.put("cour", new Cour());
		model2.put("typeCours", typeCours);
		model3.put("profs", profs);
		model.put("authUser", authUser);
		vue.addData("authUser", authUser);
		return "/cours/formCours";

	}

//add / modify Cours
	@PostMapping("/cours/new")
	public RedirectView newCoursAction(@AuthenticationPrincipal User authUser, ModelMap model,
			@ModelAttribute Cour cour) {
		model.put("authUser", authUser);
		vue.addData("authUser", authUser);
		courRepo.save(cour);
		return new RedirectView("/cours");

	}

//edit Cours
	@GetMapping("/cours/edit/{id}")
	public String editCoursAction(@AuthenticationPrincipal User authUser, ModelMap model, ModelMap model2,
			ModelMap model3, @PathVariable int id) {

		Iterable<TypeCour> typeCours = typeCoursRepository.findAll();
		Iterable<Prof> profs = profRepository.findAll();
		courRepo.findById(id).ifPresent(cour -> model.put("cour", cour));
		model2.put("typeCours", typeCours);
		model3.put("profs", profs);
		model.put("authUser", authUser);
		vue.addData("authUser", authUser);
		return "/cours/formCours";

	}

//Delete inscrit

	@GetMapping("delete/inscrit/{id}/{idCour}")
	public RedirectView DeleteInscritAction(@AuthenticationPrincipal User authUser, @PathVariable int id,
			ModelMap model, @PathVariable int idCour, RedirectAttributes attrs) {
		Optional<Inscription> opt = inscriptionRepository.findById(id);
		if (opt.isPresent()) {
			Eleve eleve = opt.get().getEleve();
			attrs.addFlashAttribute("inscrit", UIMessage
					.error("Suppression", "Voulez vous supprimer " + eleve.getPrenom() + " " + eleve.getNom() + " ?")
					.addLinks(new UILink("oui", "delete/inscrit/force/" + id + "/" + idCour), new UILink("non", "")));
		}
		model.put("authUser", authUser);
		vue.addData("authUser", authUser);

		return new RedirectView("/cours/" + idCour);
	}

	@GetMapping("delete/inscrit/force/{id}/{idCour}")
	public RedirectView deleteInscritAction(@AuthenticationPrincipal User authUser, @PathVariable int id,
			@PathVariable int idCour, ModelMap model) {
		inscriptionRepository.deleteById(id);
		model.put("authUser", authUser);
		vue.addData("authUser", authUser);
		return new RedirectView("/cours/" + idCour);
	}
}
