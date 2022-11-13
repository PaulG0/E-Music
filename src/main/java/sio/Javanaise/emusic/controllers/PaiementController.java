package sio.Javanaise.emusic.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

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
import sio.Javanaise.emusic.models.Paiement;
import sio.Javanaise.emusic.repositories.ICoursRepository;
import sio.Javanaise.emusic.repositories.IEleveRepository;
import sio.Javanaise.emusic.repositories.IPaiementRepository;
import sio.Javanaise.emusic.repositories.IResponsableRepository;
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
    private ICoursRepository coursRepository;
    
    @Autowired
    private UIPaiementService paiementService;
    
    @GetMapping("")
    public String indexAction(ModelMap model) {
    	
    	Iterable<Paiement> paiements = paiementRepository.findAllByOrderByDateTransmission();
    	model.put("paiements", paiements);
    	return "/paiements/index";
    	
    }
    
    @PostMapping("/datepaiement")
    public String indexDatePaiementAction(ModelMap model, @ModelAttribute("dateStart") String dateStart, @ModelAttribute("dateEnd") String dateEnd) {
    	
    	LocalDate theDateStart = LocalDate.parse(dateStart, DateTimeFormatter.ofPattern("yyy-MM-dd"));
    	LocalDate theDateEnd = LocalDate.parse(dateEnd, DateTimeFormatter.ofPattern("yyy-MM-dd"));
    	
    	Iterable<Paiement> paiements = paiementRepository.findByDateTransmissionBetween(theDateStart, theDateEnd);
    	model.put("paiements", paiements);
    	return "/paiements/index";
    	
    }
    
    @PostMapping("/cours")
    public String indexCoursAction(ModelMap model, @ModelAttribute("libelle") String libelle) {
    	
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
    	
    	return "/paiements/index";
    	
    }
    
    @GetMapping("/new/{idFacture}")
    public String newAction(ModelMap model, @PathVariable int idFacture) {
    	
    	vue.addData("facture", idFacture);
    	vue.addMethod("foncCalendar", paiementService.calendarUI());
    	
    	model.put("paiement", new Paiement());
    	return "/paiements/form";
    	
    }
    
    @PostMapping("/new")
    public RedirectView newAction(@ModelAttribute Paiement paiement, @ModelAttribute("laDateTransmission") String laDateTransmission) {
    	
    	LocalDate dateTransmission = LocalDate.parse(laDateTransmission, DateTimeFormatter.ofPattern("yyy-MM-dd"));
    	paiement.setDateTransmission(dateTransmission);
    	
    	paiementRepository.save(paiement);
    	return new RedirectView("/responsables");
    	
    }
    
    @GetMapping("/edit/{id}")
    public String editAction(ModelMap model, @PathVariable int id) {
    	
    	paiementRepository.findById(id).ifPresent(paiement -> model.put("paiement", paiement));
    	return "/paiements/form";
    	
    }
	
}
