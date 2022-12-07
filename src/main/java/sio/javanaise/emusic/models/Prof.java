package sio.javanaise.emusic.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class Prof {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String nom;

	private String prenom;

	private String email;

	private String token;

	@JsonIgnore
	@OneToMany(mappedBy = "prof", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private List<Cour> cours = new ArrayList<>();

}
