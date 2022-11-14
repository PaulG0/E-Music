package sio.Javanaise.emusic.models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.ManyToOne;

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

	private String login;
	private String password;
	private String nom;
	private String prenom;

	@DateTimeFormat(pattern = "yyy-MM-dd")
	private LocalDate date_naiss;

	@OneToMany(mappedBy = "eleve", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private List<Inscription> inscrits = new ArrayList<>();

	@ManyToOne(optional = true)
	private Responsable responsable;

}
