package sio.Javanaise.emusic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import io.github.jeemv.springboot.vuejs.VueJS;
import sio.Javanaise.emusic.models.Responsable;
import sio.Javanaise.emusic.models.User;
import sio.Javanaise.emusic.repositories.IResponsableDAO;
import sio.Javanaise.emusic.repositories.IUserDAO;
import sio.Javanaise.emusic.services.UserService;

@Controller
@RequestMapping({ "/", "" })
public class MainController {

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
		return "index";
	}

	@GetMapping("new")
	public String newAction(ModelMap model) {
		model.put("responsable", new Responsable());
		return "signup";
	}

	@PostMapping("new")
	public RedirectView newAction(@ModelAttribute Responsable responsable) {
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
		userrepo.save(us);
		return new RedirectView("index");
	}
}
