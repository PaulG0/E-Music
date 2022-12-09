package sio.javanaise.emusic.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
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
import sio.javanaise.emusic.models.Prof;
import sio.javanaise.emusic.models.Responsable;
import sio.javanaise.emusic.models.User;
import sio.javanaise.emusic.repositories.IEleveRepository;
import sio.javanaise.emusic.repositories.IProfRepository;
import sio.javanaise.emusic.repositories.IResponsableDAO;
import sio.javanaise.emusic.repositories.IUserDAO;
import sio.javanaise.emusic.services.ResponsableService;
import sio.javanaise.emusic.services.TokenGenerator;
import sio.javanaise.emusic.services.UIResponsableService;
import sio.javanaise.emusic.services.UserService;

@Controller
@RequestMapping("/responsables")
public class ResponsableController {

	@Autowired(required = true)
	private VueJS vue;

	@Autowired
	Environment environment;

	@ModelAttribute("vue")
	public VueJS getVue() {
		return this.vue;
	}

	@Autowired
	private IUserDAO userrepo;

	@Autowired
	private IResponsableDAO responsablerepo;

	@Autowired
	private IEleveRepository eleverepo;

	@Autowired
	private IProfRepository profRepository;

	@Autowired
	private UIResponsableService responsableService;

	@Autowired
	private ResponsableService rService;

	@Autowired
	private UserDetailsService uService;

	@Autowired
	private TokenGenerator tokgen;

	@GetMapping("")
	public String indexAction(@AuthenticationPrincipal User authUser, ModelMap model) {

		vue.addData("messageOption");
		vue.addData("membre");
		vue.addData("user");
		vue.addData("toDelete");
		vue.addData("unRole");
		vue.addData("villeAction");
		vue.addData("idResponsable");
		vue.addData("dateNaissEleve");
		vue.addData("users", userrepo.findAll());
		vue.addData("responsables", responsablerepo.findAll());
		vue.addData("eleves", eleverepo.findAll());
		vue.addData("profs", profRepository.findAll());
		Iterable<Responsable> responsables = responsablerepo.findAll();
		Iterable<Eleve> eleves = eleverepo.findAll();
		Iterable<Prof> profs = profRepository.findAll();

		vue.addMethod("confMessageOption", "this.messageOption=membre; this.unRole=unRole;", "membre, unRole");
		vue.addMethod("popupDelete", "this.toDelete=membre;" + responsableService.modalDelete() + ";", "membre");
		vue.addMethod("popupSuspendre",
				"this.membre=membre; this.user=user;" + responsableService.modalSuspendre() + ";", "membre, user");

		model.put("responsables", responsables);
		model.put("responsable", new Responsable());
		model.put("eleves", eleves);
		model.put("eleve", new Eleve());
		model.put("profs", profs);
		model.put("prof", new Prof());

		model.put("base", environment.getProperty("app.base"));
		model.put("authUser", authUser);
		vue.addData("authUser", authUser);

		return "/responsables/index";

	}

	@PostMapping("/new")
	public RedirectView newAction(@ModelAttribute Responsable responsable, @ModelAttribute("password") String password,
			@ModelAttribute("login") String login, RedirectAttributes attrs) {

		vue.addData("affichage", false);
		Optional<User> opt2 = userrepo.findByLogin(login);
		if (opt2.isPresent()) {
			attrs.addFlashAttribute("erreurLogin", "login déjà utilisé");
			return new RedirectView("../responsables");
		}
		if (login.length() < 5 || login.length() > 20) {
			attrs.addFlashAttribute("erreurLogin", "Votre login doit être compris entre 5 et 20 caractères");
		}
		if (!rService.NomEstValide(responsable.getNom())) {
			attrs.addFlashAttribute("erreurNom",
					"Nom invalide, veuillez n'utiliser que des lettres latines, mettez une majuscule au début. Les noms composés doivent etre séparés par des -");
			return new RedirectView("../responsables");
		}
		if (!rService.NomEstValide(responsable.getPrenom())) {
			attrs.addFlashAttribute("erreurPrenom",
					"Prénom invalide, veuillez n'utiliser que des lettres latines, mettez une majuscule au début. Les noms composés doivent être séparés par des -");
			return new RedirectView("../responsables");
		}
		Optional<Responsable> opt = responsablerepo.findByEmail(responsable.getEmail());
		if (opt.isPresent()) {
			attrs.addFlashAttribute("erreurEmail", "Adresse email déjà utilisé");
			return new RedirectView("../responsables");
		}
		if (!rService.EmailEstValide(responsable.getEmail())) {
			attrs.addFlashAttribute("erreurEmail", "Adresse email invalide");
			return new RedirectView("../responsables");
		}
		if (password.length() < 8) {
			attrs.addFlashAttribute("erreurPassword", "Votre mot de passe doit contenir au moins 8 caractères");
			return new RedirectView("../responsables");
		}
		if (!rService.CodePostalEstValide(responsable.getCode_postal())) {
			attrs.addFlashAttribute("erreurCode", "Votre code postal doit contenir 5 chiffres");
			return new RedirectView("../responsables");
		}
		if (responsable.getTel1().equals("") || responsable.getTel1() == null) {
			attrs.addFlashAttribute("erreurTel", "Vous devez renseigner un numéro de téléphone");
			return new RedirectView("../responsables");
		}
		if (!rService.NuméroEstValide(responsable.getTel1())) {
			attrs.addFlashAttribute("erreurTel", "Numéro invalide");
			return new RedirectView("../responsables");
		}
		if (!responsable.getVille().equals("ifs") && !responsable.getVille().equals("Ifs")
				&& !responsable.getVille().equals("IFS")) {
			responsable.setQuotient_familial(null);
		}
		if (responsable.getToken() == null) {
			String token = tokgen.generateToken();
			User us = ((UserService) uService).createUser(login, password);
			us.setAuthorities("PARENT");
			us.setToken(token);
			userrepo.save(us);
			responsable.setToken(us.getToken());
		}
		responsablerepo.save(responsable);
		return new RedirectView("../responsables");

	}

