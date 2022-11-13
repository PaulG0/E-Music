package sio.Javanaise.emusic.models;

import java.sql.Time;
import java.time.LocalDate;
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
public class Planning {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@DateTimeFormat(pattern = "yyy-MM-dd")
	private LocalDate dateDebut;
	@DateTimeFormat(pattern = "HH:mm")
	private Time heureDebut;

	@DateTimeFormat(pattern = "HH:mm")
	private Time duree;

	@OneToMany(mappedBy = "planning", cascade = CascadeType.ALL)
	private List<Inscription> incriptions;

	@ManyToOne
	private Cour cour;

}