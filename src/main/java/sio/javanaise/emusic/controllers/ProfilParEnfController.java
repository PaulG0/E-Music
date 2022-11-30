package sio.javanaise.emusic.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import sio.javanaise.emusic.models.Inscription;
import sio.javanaise.emusic.models.Planning;
import sio.javanaise.emusic.models.Responsable;
import sio.javanaise.emusic.models.User;
import sio.javanaise.emusic.repositories.IEleveDAO;
import sio.javanaise.emusic.repositories.IInscriptionRepository;
import sio.javanaise.emusic.repositories.IPlanningRepository;
import sio.javanaise.emusic.repositories.IResponsableDAO;
import sio.javanaise.emusic.repositories.IUserDAO;
import sio.javanaise.emusic.services.FormatService;
import sio.javanaise.emusic.services.ResponsableService;
import sio.javanaise.emusic.services.TokenGenerator;
import sio.javanaise.emusic.services.UserService;
import sio.javanaise.emusic.ui.UILink;
import sio.javanaise.emusic.ui.UIMessage;

@Controller
@RequestMapping({ "/parent", "/parent/", "/parent/profil" })
public class ProfilParEnfController {

	@Autowired
	private IResponsableDAO parentrepo;

	@Autowired
	private IUserDAO userrepo;

	@Autowired
	private IEleveDAO enfantrepo;

	@Autowired
	private IPlanningRepository planrepo;

	@Autowired
	private IInscriptionRepository inscrepo;

	@Autowired()
	private UserDetailsService uService;

	@Autowired
	private ResponsableService rService;

	@Autowired
	private FormatService fService;

	@Autowired
	private TokenGenerator tokgen;

