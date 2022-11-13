package sio.Javanaise.emusic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import io.github.jeemv.springboot.vuejs.VueJS;
import sio.Javanaise.emusic.models.Prof;
import sio.Javanaise.emusic.repositories.IProfRepository;

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
    private IProfRepository profRepository;
    
    @PostMapping("/new")
    public RedirectView newAction(@ModelAttribute Prof prof) {
    	
    	profRepository.save(prof);
    	return new RedirectView("/responsables");
    	
    }
	
}
