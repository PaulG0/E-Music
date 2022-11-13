package sio.Javanaise.emusic.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import sio.Javanaise.emusic.models.Cour;
import sio.Javanaise.emusic.models.Eleve;
import sio.Javanaise.emusic.models.Facture;
import sio.Javanaise.emusic.models.Inscription;
import sio.Javanaise.emusic.repositories.ICoursRepository;
import sio.Javanaise.emusic.repositories.IEleveRepository;
import sio.Javanaise.emusic.repositories.IFactureRepository;
import sio.Javanaise.emusic.repositories.IInscriptionRepository;
import sio.Javanaise.emusic.services.UIPaiementService;

@Controller
@RequestMapping("/factures")
public class FactureController {

	@Autowired(required = true)
    private VueJS vue;

    @ModelAttribute("vue")
    public VueJS getVue() {
        return this.vue;
    }
    
    @Autowired
    private IFactureRepository factureRepository;
    
    @Autowired
    private ICoursRepository coursRepository;
    
    @Autowired
    private IInscriptionRepository inscriptionRepository;
    
    @Autowired
    private IEleveRepository eleveRepository;
    
    @Autowired
    private UIPaiementService paiementService;
    
    @GetMapping("")
    public String indexAction(ModelMap model) {
    	
    	Iterable<Facture> factures = factureRepository.findAllByOrderByDateFacture();
    	model.put("factures", factures);
    	return "/factures/index";
    	
    }
    
    @PostMapping("/datefacture")
    public String indexDateFactureAction(ModelMap model, @ModelAttribute("dateStart") String dateStart, @ModelAttribute("dateEnd") String dateEnd) {
    	
    	LocalDate theDateStart = LocalDate.parse(dateStart, DateTimeFormatter.ofPattern("yyy-MM-dd"));
    	LocalDate theDateEnd = LocalDate.parse(dateEnd, DateTimeFormatter.ofPattern("yyy-MM-dd"));
    	
    	Iterable<Facture> factures = factureRepository.findByDateFactureBetween(theDateStart, theDateEnd);
    	model.put("factures", factures);
    	return "/factures/index";
    	
    }
    
    @PostMapping("/cours")
    public String indexCoursAction(ModelMap model, @ModelAttribute("libelle") String libelle) {
    	
    	Cour cour = coursRepository.findOneByLibelleContainingIgnoreCase(libelle);
    	
    	if(cour == null) {
    		
    		ArrayList<Facture> listFactures = new ArrayList<>();
        	model.put("factures", listFactures);
    		
    	} else {
    		
    		Iterable<Facture> factures = factureRepository.findAllByOrderByDateFacture();
        	ArrayList<Facture> listFactures = new ArrayList<>();
        	for (Facture facture : factures) {
    			
        		String test = facture.getInscription().getPlanning().getCour().getLibelle();
        		
        		if(test == cour.getLibelle()) {
        			listFactures.add(facture);
        		}
        		
    		}
        	
        	model.put("factures", listFactures);	
    		
    	}
    	
    	return "/factures/index";
    	
    }
    
    @GetMapping("/{idEleve}")
    public String listEleveAction(ModelMap model, @PathVariable int idEleve) {
    	
    	Optional<Eleve> eleve = eleveRepository.findById(idEleve);
    	
    	Iterable<Facture> factures = factureRepository.findAllByOrderByDateFacture();
    	ArrayList<Facture> listFactures = new ArrayList<>();
    	for (Facture facture : factures) {
			
    		int test = facture.getInscription().getEleve().getId();
    		
    		if(test == eleve.get().getId()) {
    			listFactures.add(facture);
    		}
    		
		}
    	
    	model.put("factures", listFactures);	
    	
    	return "/factures/listEleve";
    	
    }
    
    @GetMapping("/{idEleve}/{id}")
    public String detailAction(ModelMap model, @PathVariable int idEleve, @PathVariable int id) {
    	
    	factureRepository.findById(id).ifPresent(facture -> model.put("facture", facture));
    	return "/factures/detail";
    	
    }
    
    @GetMapping("/{idEleve}/new/{idinscription}")
    public String newAction(ModelMap model, ModelMap model2, @PathVariable int idEleve, @PathVariable int idinscription) {
    	
    	inscriptionRepository.findById(idinscription).ifPresent(inscription -> model2.put("inscription", inscription));
    	vue.addMethod("foncCalendar", paiementService.calendarUI());
    	model.put("facture", new Facture());
    	return "factures/form";
    	
    }
    
    @PostMapping("/new")
    public String newAction(ModelMap model, @ModelAttribute Facture facture) {
    	
    	
    	
    	factureRepository.save(facture);
    	return "factures/form";
    	
    }
	
}