	@PostMapping("/edit")
	public RedirectView editAction(@ModelAttribute Responsable responsable, RedirectAttributes attrs) {

		vue.addData("affichage", false);
		if (!rService.NomEstValide(responsable.getNom())) {
			attrs.addFlashAttribute("erreurNom",
					"Nom invalide, veuillez n'utiliser que des lettres latines, mettez une majuscule au début. Les noms composés doivent etre séparés par des -");
			return new RedirectView("../responsables");
		}
		if (!rService.NomEstValide(responsable.getPrenom())) {
			attrs.addFlashAttribute("erreurPrenom",
					"Prénom invalide, veuillez n'utiliser que des lettres latines, mettez une majuscule au début. Les noms composés doivent être séparés par des -");
			return new RedirectView("../responsables");
		}
		Optional<Responsable> opt = responsablerepo.findByEmail(responsable.getEmail());
		Optional<Responsable> opt2 = responsablerepo.findById(responsable.getId());
		if (opt.isPresent() && opt2.get().getId() != opt.get().getId()) {
			attrs.addFlashAttribute("erreurEmail", "Adresse email déjà utilisé");
			return new RedirectView("../responsables");
		}
		if (!rService.EmailEstValide(responsable.getEmail())) {
			attrs.addFlashAttribute("erreurEmail", "Adresse email invalide");
			return new RedirectView("../responsables");
		}
		if (!rService.CodePostalEstValide(responsable.getCode_postal())) {
			attrs.addFlashAttribute("erreurCode", "Votre code postal doit contenir 5 chiffres");
			return new RedirectView("../responsables");
		}
		if (responsable.getTel1().equals("") || responsable.getTel1() == null) {
			attrs.addFlashAttribute("erreurTel", "Vous devez renseigner un numéro de téléphone");
			return new RedirectView("../responsables");
		}
		if (!rService.NuméroEstValide(responsable.getTel1())) {
			attrs.addFlashAttribute("erreurTel", "Numéro invalide");
			return new RedirectView("../responsables");
		}
		if (!responsable.getVille().equals("ifs") && !responsable.getVille().equals("Ifs")
				&& !responsable.getVille().equals("IFS")) {
			responsable.setQuotient_familial(null);
		}
		responsable.setToken(opt2.get().getToken());
		responsablerepo.save(responsable);
		return new RedirectView("../responsables");

	}

	@GetMapping("/delete/{role}/{id}")
	public RedirectView deleteAction(@PathVariable String role, @PathVariable int id) {

		if (role.equals("prof")) {

			profRepository.deleteById(id);

		} else if (role.equals("parent")) {

			responsablerepo.deleteById(id);

		} else if (role.equals("eleve")) {

			eleverepo.deleteById(id);

		}
		return new RedirectView("../../../responsables");

	}

	@GetMapping("/suspendre/{id}")
	public RedirectView suspendreAction(@PathVariable int id) {

		Optional<User> opt = userrepo.findById(id);

		if (opt.isPresent()) {

			if (opt.get().isSuspended() == true) {
				opt.get().setSuspended(false);
			} else {
				opt.get().setSuspended(true);
			}

			userrepo.save(opt.get());

		}

		return new RedirectView("../../responsables");

	}

}
