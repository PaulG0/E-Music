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
	public String indexAction(@AuthenticationPrincipal User authUser, ModelMap model, ModelMap model2) {
		String role = authUser.getAuthorities().toString();
		Iterable<Responsable> responsables = parentrepo.findAll();
		System.out.println(role);
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
}
