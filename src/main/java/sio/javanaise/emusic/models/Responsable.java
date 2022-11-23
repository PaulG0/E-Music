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
public class Responsable {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String nom;
	private String prenom;
	private String adresse;
	private String ville;
	private String code_postal;
	private String email;
	private Integer quotient_familial;
	private String tel1;
	private String tel2;
	private String tel3;
	private String token;

	@JsonIgnore
	@OneToMany(mappedBy = "responsable", cascade = CascadeType.ALL)
	private List<Eleve> eleves = new ArrayList<>();

}
