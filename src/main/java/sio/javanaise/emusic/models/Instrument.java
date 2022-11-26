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
public class Instrument {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private String intitule;

	@JsonIgnore
	@OneToMany(mappedBy = "instrument", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
	private List<pretInstrument> instruments = new ArrayList<>();

}