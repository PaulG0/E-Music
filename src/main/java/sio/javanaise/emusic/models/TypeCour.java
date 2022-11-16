package sio.javanaise.emusic.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import sio.javanaise.emusic.enumeration.TypeCourEnum;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class TypeCour {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private TypeCourEnum libelle;

}
