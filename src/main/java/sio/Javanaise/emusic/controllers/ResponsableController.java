package sio.Javanaise.emusic.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
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
    	
		return "/responsables/index";
		
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
	
}
