package sio.Javanaise.emusic.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode

@Entity
public class Cours {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String libelle;

    private int ageMin;

    private int ageMAx;

    private int nbPlace;

    private String type;

//    private List<enfant> enfants = new ArrayList<>();

    @OneToMany(mappedBy = "cours", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private List<Instrument> instruments = new ArrayList<>();

}
