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
import sio.javanaise.emusic.enumeration.RoleEnum;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Paiement {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	private int prix;
	
	@DateTimeFormat(pattern = "yyy-MM-dd")
	private LocalDate dateTransmission;
	
	@ManyToOne
	private Facture facture;
	
}
