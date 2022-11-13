package sio.Javanaise.emusic.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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

	private String login;
	private String nom;
	private String prenom;
	private String adresse;
	private String ville;
	private int code_postal;
	private String email;
	private String password;
	private int quotient_familial;
	private String tel1;
	private String tel2;
	private String tel3;

	@OneToMany(mappedBy = "responsable", cascade = CascadeType.ALL)
	private List<Eleve> eleves = new ArrayList<>();

}
