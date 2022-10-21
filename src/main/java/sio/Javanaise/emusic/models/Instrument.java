package sio.Javanaise.emusic.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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

    @ManyToOne
    private Cour cour;

}
