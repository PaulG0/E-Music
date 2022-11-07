package sio.Javanaise.emusic.controllers;

import java.util.Optional;

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
import sio.Javanaise.emusic.models.Responsable;
import sio.Javanaise.emusic.models.User;
import sio.Javanaise.emusic.repositories.IResponsableDAO;
import sio.Javanaise.emusic.repositories.IUserDAO;
import sio.Javanaise.emusic.services.ResponsableService;
import sio.Javanaise.emusic.services.UserService;

@Controller
@RequestMapping({ "/", "", "/index" })
public class MainController {

	@Autowired
	private IResponsableDAO parentrepo;

	@Autowired
	private IUserDAO userrepo;

	@Autowired()
	private UserDetailsService uService;

	@Autowired
	private ResponsableService rService;

	@Autowired(required = true)
	private VueJS vue;

	@ModelAttribute("vue")
	public VueJS getVue() {
		return this.vue;
	}

	@GetMapping("")
	public String indexAction(@AuthenticationPrincipal User authUser, ModelMap model) {
		Iterable<Responsable> responsables = parentrepo.findAll();
		model.put("responsables", responsables);
		model.put("authUser", authUser);
		vue.addData("authUser", authUser);
		return "index";
	}

	@GetMapping("new")
	public String newAction(ModelMap model) {
		model.put("responsable", new Responsable());
		return "signup";
	}

	@PostMapping("new")
	public RedirectView newAction(@ModelAttribute Responsable responsable, RedirectAttributes attrs) {
		Optional<Responsable> opt = parentrepo.findByEmail(responsable.getEmail());
		if (opt.isPresent()) {
			attrs.addFlashAttribute("erreurEmail", "Adresse email deja utilisée");
			return new RedirectView("/new/");
		}
		if (!rService.EmailEstValide(responsable.getEmail())) {
			attrs.addFlashAttribute("erreurEmail", "Adresse email invalide");
			return new RedirectView("/new/");
		}
		parentrepo.save(responsable);
		if (responsable.getQuotient_familial() >= 0) {
			if (!responsable.getVille().equals("ifs") && !responsable.getVille().equals("Ifs")
					&& !responsable.getVille().equals("IFS")) {
				responsable.setQuotient_familial(responsable.getQuotient_familial() * (-1));
			}
		} else {
			if (responsable.getVille().equals("ifs") || responsable.getVille().equals("Ifs")
					|| responsable.getVille().equals("IFS")) {
				responsable.setQuotient_familial(responsable.getQuotient_familial() * (-1));
			}
		}
		User us = ((UserService) uService).createUser(responsable.getEmail(), responsable.getPassword());
		us.setAuthorities("PARENT");
		us.setPrenom(responsable.getPrenom());
		userrepo.save(us);
		return new RedirectView("index");
	}
}
