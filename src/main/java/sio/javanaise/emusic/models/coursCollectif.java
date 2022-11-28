package sio.javanaise.emusic.models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class coursCollectif {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int nbPlace;

}
