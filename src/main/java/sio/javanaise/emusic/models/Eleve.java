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
public class Eleve {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String nom;
	private String prenom;
	private String sexe;

	@DateTimeFormat(pattern = "yyy-MM-dd")
	private LocalDate dateNaiss;


	private String token;

	@JsonIgnore
	@OneToMany(mappedBy = "eleve", cascade = CascadeType.ALL)

	private List<Inscription> inscrits = new ArrayList<>();

	@ManyToOne(optional = true)
	private Responsable responsable;

}
