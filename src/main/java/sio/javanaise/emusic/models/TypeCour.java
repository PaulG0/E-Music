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
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import sio.javanaise.emusic.enumeration.TypeCourEnum;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
@NoArgsConstructor
public class TypeCour {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@NonNull
	private TypeCourEnum libelle;

	@JsonIgnore
	@OneToMany(mappedBy = "typeCour", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private List<Cour> cour = new ArrayList<>();

}