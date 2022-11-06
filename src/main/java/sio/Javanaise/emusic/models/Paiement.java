package sio.Javanaise.emusic.models;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sio.Javanaise.emusic.enumeration.RoleEnum;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Paiement {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private int prix;
	private LocalDate date_transmission;
	
	@ManyToOne
	private Responsable responsable;
	
}
