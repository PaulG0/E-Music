package sio.Javanaise.emusic.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import sio.Javanaise.emusic.models.Cour;

public interface ICoursRepository extends JpaRepository<Cour, Integer> {

	public List<Cour> findByLibelleContainingIgnoreCase(String libelle);
	
	public Cour findOneByLibelleContainingIgnoreCase(String libelle);
	
}
