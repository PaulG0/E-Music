package sio.javanaise.emusic.controllers;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import io.github.jeemv.springboot.vuejs.VueJS;
import sio.javanaise.emusic.models.Eleve;
import sio.javanaise.emusic.models.Responsable;
import sio.javanaise.emusic.models.User;
import sio.javanaise.emusic.repositories.IEleveDAO;
import sio.javanaise.emusic.repositories.IResponsableDAO;
import sio.javanaise.emusic.repositories.IUserDAO;
import sio.javanaise.emusic.services.ResponsableService;
import sio.javanaise.emusic.services.TokenGenerator;
import sio.javanaise.emusic.services.UserService;

@Controller
@RequestMapping({ "/", "" })
public class MainController {

	@Autowired
	private IResponsableDAO parentrepo;

	@Autowired
	private IUserDAO userrepo;

	@Autowired
	private IEleveDAO enfantrepo;

	@Autowired()
	private UserDetailsService uService;

	@Autowired
	private ResponsableService rService;

	@Autowired(required = true)
	private VueJS vue;

	@Autowired
	private TokenGenerator tokgen;

	@ModelAttribute("vue")
	public VueJS getVue() {
		return this.vue;
	}

	@GetMapping("")
	public String indexAction(@AuthenticationPrincipal User authUser, ModelMap model, ModelMap model2) {
		Iterable<Responsable> responsables = parentrepo.findAll();
		Iterable<Eleve> eleves = enfantrepo.findAll();
		if (authUser != null) {
			String role = authUser.getAuthorities().toString();
			if (role.equals("[ROLE_PARENT]")) {

				for (Responsable responsable : responsables) {
					if (responsable.getToken().equals(authUser.getToken())) {
						parentrepo.findById(responsable.getId()).ifPresent(authResponsable -> {

							model2.put("authResponsable", authResponsable);
							vue.addData("authResponsable", authResponsable);
						});

					}
				}

			}
			if (role.equals("[ROLE_ELEVE]")) {
				for (Eleve eleve : eleves) {
					if (eleve.getToken().equals(authUser.getToken())) {
						enfantrepo.findById(eleve.getId()).ifPresent(authEleve -> {
							model2.put("authEleve", authEleve);
							vue.addData("authEleve", authEleve);
						});
					}
				}
			}
		}

		model.put("responsables", responsables);
		model.put("authUser", authUser);
		model.put("signup", "");
		model.put("login", "");
		vue.addData("affichage", false);
		vue.addData("authUser", authUser);
		vue.addData("villeAction");
		model.put("responsable", new Responsable());
		return "index";
	}

	@GetMapping("failure")
	public String failureAction(@AuthenticationPrincipal User authUser, ModelMap model, ModelMap model2) {
		Iterable<Responsable> responsables = parentrepo.findAll();
		if (authUser != null) {
			String role = authUser.getAuthorities().toString();
			if (role.equals("[ROLE_PARENT]")) {

				for (Responsable responsable : responsables) {
					if (responsable.getToken().equals(authUser.getToken())) {
						parentrepo.findById(responsable.getId()).ifPresent(authResponsable -> {

							model2.put("authResponsable", authResponsable);
							vue.addData("authResponsable", authResponsable);
						});

					}
				}

			}
		}

		model.put("responsables", responsables);
		model.put("authUser", authUser);
		model.put("signup", "");
		model.put("login", "$('.ui.modal.login').modal('show');");
		vue.addData("affichage", true);
		vue.addData("authUser", authUser);
		vue.addData("villeAction");
		model.put("responsable", new Responsable());
		return "index";
	}

