package sio.javanaise.emusic.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import io.github.jeemv.springboot.vuejs.VueJS;
import sio.javanaise.emusic.models.Cour;
import sio.javanaise.emusic.models.Paiement;
import sio.javanaise.emusic.models.User;
import sio.javanaise.emusic.repositories.ICoursRepository;
import sio.javanaise.emusic.repositories.IPaiementRepository;

@Controller
@RequestMapping("/paiements")
public class PaiementController {


	@Autowired
	Environment environment;
	@Autowired(required = true)
    private VueJS vue;

    @ModelAttribute("vue")
    public VueJS getVue() {
        return this.vue;
    }
    
    @Autowired
    private IPaiementRepository paiementRepository;
    
    @Autowired
    private ICoursRepository coursRepository;
    
    @GetMapping("")
    public String indexAction(@AuthenticationPrincipal User authUser, ModelMap model) {
    	
    	Iterable<Paiement> paiements = paiementRepository.findAllByOrderByDateTransmission();
    	model.put("paiements", paiements);
		model.put("base", environment.getProperty("app.base"));
    	model.put("authUser", authUser);
		vue.addData("authUser", authUser);
    	return "/paiements/index";
    	
    }
    
    @PostMapping("/datepaiement")
    public String indexDatePaiementAction(@AuthenticationPrincipal User authUser, ModelMap model, @ModelAttribute("dateStart") String dateStart, @ModelAttribute("dateEnd") String dateEnd) {
    	
    	LocalDate theDateStart = LocalDate.parse(dateStart, DateTimeFormatter.ofPattern("yyy-MM-dd"));
    	LocalDate theDateEnd = LocalDate.parse(dateEnd, DateTimeFormatter.ofPattern("yyy-MM-dd"));
    	
    	Iterable<Paiement> paiements = paiementRepository.findByDateTransmissionBetween(theDateStart, theDateEnd);
    	model.put("paiements", paiements);
		model.put("base", environment.getProperty("app.base"));
    	model.put("authUser", authUser);
		vue.addData("authUser", authUser);
    	return "/paiements/index";
    	
    }
    
    @PostMapping("/cours")
    public String indexCoursAction(@AuthenticationPrincipal User authUser, ModelMap model, @ModelAttribute("libelle") String libelle) {
    	
    	Cour cour = coursRepository.findOneByLibelleContainingIgnoreCase(libelle);
    	
    	if(cour == null) {
    		
    		ArrayList<Paiement> listPaiements = new ArrayList<>();
        	model.put("paiements", listPaiements);
    		
    	} else {
    		
    		Iterable<Paiement> paiements = paiementRepository.findAllByOrderByDateTransmission();
    		ArrayList<Paiement> listPaiements = new ArrayList<>();
    		
    		for (Paiement paiement : paiements) {
				
    			String test = paiement.getFacture().getInscription().getPlanning().getCour().getLibelle();
    			
    			if(test == cour.getLibelle()) {
    				listPaiements.add(paiement);
        		}
    			
			}
    		
    		model.put("paiements", listPaiements);
    		
    	}
		model.put("base", environment.getProperty("app.base"));
    	model.put("authUser", authUser);
		vue.addData("authUser", authUser);
    	return "/paiements/index";
    	
    }
    
    @GetMapping("/new/{idFacture}")
    public String newAction(@AuthenticationPrincipal User authUser, ModelMap model, @PathVariable int idFacture) {
    	
    	vue.addData("facture", idFacture);
    	model.put("paiement", new Paiement());
    	model.put("authUser", authUser);
		vue.addData("authUser", authUser);
    	return "/paiements/form";
    	
    }
    
    @PostMapping("/new")
    public RedirectView newAction(@ModelAttribute Paiement paiement, @ModelAttribute("laDateTransmission") String laDateTransmission) {
    	
    	LocalDate dateTransmission = LocalDate.parse(laDateTransmission, DateTimeFormatter.ofPattern("yyy-MM-dd"));
    	paiement.setDateTransmission(dateTransmission);
    	
    	paiementRepository.save(paiement);
    	return new RedirectView("../paiements");
    	
    }
    
    @GetMapping("/edit/{id}")
    public String editAction(@AuthenticationPrincipal User authUser, ModelMap model, @PathVariable int id) {
    	
    	paiementRepository.findById(id).ifPresent(paiement -> model.put("paiement", paiement));
		model.put("base", environment.getProperty("app.base"));
    	model.put("authUser", authUser);
		vue.addData("authUser", authUser);
    	return "/paiements/form";
    	
    }
	
}
