package sio.Javanaise.emusic.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import io.github.jeemv.springboot.vuejs.VueJS;
import sio.Javanaise.emusic.models.Paiement;
import sio.Javanaise.emusic.repositories.IPaiementRepository;
import sio.Javanaise.emusic.services.UIPaiementService;

@Controller
@RequestMapping("/paiements")
public class PaiementController {

	@Autowired(required = true)
    private VueJS vue;

    @ModelAttribute("vue")
    public VueJS getVue() {
        return this.vue;
    }
    
    @Autowired
    private IPaiementRepository paiementRepository;
    
    @Autowired
    private UIPaiementService paiementService;
    
    @GetMapping("/new/{id}")
    public String newAction(ModelMap model, @PathVariable int id) {
    	
    	vue.addMethod("foncCalendar", paiementService.calendarUI());
    	
    	model.put("paiement", new Paiement());
    	return "/paiements/form";
    	
    }
    
    @PostMapping("/new")
    public RedirectView newAction(@ModelAttribute Paiement paiement) {
    	
    	paiementRepository.save(paiement);
    	return new RedirectView("/responsables/index");
    	
    }
    
    @GetMapping("/edit/{id}")
    public String editAction(ModelMap model, @PathVariable int id) {
    	
    	paiementRepository.findById(id).ifPresent(paiement -> model.put("paiement", paiement));
    	return "/paiements/form";
    	
    }
	
}
