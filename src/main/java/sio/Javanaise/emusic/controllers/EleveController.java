package sio.Javanaise.emusic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import io.github.jeemv.springboot.vuejs.VueJS;
import sio.Javanaise.emusic.models.Eleve;
import sio.Javanaise.emusic.repositories.IEleveRepository;

@Controller
@RequestMapping("/eleves")
public class EleveController {

	@Autowired(required = true)
    private VueJS vue;

    @ModelAttribute("vue")
    public VueJS getVue() {
        return this.vue;
    }
    
    @Autowired
    private IEleveRepository eleveRepository;
    
    @PostMapping("/new")
    public RedirectView newAction(@ModelAttribute Eleve eleve) {
    	
    	eleveRepository.save(eleve);
    	return new RedirectView("/responsables");
    	
    }
	
}