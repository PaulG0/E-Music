package sio.Javanaise.emusic.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import io.github.jeemv.springboot.vuejs.VueJS;
import sio.Javanaise.emusic.models.Eleve;
import sio.Javanaise.emusic.models.Responsable;
import sio.Javanaise.emusic.repositories.IEleveRepository;
import sio.Javanaise.emusic.repositories.IResponsableRepository;

@Controller
@RequestMapping("/responsables")
public class ResponsableController {

	@Autowired(required = true)
    private VueJS vue;

    @ModelAttribute("vue")
    public VueJS getVue() {
        return this.vue;
    }
    
    @Autowired
    private IResponsableRepository responsablerepo;
    
    @Autowired
    private IEleveRepository eleverepo;
	
    @GetMapping({"/index", ""})
	public String indexAction(ModelMap model) {
		
    	vue.addData("responsables", responsablerepo.findAll());
    	vue.addData("eleves", eleverepo.findAll());
		return "/responsables/index";
		
	}
	
}
