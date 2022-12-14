package sio.javanaise.emusic.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
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
import sio.javanaise.emusic.models.User;
import sio.javanaise.emusic.repositories.IEleveRepository;
import sio.javanaise.emusic.repositories.IUserDAO;
import sio.javanaise.emusic.services.ResponsableService;
import sio.javanaise.emusic.services.TokenGenerator;
import sio.javanaise.emusic.services.UserService;

@Controller
@RequestMapping("/eleves")
public class EleveController {
	@Autowired
	Environment environment;
	@Autowired(required = true)
    private VueJS vue;

    @ModelAttribute("vue")
    public VueJS getVue() {
        return this.vue;
    }
    
    @Autowired
	private IUserDAO userrepo;
    
    @Autowired
    private IEleveRepository eleveRepository;
    
    @Autowired
	private ResponsableService rService;
    
    @Autowired
	private UserDetailsService uService;
    
    @Autowired
	private TokenGenerator tokgen;
    
    @PostMapping("/new")
    public RedirectView newAction(@ModelAttribute Eleve eleve, @ModelAttribute("dateNaissa") String dateNaissa, 
    		@ModelAttribute("password") String password, @ModelAttribute("login") String login, 
    		RedirectAttributes attrs) {
    	
    	Optional<User> opt2 = userrepo.findByLogin(login);
		if (opt2.isPresent()) {
			attrs.addFlashAttribute("erreurLogin", "login d??j?? utilis??");
			return new RedirectView("../responsables");
		}
		if (login.length() < 5 || login.length() > 20) {
			attrs.addFlashAttribute("erreurLogin", "Votre login doit ??tre compris entre 5 et 20 caract??res");
		}
		if (!rService.NomEstValide(eleve.getNom())) {
			attrs.addFlashAttribute("erreurNom",
					"Nom invalide, veuillez n'utiliser que des lettres latines, mettez une majuscule au d??but. Les noms compos??s doivent ??tre s??par??s par des -");
			return new RedirectView("../responsables");
		}
		if (!rService.NomEstValide(eleve.getPrenom())) {
			attrs.addFlashAttribute("erreurPrenom",
					"Prenom invalide, veuillez n'utiliser que des lettres latines, mettez une majuscule au d??but. Les noms compos??s doivent ??tre s??par??s par des -");
			return new RedirectView("../responsables");
		}
		if (password.length() < 8) {
			attrs.addFlashAttribute("erreurPassword", "Votre mot de passe doit contenir au moins 8 caract??res");
			return new RedirectView("../responsables");
		}
		if(eleve.getToken() == null) {
			String token = tokgen.generateToken(login);
			User us = ((UserService) uService).createUser(login, password);
			us.setAuthorities("ELEVE");
			us.setToken(token);
			userrepo.save(us);
			eleve.setToken(us.getToken());
		}
		LocalDate dateNaissance = LocalDate.parse(dateNaissa, DateTimeFormatter.ofPattern("yyy-MM-dd"));
		eleve.setDateNaiss(dateNaissance);
    	eleveRepository.save(eleve);
    	return new RedirectView("../responsables");
    	
    }
    
    @PostMapping("/edit")
    public RedirectView editAction(@ModelAttribute Eleve eleve, @ModelAttribute("dateNaissa") String dateNaissa, 
    		RedirectAttributes attrs) {
    	
		if (!rService.NomEstValide(eleve.getNom())) {
			attrs.addFlashAttribute("erreurNom",
					"Nom invalide, veuillez n'utiliser que des lettres latines, mettez une majuscule au d??but. Les noms compos??s doivent ??tre s??par??s par des -");
			return new RedirectView("../responsables");
		}
		if (!rService.NomEstValide(eleve.getPrenom())) {
			attrs.addFlashAttribute("erreurPrenom",
					"Prenom invalide, veuillez n'utiliser que des lettres latines, mettez une majuscule au d??but. Les noms compos??s doivent ??tre s??par??s par des -");
			return new RedirectView("../responsables");
		}
		Optional<Eleve> opt = eleveRepository.findById(eleve.getId());
		if(dateNaissa == "" || dateNaissa == null) {
			eleve.setDateNaiss(opt.get().getDateNaiss());
		} else {
			LocalDate dateNaissance = LocalDate.parse(dateNaissa, DateTimeFormatter.ofPattern("yyy-MM-dd"));
			eleve.setDateNaiss(dateNaissance);
		}
		
		eleve.setToken(opt.get().getToken());
    	eleveRepository.save(eleve);
    	return new RedirectView("../responsables");
    	
    }
	
}
