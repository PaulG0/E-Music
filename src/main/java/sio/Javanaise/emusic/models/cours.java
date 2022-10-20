package sio.Javanaise.emusic.models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode

public class cours {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private int ageMin;

    private int ageMAx;

    private int nbEleve;

    private List<enfant> enfants = new ArrayList<>();

    private List<instrument> instruments = new ArrayList<>();

}
