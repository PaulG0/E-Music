package sio.Javanaise.emusic.models;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class CoursIndividuel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

}
