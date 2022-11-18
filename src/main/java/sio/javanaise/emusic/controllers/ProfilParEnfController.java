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
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.jeemv.springboot.vuejs.VueJS;
import sio.javanaise.emusic.models.Responsable;
import sio.javanaise.emusic.models.User;
import sio.javanaise.emusic.repositories.IResponsableDAO;
import sio.javanaise.emusic.repositories.IUserDAO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import sio.javanaise.emusic.models.Eleve;
import sio.javanaise.emusic.repositories.IEleveDAO;
import sio.javanaise.emusic.services.ResponsableService;
import sio.javanaise.emusic.services.TokenGenerator;
import sio.javanaise.emusic.services.UserService;
import sio.javanaise.emusic.ui.UILink;
import sio.javanaise.emusic.ui.UIMessage;

@Controller
@RequestMapping({ "/parent/", "/parent/profil" })
public class ProfilParEnfController {

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

	@Autowired
	private TokenGenerator tokgen;

	@Autowired(required = true)
	private VueJS vue;

	@ModelAttribute("vue")
	public VueJS getVue() {
		return this.vue;
	}

	@GetMapping("")
	public String indexAction(@AuthenticationPrincipal User authUser, ModelMap model, ModelMap model2) {
		String role = authUser.getAuthorities().toString();
		Iterable<Responsable> responsables = parentrepo.findAll();
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
		model.put("authUser", authUser);
		vue.addData("authUser", authUser);
		return "/parent/profil";
	}

	@GetMapping("add")
	public String addAction(ModelMap model) {
		model.put("eleve", new Eleve());
		return "/parent/form";
	}

	@PostMapping("add")
	public RedirectView addAction(@AuthenticationPrincipal User authUser, @ModelAttribute Eleve eleve,
			@ModelAttribute("password") String password, @ModelAttribute("login") String login, ModelMap model2,
			RedirectAttributes attrs) {
		Iterable<Responsable> responsables = parentrepo.findAll();
		for (Responsable responsable : responsables) {
			if (responsable.getToken().equals(authUser.getToken())) {
				parentrepo.findById(responsable.getId()).ifPresent(authResponsable -> {
					model2.put("authResponsable", authResponsable);
					vue.addData("authResponsable", authResponsable);
					eleve.setResponsable(authResponsable);
				});
			}
		}

		Optional<User> opt2 = userrepo.findByLogin(login);
		if (opt2.isPresent()) {
			attrs.addFlashAttribute("erreurLogin", "login deja utilisée");
			return new RedirectView("/parent/add/");
		}
		if (login.length() < 5 || login.length() > 20) {
			attrs.addFlashAttribute("erreurLogin", "Votre login doit etre compris entre 5 et 20 caracteres");
		}
		if (!rService.NomEstValide(eleve.getNom())) {
			attrs.addFlashAttribute("erreurNom",
					"Nom invalide, veillez n'utiliser que des lettres latines, mettez une majuscule au debut. Les noms composés doivent etre séparés par des -");
			return new RedirectView("/parent/add/");
		}
		if (!rService.NomEstValide(eleve.getPrenom())) {
			attrs.addFlashAttribute("erreurPrenom",
					"Prenom invalide, veillez n'utiliser que des lettres latines, mettez une majuscule au debut. Les noms composés doivent etre séparés par des -");
			return new RedirectView("/parent/add/");
		}
		String token = tokgen.generateToken(login);
		User us = ((UserService) uService).createUser(login, password);
		us.setAuthorities("ELEVE");
		us.setToken(token);
		userrepo.save(us);
		eleve.setToken(us.getToken());
		enfantrepo.save(eleve);
		return new RedirectView("/parent/profil");
	}

	@GetMapping("delete/")
	public RedirectView deleteAction(@AuthenticationPrincipal User authUser, ModelMap model2,
			RedirectAttributes attrs) {
		String role = authUser.getAuthorities().toString();
		Iterable<Responsable> responsables = parentrepo.findAll();
		if (role.equals("[ROLE_PARENT]")) {
			for (Responsable responsable : responsables) {
				if (responsable.getToken().equals(authUser.getToken())) {
					parentrepo.findById(responsable.getId()).ifPresent(authResponsable -> {
						model2.put("authResponsable", authResponsable);
						attrs.addFlashAttribute("msg",
								UIMessage.error("Suppression", "Voulez vous vraiment supprimer votre compte ?")
										.addLinks(new UILink("oui", "/parent/delete/force/"),
												new UILink("non", "/parent/")));
					});
				}
			}
		}
		return new RedirectView("/parent/");
	}

	@GetMapping("delete/force")
	public RedirectView deleteForceAction(HttpSession session, @AuthenticationPrincipal User authUser, ModelMap model2,
			RedirectAttributes attrs) {
		String role = authUser.getAuthorities().toString();
		Iterable<Responsable> responsables = parentrepo.findAll();
		if (role.equals("[ROLE_PARENT]")) {
			for (Responsable responsable : responsables) {
				if (responsable.getToken().equals(authUser.getToken())) {
					if (parentrepo.findById(responsable.getId()).isPresent()) {
						for (User eleveUser : userrepo.findAll()) {
							for (Eleve eleve : responsable.getEleves()) {
								if (eleveUser.getToken().equals(eleve.getToken())) {
									userrepo.deleteById(eleveUser.getId());
								}
							}
						}
						parentrepo.deleteById(responsable.getId());
					}
					if (userrepo.findById(authUser.getId()).isPresent()) {
						userrepo.deleteById(authUser.getId());
					}
					session.invalidate();
				}
			}
		}
		return new RedirectView("/index");
	}

}
