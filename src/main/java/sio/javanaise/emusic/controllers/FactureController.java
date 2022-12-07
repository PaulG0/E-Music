package sio.javanaise.emusic.controllers;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Optional;

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

import io.github.jeemv.springboot.vuejs.VueJS;
import sio.javanaise.emusic.models.Cour;
import sio.javanaise.emusic.models.Eleve;
import sio.javanaise.emusic.models.Facture;
import sio.javanaise.emusic.models.User;
import sio.javanaise.emusic.repositories.ICoursRepository;
import sio.javanaise.emusic.repositories.IEleveRepository;
import sio.javanaise.emusic.repositories.IFactureRepository;
import sio.javanaise.emusic.repositories.IInscriptionRepository;

@Controller
@RequestMapping("/factures")
public class FactureController {


	@Autowired
	Environment environment;
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
    
    @GetMapping("")
    public String indexAction(@AuthenticationPrincipal User authUser, ModelMap model) {
    	
    	Iterable<Facture> factures = factureRepository.findAllByOrderByDateFacture();
    	model.put("factures", factures);
		model.put("base", environment.getProperty("app.base"));
    	model.put("authUser", authUser);
		vue.addData("authUser", authUser);
    	return "/factures/index";
    	
    }
    
    @PostMapping("/datefacture")
    public String indexDateFactureAction(@AuthenticationPrincipal User authUser, ModelMap model, @ModelAttribute("dateStart") String dateStart, @ModelAttribute("dateEnd") String dateEnd) {
    	
    	LocalDate theDateStart = LocalDate.parse(dateStart, DateTimeFormatter.ofPattern("yyy-MM-dd"));
    	LocalDate theDateEnd = LocalDate.parse(dateEnd, DateTimeFormatter.ofPattern("yyy-MM-dd"));
    	
    	Iterable<Facture> factures = factureRepository.findByDateFactureBetween(theDateStart, theDateEnd);
    	model.put("factures", factures);
		model.put("base", environment.getProperty("app.base"));
    	model.put("authUser", authUser);
		vue.addData("authUser", authUser);
    	return "/factures/index";
    	
    }
    
    @PostMapping("/cours")
    public String indexCoursAction(@AuthenticationPrincipal User authUser, ModelMap model, @ModelAttribute("libelle") String libelle) {
    	
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
		model.put("base", environment.getProperty("app.base"));
    	model.put("authUser", authUser);
		vue.addData("authUser", authUser);
    	return "/factures/index";
    	
    }
    
    @GetMapping("/{idEleve}")
    public String listEleveAction(@AuthenticationPrincipal User authUser, ModelMap model, @PathVariable int idEleve) {
    	
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
		model.put("base", environment.getProperty("app.base"));
    	model.put("authUser", authUser);
		vue.addData("authUser", authUser);
    	
    	return "/factures/listEleve";
    	
    }
    
    @GetMapping("/{idEleve}/{id}")
    public String detailAction(@AuthenticationPrincipal User authUser, ModelMap model, @PathVariable int idEleve, @PathVariable int id) {
    	
    	factureRepository.findById(id).ifPresent(facture -> model.put("facture", facture));
		model.put("base", environment.getProperty("app.base"));
    	model.put("authUser", authUser);
		vue.addData("authUser", authUser);
    	return "/factures/detail";
    	
    }
    
    @GetMapping("/{idEleve}/new/{idinscription}")
    public String newAction(@AuthenticationPrincipal User authUser, ModelMap model, ModelMap model2, @PathVariable int idEleve, @PathVariable int idinscription) {
    	
    	inscriptionRepository.findById(idinscription).ifPresent(inscription -> model2.put("inscription", inscription));
    	model.put("facture", new Facture());
		model.put("base", environment.getProperty("app.base"));
    	model.put("authUser", authUser);
		vue.addData("authUser", authUser);
    	return "factures/form";
    	
    }
    
    @PostMapping("/new")
    public String newAction(ModelMap model, @ModelAttribute Facture facture) {
    	
    	
    	
    	factureRepository.save(facture);
    	return "factures/form";
    	
    }
	
}
