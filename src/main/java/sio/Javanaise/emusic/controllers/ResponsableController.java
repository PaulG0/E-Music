package sio.Javanaise.emusic.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import sio.Javanaise.emusic.models.Eleve;
import sio.Javanaise.emusic.models.Responsable;
import sio.Javanaise.emusic.repositories.IEleveRepository;
import sio.Javanaise.emusic.repositories.IResponsableRepository;
import sio.Javanaise.emusic.services.UIResponsableService;

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
    
    @Autowired
    private UIResponsableService responsableService;
	
    @GetMapping({"/index", ""})
	public String indexAction(ModelMap model) {
		
    	vue.addData("messageOption");
    	vue.addData("membre");
    	vue.addData("toDelete");
    	vue.addData("unRole");
    	vue.addData("responsables", responsablerepo.findAll());
    	vue.addData("eleves", eleverepo.findAll());
    	
    	vue.addMethod("confMessageOption", "this.messageOption=membre; this.unRole=unRole;", "membre, unRole");
    	vue.addMethod("popupDelete", "this.toDelete=membre;"
				+ responsableService.modalDelete() + ";", "membre");
    	vue.addMethod("popupSuspendre", "this.membre=membre;"
    			+ responsableService.modalSuspendre() + ";", "membre");
    	
		return "/responsables/index";
		
	}
    
    @GetMapping("/new")
    public String newAction(ModelMap model) {
    	
    	model.put("responsable", new Responsable());
    	return "/responsables/form";
    }
    
    @PostMapping("/new")
    public RedirectView newAction(@ModelAttribute Responsable responsable) {
    	
    	if(responsable.getRole() == null) {
    		
    		Optional<Responsable> opt = responsablerepo.findById(responsable.getId());
    		
    		if(opt.isPresent()) {
    			responsable.setRole(opt.get().getRole());
    		}
    		
    	}
    	responsablerepo.save(responsable);
    	return new RedirectView("/responsables/index");
    	
    }
    
    @GetMapping("/delete/{role}/{id}")
    public RedirectView deleteAction(@PathVariable String role, @PathVariable int id) {
    	
    	if(role.equals("prof") || role.equals("parent")) {
    		
    		responsablerepo.deleteById(id);
    		
    	} else if(role.equals("eleve")) {
    		
    		eleverepo.deleteById(id);
    		
    	}
    	return new RedirectView("/responsables/index");
    	
    }
    
    @GetMapping("/edit/{id}")
    public String editAction(ModelMap model, @PathVariable int id) {
    	
    	responsablerepo.findById(id).ifPresent(responsable -> model.put("responsable", responsable));
    	return "/responsables/form";
    	
    }
    
    @GetMapping("/suspendre/{id}")
    public RedirectView suspendreAction(@PathVariable int id) {
    	
    	Optional<Responsable> opt = responsablerepo.findById(id);
    	
    	if(opt.isPresent()) {
    		
    		if(opt.get().isSuspendre() == true) {
    			opt.get().setSuspendre(false);
    		} else {
    			opt.get().setSuspendre(true);
    		}
    		
    		responsablerepo.save(opt.get());
    		
    	}
    	
    	return new RedirectView("/responsables/index");
    	
    }
	
}
