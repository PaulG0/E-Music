package sio.javanaise.emusic.models;

import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnore;

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


	@DateTimeFormat(pattern = "yyy-MM-dd")
	private LocalDate dateFin;

	@DateTimeFormat(pattern = "HH:mm")
	private Time heureDebut;



	private String jourSemaine;


	private String Status;

	@DateTimeFormat(pattern = "HH:mm")
	private Time heureFin;

	@ManyToOne
	private ClasseCours ClasseCours;

	@ManyToOne
	private Cour cour;
}