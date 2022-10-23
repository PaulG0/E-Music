package sio.Javanaise.emusic.models;

import java.util.ArrayList;
import java.util.List;

<<<<<<< Updated upstream
import javax.persistence.CascadeType;
=======
>>>>>>> Stashed changes
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import sio.Javanaise.emusic.enumeration.TypeCourEnum;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class TypeCour {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

<<<<<<< Updated upstream
    @NonNull
    private TypeCourEnum libelle;

    @OneToMany(mappedBy = "typeCour", cascade = { CascadeType.MERGE, CascadeType.PERSIST })
    private List<Cour> cour = new ArrayList<>();

=======
	private TypeCourEnum libelle;

	@OneToMany(mappedBy = "typecour")
	private List<Cour> cour = new ArrayList<>();
>>>>>>> Stashed changes
}
