package sio.Javanaise.emusic.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Tranche {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	private int quotient_min;
}
