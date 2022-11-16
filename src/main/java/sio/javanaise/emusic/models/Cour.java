package sio.javanaise.emusic.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Cour {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	private String libelle;

	private int ageMin;

	private int ageMAx;

	private int nbPlace;

	@ManyToOne
	private TypeCour typeCour;

	@ManyToOne
	private Prof prof;

	@OneToMany(mappedBy = "cour", cascade = CascadeType.ALL)
	private List<Planning> plannings = new ArrayList<>();

}