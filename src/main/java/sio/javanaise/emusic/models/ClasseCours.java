package sio.javanaise.emusic.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity

public class ClasseCours {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;


    @JsonIgnore
    @OneToMany(mappedBy = "Planning", cascade = CascadeType.ALL)
    private List<Planning> Planning = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "Classe", cascade = CascadeType.ALL)
    private List<Inscription> Inscription = new ArrayList<>();

}
