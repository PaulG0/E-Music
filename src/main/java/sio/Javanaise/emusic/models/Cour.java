package sio.Javanaise.emusic.models;

import java.sql.Time;
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

	@DateTimeFormat(pattern = "yyy-MM-dd")
	private LocalDate dateDebut;

	@DateTimeFormat(pattern = "HH:mm")
	private Time heureDebut;

	@OneToMany(mappedBy = "cour", cascade = CascadeType.ALL)
	private List<Inscription> incriptions;

	@OneToMany(mappedBy = "cour", cascade = CascadeType.ALL)
	private List<pretInstrument> Instruments = new ArrayList<>();

	@ManyToOne
	private TypeCour typeCour;

	@ManyToOne
	private Prof prof;

	public String dateJour(LocalDate d) {
d.getDayOfWeek()
		return "";
	}

}
