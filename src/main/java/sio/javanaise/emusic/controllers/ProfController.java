package sio.javanaise.emusic.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import io.github.jeemv.springboot.vuejs.VueJS;
import sio.javanaise.emusic.models.Prof;
import sio.javanaise.emusic.models.User;
import sio.javanaise.emusic.repositories.IProfRepository;
import sio.javanaise.emusic.repositories.IUserDAO;
import sio.javanaise.emusic.services.ResponsableService;
import sio.javanaise.emusic.services.TokenGenerator;
import sio.javanaise.emusic.services.UserService;

@Controller
@RequestMapping("/profs")
public class ProfController {

	@Autowired(required = true)
    private VueJS vue;

    @ModelAttribute("vue")
    public VueJS getVue() {
        return this.vue;
    }
    
    @Autowired
	private IUserDAO userrepo;
    
    @Autowired
    private IProfRepository profRepository;
    
    @Autowired
	private ResponsableService rService;
    
    @Autowired
	private UserDetailsService uService;
    
    @Autowired
	private TokenGenerator tokgen;
    
    @PostMapping("/new")
    public RedirectView newAction(@ModelAttribute Prof prof, @ModelAttribute("password") String password,
			@ModelAttribute("login") String login, RedirectAttributes attrs) {
    	
    	Optional<User> opt2 = userrepo.findByLogin(login);
		if (opt2.isPresent()) {
			attrs.addFlashAttribute("erreurLogin", "login déjà utilisé");
			return new RedirectView("/responsables");
		}
		if (login.length() < 5 || login.length() > 20) {
			attrs.addFlashAttribute("erreurLogin", "Votre login doit être compris entre 5 et 20 caractères");
		}
		if (!rService.NomEstValide(prof.getNom())) {
			attrs.addFlashAttribute("erreurNom",
					"Nom invalide, veuillez n'utiliser que des lettres latines, mettez une majuscule au début. Les noms composés doivent être séparés par des -");
			return new RedirectView("/responsables");
		}
		if (!rService.NomEstValide(prof.getPrenom())) {
			attrs.addFlashAttribute("erreurPrenom",
					"Prenom invalide, veuillez n'utiliser que des lettres latines, mettez une majuscule au début. Les noms composés doivent être séparés par des -");
			return new RedirectView("/responsables");
		}
		Optional<Prof> opt = profRepository.findByEmail(prof.getEmail());
		if (opt.isPresent()) {
			attrs.addFlashAttribute("erreurEmail", "Adresse email déjà utilisée");
			return new RedirectView("/responsables");
		}
		if (!rService.EmailEstValide(prof.getEmail())) {
			attrs.addFlashAttribute("erreurEmail", "Adresse email invalide");
			return new RedirectView("/responsables");
		}
		if (password.length() < 8) {
			attrs.addFlashAttribute("erreurPassword", "Votre mot de passe doit contenir au moins 8 caractères");
			return new RedirectView("/responsables");
		}
		if(prof.getToken() == null) {
			String token = tokgen.generateToken(login);
			User us = ((UserService) uService).createUser(login, password);
			us.setAuthorities("PROF");
			us.setToken(token);
			userrepo.save(us);
			prof.setToken(us.getToken());
		}
		profRepository.save(prof);
		return new RedirectView("/responsables");
    	
    }
	
}
