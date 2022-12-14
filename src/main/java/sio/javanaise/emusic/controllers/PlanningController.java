package sio.javanaise.emusic.controllers;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import sio.javanaise.emusic.models.Eleve;
import sio.javanaise.emusic.models.Inscription;
import sio.javanaise.emusic.models.Planning;
import sio.javanaise.emusic.models.User;
import sio.javanaise.emusic.repositories.ICoursRepository;
import sio.javanaise.emusic.repositories.IInscriptionRepository;
import sio.javanaise.emusic.repositories.IPlanningRepository;
import sio.javanaise.emusic.repositories.IProfRepository;
import sio.javanaise.emusic.services.CoursService;
import sio.javanaise.emusic.services.FormatService;
import sio.javanaise.emusic.services.planningService;
import sio.javanaise.emusic.ui.UILink;
import sio.javanaise.emusic.ui.UIMessage;

@Controller
@RequestMapping({ "/planning", "/planning/" })
public class PlanningController {


	@Autowired
	Environment environment;

	@Autowired
	private IProfRepository profRepository;

	@Autowired
	private IPlanningRepository planningRepository;

	@Autowired
	private IInscriptionRepository inscriptionRepository;

	@Autowired
	private ICoursRepository coursRepository;

	@Autowired
	private planningService planService;

	@Autowired
	private FormatService formatService;

	@Autowired
	private CoursService courService;

	@Autowired(required = true)
	private VueJS vue;

	@ModelAttribute("vue")
	public VueJS getVue() {
		return this.vue;
	}

	// liste
	@RequestMapping("")
	public String indexCoursAction(@AuthenticationPrincipal User authUser, ModelMap model) {
		Iterable<Planning> plannings = planningRepository.findAll();
		model.put("plannings", plannings);
		model.put("base", environment.getProperty("app.base"));
		model.put("authUser", authUser);
		vue.addData("authUser", authUser);
		return "/planning/index";
	}

	// detail
	@GetMapping("/{id}")
	public String detailCoursAction(@AuthenticationPrincipal User authUser, @PathVariable int id, ModelMap model) {

		planningRepository.findById(id).ifPresent(planning -> model.put("planning", planning));
		Iterable<Eleve> eleve = courService.listeleve(id);
		model.put("eleve", eleve);
		model.put("base", environment.getProperty("app.base"));
		model.put("authUser", authUser);
		vue.addData("authUser", authUser);
		return "/planning/detail";
	}

	// delete Cours
	@GetMapping("/delete/{id}")
	public RedirectView DeleteCourAction(@AuthenticationPrincipal User authUser, ModelMap model, @PathVariable int id,
			RedirectAttributes attrs) {

		Optional<Planning> opt = planningRepository.findById(id);
		if (opt.isPresent()) {
			attrs.addFlashAttribute("msg", UIMessage.error("Suppression", "Voulez vous supprimer " + opt.get() + " ?")
					.addLinks(new UILink("oui", "planning/delete/force/" + id), new UILink("non", "planning/")));
		}
		model.put("authUser", authUser);
		vue.addData("authUser", authUser);
		return new RedirectView("../../planning");
	}

	@GetMapping("delete/force/{id}")
	public RedirectView deleteAction(@PathVariable int id, ModelMap model) {

		planningRepository.deleteById(id);
		return new RedirectView("../../../planning");
	}

//add inscrit

	@PostMapping("inscrit/add/{id}")
	public RedirectView AddInscritAction(@PathVariable int id, @ModelAttribute("ajoutEleve") List<Eleve> eleves,
			ModelMap model) {

		Optional<Planning> opt = planningRepository.findById(id);
		Planning planning = new Planning();
		if (opt.isPresent()) {
			planning = opt.get();
		}


		Inscription inscrit = new Inscription();

		for (Eleve eleve : eleves) {
			inscrit.setEleve(eleve);
			inscrit.setPlanning(planning);
			inscriptionRepository.save(inscrit);

		}

		return new RedirectView("../../../planning/" + id);
	}
	// Delete inscrit

	@GetMapping("delete/inscrit/{id}/{idCour}")
	public RedirectView DeleteInscritAction(@PathVariable int id, @PathVariable int idCour, RedirectAttributes attrs) {
		Optional<Inscription> opt = inscriptionRepository.findById(id);
		if (opt.isPresent()) {
			Eleve eleve = opt.get().getEleve();
			attrs.addFlashAttribute("inscrit",
					UIMessage
							.error("Suppression",
									"Voulez vous supprimer " + eleve.getPrenom() + " " + eleve.getNom() + " ?")
							.addLinks(new UILink("oui", "planning/delete/inscrit/force/" + id + "/" + idCour),
									new UILink("non", "")));
		}

		return new RedirectView("../../../../planning/" + idCour);
	}



	@GetMapping("delete/inscrit/force/{id}/{idCour}")
	public RedirectView deleteInscritAction(@PathVariable int id, @PathVariable int idCour) {
		inscriptionRepository.deleteById(id);

		return new RedirectView("../../../../../planning/" + idCour);
	}



	// =======================================================
	// =======================================================

	// =======================================================
	// =======================================================

	// =======================================================
	// =======================================================

	// affichage du planning prof
	@GetMapping("/prof/{id}")

	private String planingProfActionGet(@AuthenticationPrincipal User authUser, @PathVariable int id, ModelMap model) {

		List<Planning> planning = planService.planningProf(id);


		model.put("planning", planning);
		model.put("idProf", id);
		model.put("base", environment.getProperty("app.base"));
		model.put("authUser", authUser);
		vue.addData("authUser", authUser);

		return "/planning/prof";
	}

	@PostMapping("/prof/{id}")
	private String planingProfActionPost(@AuthenticationPrincipal User authUser, @PathVariable int id, ModelMap model,
			@ModelAttribute("datePlanning") String datePlanning) {

		LocalDate date = formatService.formatdate(datePlanning);

		List<Planning> planning = planService.planningJour(id, date);

		vue.addData("plannings", planning);

		model.put("plannings", planning);
		model.put("idProf", id);
		model.put("datePlanning", date);
		model.put("base", environment.getProperty("app.base"));
		model.put("authUser", authUser);
		vue.addData("authUser", authUser);


		return "/planning/prof";

	}

}
