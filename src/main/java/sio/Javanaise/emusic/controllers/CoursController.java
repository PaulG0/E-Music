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
import sio.Javanaise.emusic.models.Cour;
import sio.Javanaise.emusic.repositories.ICoursRepository;

@Controller
@RequestMapping({ "/cours", "/cours/" })
public class CoursController {

    @Autowired(required = true)
    private VueJS vue;

    @ModelAttribute("vue")
    public VueJS getVue() {
        return this.vue;
    }
    
    @Autowired
    private ICoursRepository coursRepository;

    @GetMapping("")
    public String indexCoursAction() {
        return "/cours/index";
    }
    
    @GetMapping("/new")
    public String coursNewAction(ModelMap model) {
    	
    	model.put("cour", new Cour());
    	return "/cours/form";
    	
    }
    
    @PostMapping("/new")
    public RedirectView CoursNewAction(@ModelAttribute Cour cour) {
    	
    	coursRepository.save(cour);
    	return new RedirectView("/cours");
    	
    }
    
    @GetMapping("/delete/{id}")
    public RedirectView coursDeleteAction(@PathVariable int id) {
    	
    	coursRepository.deleteById(id);
    	return new RedirectView("/cours");
    	
    }
    
    @GetMapping("/edit/{id}")
    public String coursEditAction(ModelMap model, @PathVariable int id) {
    	
    	coursRepository.findById(id).ifPresent(cour -> model.put("cour", cour));
    	return "/cours/form";
    	
    }
    
}