	/*
	@GetMapping("new")
	public String newAction(@AuthenticationPrincipal User authUser, ModelMap model, ModelMap model2) {
		Iterable<Responsable> responsables = parentrepo.findAll();
		if (authUser != null) {
			String role = authUser.getAuthorities().toString();
			if (role.equals("[ROLE_PARENT]")) {

				for (Responsable responsable : responsables) {
					if (responsable.getToken().equals(authUser.getToken())) {
						parentrepo.findById(responsable.getId()).ifPresent(authResponsable -> {

							model2.put("authResponsable", authResponsable);
							vue.addData("authResponsable", authResponsable);
						});

					}
				}

			}
		}

		model.put("responsables", responsables);
		model.put("authUser", authUser);
		model.put("signup", "$('.ui.modal.signup').modal('show');");
		model.put("login", "");
		vue.addData("affichage", true);
		vue.addData("authUser", authUser);
		vue.addData("villeAction");
		model.put("responsable", new Responsable());
		return "index";
	}
*/
	@PostMapping("new")
	public RedirectView newAction(@ModelAttribute Responsable responsable, @ModelAttribute("password") String password,
			@ModelAttribute("login") String login, RedirectAttributes attrs) {
		vue.addData("affichage", false);
		Optional<User> opt2 = userrepo.findByLogin(login);
		if (opt2.isPresent()) {
			attrs.addFlashAttribute("erreurLogin", "login deja utilisée");
			return new RedirectView("");
		}
		if (login.length() < 5 || login.length() > 20) {
			attrs.addFlashAttribute("erreurLogin", "Votre login doit etre compris entre 5 et 20 caracteres");
		}
		if (!rService.NomEstValide(responsable.getNom())) {
			attrs.addFlashAttribute("erreurNom",
					"Nom invalide, veillez n'utiliser que des lettres latines, mettez une majuscule au debut. Les noms composés doivent etre séparés par des -");
			return new RedirectView("");
		}
		if (!rService.NomEstValide(responsable.getPrenom())) {
			attrs.addFlashAttribute("erreurPrenom",
					"Prenom invalide, veillez n'utiliser que des lettres latines, mettez une majuscule au debut. Les noms composés doivent etre séparés par des -");
			return new RedirectView("");
		}
		Optional<Responsable> opt = parentrepo.findByEmail(responsable.getEmail());
		if (opt.isPresent()) {
			attrs.addFlashAttribute("erreurEmail", "Adresse email deja utilisée");
			return new RedirectView("");
		}
		if (!rService.EmailEstValide(responsable.getEmail())) {
			attrs.addFlashAttribute("erreurEmail", "Adresse email invalide");
			return new RedirectView("");
		}
		if (password.length() < 8) {
			attrs.addFlashAttribute("erreurPassword", "Votre mot de passe doit contenir au moins 8 caracteres");
			return new RedirectView("/new/");
		}
		if (!rService.CodePostalEstValide(responsable.getCode_postal())) {
			attrs.addFlashAttribute("erreurCode", "Votre code postal doit contenir 5 chiffre");
			return new RedirectView("");
		}
		if (responsable.getTel1().equals("") || responsable.getTel1() == null) {
			attrs.addFlashAttribute("erreurTel", "Vous devez renseigner un numéro de téléphone");
			return new RedirectView("");
		}
		if (!rService.NuméroEstValide(responsable.getTel1())) {
			attrs.addFlashAttribute("erreurTel", "Numéro invalide");
			return new RedirectView("");
		}
		parentrepo.save(responsable);
		if (!responsable.getVille().equals("ifs") && !responsable.getVille().equals("Ifs")
				&& !responsable.getVille().equals("IFS")) {
			responsable.setQuotient_familial(null);
		}
		String token = tokgen.generateToken(login);
		User us = ((UserService) uService).createUser(login, password);
		us.setAuthorities("PARENT");
		us.setToken(token);
		userrepo.save(us);
		responsable.setToken(us.getToken());
		parentrepo.save(responsable);
		return new RedirectView("");
	}

	@GetMapping("deco")
	public RedirectView decoAction(HttpSession session, @AuthenticationPrincipal User authUser,
			RedirectAttributes attrs) {
		session.invalidate();
		return new RedirectView("/");
	}
}
