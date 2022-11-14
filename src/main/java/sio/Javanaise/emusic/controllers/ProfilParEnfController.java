package sio.Javanaise.emusic.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
import sio.Javanaise.emusic.models.Eleve;
import sio.Javanaise.emusic.models.Responsable;
import sio.Javanaise.emusic.models.User;
import sio.Javanaise.emusic.repositories.IEleveDAO;
import sio.Javanaise.emusic.repositories.IResponsableDAO;
import sio.Javanaise.emusic.repositories.IUserDAO;
import sio.Javanaise.emusic.services.ResponsableService;
import sio.Javanaise.emusic.services.TokenGenerator;
import sio.Javanaise.emusic.services.UserService;

@Controller
@RequestMapping("/parent/")
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

	@GetMapping("add/{id}")
	public String addAction(ModelMap model, @PathVariable int id) {
		model.put("eleve", new Eleve());
		vue.addData("id_parent", id);
		return "/parent/add";
	}

	@PostMapping("add")
	public RedirectView addAction(@ModelAttribute Eleve eleve, @ModelAttribute("password") String password,
			@ModelAttribute("login") String login, RedirectAttributes attrs) {
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
			return new RedirectView("/new/");
		}
		if (!rService.NomEstValide(eleve.getPrenom())) {
			attrs.addFlashAttribute("erreurPrenom",
					"Prenom invalide, veillez n'utiliser que des lettres latines, mettez une majuscule au debut. Les noms composés doivent etre séparés par des -");
			return new RedirectView("/new/");
		}
		String token = tokgen.generateToken(login);
		User us = ((UserService) uService).createUser(login, password);
		us.setAuthorities("ELEVE");
		us.setToken(token);
		userrepo.save(us);
		eleve.setToken(us.getToken());

		enfantrepo.save(eleve);
		return new RedirectView("/parent/");
	}
}