	@Autowired
	private PasswordEncoder passwordEncoder;

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
		Iterable<Eleve> eleves = enfantrepo.findAll();
		Iterable<Planning> plannings = planrepo.findAll();
		if (role.equals("[ROLE_PARENT]")) {
			for (Responsable responsable : responsables) {
				if (responsable.getToken().equals(authUser.getToken())) {
					parentrepo.findById(responsable.getId()).ifPresent(authResponsable -> {
						model2.put("authResponsable", authResponsable);
						vue.addData("authResponsable", authResponsable);

						vue.addData("villeAction", authResponsable.getVille());
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

		model.put("plannings", plannings);

		model.put("authUser", authUser);
		model.put("edit", "");
		model.put("editPassword", "");
		vue.addData("authUser", authUser);
		vue.addData("infoAff", false);
		vue.addData("modPass", false);
		return "/parent/profil";
	}

	@Secured("ROLE_PARENT")
	@GetMapping("add")
	public String addAction(@AuthenticationPrincipal User authUser, ModelMap model) {
		model.put("eleve", new Eleve());
		model.put("authUser", authUser);
		vue.addData("authUser", authUser);
		return "./parent/form";
	}

	@Secured("ROLE_PARENT")
	@PostMapping("add")
	public RedirectView addAction(@AuthenticationPrincipal User authUser, ModelMap model,
			@ModelAttribute("dateNaissa") String dateNaissa, @ModelAttribute Eleve eleve,
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
			model.put("authUser", authUser);
			vue.addData("authUser", authUser);
			return new RedirectView("../../parent/add/");
		}
		if (login.length() < 5 || login.length() > 20) {
			attrs.addFlashAttribute("erreurLogin", "Votre login doit etre compris entre 5 et 20 caracteres");
		}
		if (!rService.NomEstValide(eleve.getNom())) {
			attrs.addFlashAttribute("erreurNom",
					"Nom invalide, veillez n'utiliser que des lettres latines, mettez une majuscule au debut. Les noms composés doivent etre séparés par des -");
			model.put("authUser", authUser);
			vue.addData("authUser", authUser);
			return new RedirectView("../../parent/add/");
		}
		if (!rService.NomEstValide(eleve.getPrenom())) {
			attrs.addFlashAttribute("erreurPrenom",
					"Prenom invalide, veillez n'utiliser que des lettres latines, mettez une majuscule au debut. Les noms composés doivent etre séparés par des -");
			model.put("authUser", authUser);
			vue.addData("authUser", authUser);
			return new RedirectView("../../parent/add/");
		}
		String token = tokgen.generateToken(login);
		User us = ((UserService) uService).createUser(login, password);
		us.setAuthorities("ELEVE");
		us.setToken(token);
		userrepo.save(us);
		eleve.setToken(us.getToken());
		LocalDate dateNaissance = LocalDate.parse(dateNaissa, DateTimeFormatter.ofPattern("yyy-MM-dd"));
		eleve.setDateNaiss(dateNaissance);

		enfantrepo.save(eleve);
		return new RedirectView("../../parent/profil");
	}

	@Secured("ROLE_PARENT")
	@PostMapping("edit")
	public RedirectView editAction(@AuthenticationPrincipal User authUser, @ModelAttribute Responsable responsable,
			RedirectAttributes attrs) {
		System.out.println("L'erreur est au debut");
		User us = authUser;
		if (!rService.NomEstValide(responsable.getNom())) {
			attrs.addFlashAttribute("erreurNom",
					"Nom invalide, veillez n'utiliser que des lettres latines, mettez une majuscule au debut. Les noms composés doivent etre séparés par des -");
			return new RedirectView("./parent/");
		}
		System.out.println("Il passe le nom");
		if (!rService.NomEstValide(responsable.getPrenom())) {
			attrs.addFlashAttribute("erreurPrenom",
					"Prenom invalide, veillez n'utiliser que des lettres latines, mettez une majuscule au debut. Les noms composés doivent etre séparés par des -");
			return new RedirectView("./parent/");
		}
		System.out.println("Il passe le prenom");
		Optional<Responsable> opt = parentrepo.findByEmail(responsable.getEmail());
		Optional<Responsable> opt2 = parentrepo.findById(responsable.getId());
		String emailResp = opt2.get().getEmail();
		if (opt.isPresent() && !responsable.getEmail().equals(emailResp)) {
			attrs.addFlashAttribute("erreurEmail", "Adresse email deja utilisée");
			return new RedirectView("./parent/");
		}
		System.out.println("Il passe le mail");
		if (!rService.EmailEstValide(responsable.getEmail())) {
			attrs.addFlashAttribute("erreurEmail", "Adresse email invalide");
			return new RedirectView("./parent/");
		}
		System.out.println("Il passe le mail2");
		if (!rService.CodePostalEstValide(responsable.getCode_postal())) {
			attrs.addFlashAttribute("erreurCode", "Votre code postal doit contenir 5 chiffre");
			return new RedirectView("./parent/");
		}
		System.out.println("Il passe le cp");
		if (responsable.getTel1().equals("") || responsable.getTel1() == null) {
			attrs.addFlashAttribute("erreurTel", "Vous devez renseigner un numéro de téléphone");
			return new RedirectView("./parent/");
		}
		System.out.println("Il passe le tel1");
		if (!rService.NuméroEstValide(responsable.getTel1())) {
			attrs.addFlashAttribute("erreurTel", "Numéro invalide");
			return new RedirectView("./parent/");
		}
		System.out.println("Il passe le tel2");
		if (!responsable.getVille().equals("ifs") && !responsable.getVille().equals("Ifs")
				&& !responsable.getVille().equals("IFS")) {
			responsable.setQuotient_familial(null);
		}
		System.out.println("Il passe le tel3");
		responsable.setToken(us.getToken());
		parentrepo.save(responsable);
		System.out.println("Il a sauvegarder");
		return new RedirectView("./parent/");
	}

	@Secured("ROLE_PARENT")
	@PostMapping("editLogin")
	public RedirectView editLoginAction(@AuthenticationPrincipal User authUser, @ModelAttribute("login") String login,
			RedirectAttributes attrs) {
		Optional<User> opt2 = userrepo.findByLogin(login);
		if (opt2.isPresent()) {
			attrs.addFlashAttribute("erreurLogin", "login deja utilisée");
			return new RedirectView("./parent/");
		}
		if ((login.length() < 5) || (login.length() > 20)) {
			attrs.addFlashAttribute("erreurLogin", "Votre login doit etre compris entre 5 et 20 caracteres");
			return new RedirectView("./parent/");
		}
		authUser.setLogin(login);
		userrepo.save(authUser);
		return new RedirectView("./parent/");
	}

	@Secured("ROLE_PARENT")
	@PostMapping("editPassword")
	public RedirectView editPasswordAction(@AuthenticationPrincipal User authUser,
			@ModelAttribute("oldPassword") String oldPassword, @ModelAttribute("newPassword") String newPassword,
			RedirectAttributes attrs) {
		String oldHashPassword = authUser.getPassword();
		if (passwordEncoder.matches(oldPassword, oldHashPassword) && newPassword.length() >= 8) {
			authUser.setPassword(passwordEncoder.encode(newPassword));
			userrepo.save(authUser);
		} else if (passwordEncoder.matches(oldPassword, oldHashPassword) && newPassword.length() < 8) {
			attrs.addFlashAttribute("erreurPass", "Le nouveau mot de passe doit comprendre au moins 8 caracteres");
		} else {
			attrs.addFlashAttribute("erreurPass", "Mot de passe invalide");
		}
		return new RedirectView("./parent/");
	}

	@Secured("ROLE_PARENT")
	@GetMapping("delete")
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
										.addLinks(new UILink("oui", "./parent/delete/force/"),
												new UILink("non", "./parent/")));
					});
				}
			}
		}
		return new RedirectView("../");
	}

	@Secured("ROLE_PARENT")
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
		return new RedirectView("./index");
	}

	@Secured("ROLE_PARENT")
	@PostMapping("inscription")
	public RedirectView insAction(@ModelAttribute Inscription inscription, @ModelAttribute("eleve") Eleve eleve,
			@ModelAttribute("planning") Planning planning) {
		System.out.println(fService.getAge(eleve));
		inscription.setEleve(eleve);
		inscription.setPlanning(planning);
		inscrepo.save(inscription);
		return new RedirectView("../cours/");
	}
}
