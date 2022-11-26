package sio.javanaise.emusic.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Facture {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private int prix;
	
	@DateTimeFormat(pattern = "yyy-MM-dd")
	private LocalDate dateFacture;
	
	@ManyToOne
	private Inscription inscription;
	
	@JsonIgnore
	@OneToMany(mappedBy = "facture", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private List<Paiement> paiements = new ArrayList<>();
	
}
