package sio.Javanaise.emusic.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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

	private LocalDateTime date_naiss;

	@OneToMany(mappedBy = "eleve", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private List<Inscription> inscrits = new ArrayList<>();

	@ManyToOne(optional = true)
	private Responsable responsable;

}
