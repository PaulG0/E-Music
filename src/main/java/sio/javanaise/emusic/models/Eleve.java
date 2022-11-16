package sio.javanaise.emusic.models;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

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
	@DateTimeFormat(pattern = "yyy-MM-dd")
	private LocalDate date_naiss;

	private String token;

	@ManyToOne(optional = true)
	private Responsable responsable;

}
