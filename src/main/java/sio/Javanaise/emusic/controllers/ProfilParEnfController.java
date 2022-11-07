package sio.Javanaise.emusic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.jeemv.springboot.vuejs.VueJS;
import sio.Javanaise.emusic.models.Responsable;
import sio.Javanaise.emusic.models.User;
import sio.Javanaise.emusic.repositories.IResponsableDAO;
import sio.Javanaise.emusic.repositories.IUserDAO;

@Controller
@RequestMapping("/parent/")
public class ProfilParEnfController {

	@Autowired
	private IResponsableDAO parentrepo;

	@Autowired
	private IUserDAO userrepo;

	@Autowired()
	private UserDetailsService uService;

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
		return "/parent/profil";
	}
}
