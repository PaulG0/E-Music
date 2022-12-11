package sio.javanaise.emusic.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Inscription {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@ManyToOne
	private Eleve eleve;




	@ManyToOne
	private ClasseCours ClasseCour;

	@JsonIgnore
	@OneToMany(mappedBy = "inscription", cascade = CascadeType.ALL)
	private List<Facture> factures = new ArrayList<>();

}