package sio.javanaise.emusic.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import sio.javanaise.emusic.models.Cour;

public interface ICoursRepository extends JpaRepository<Cour, Integer> {
	
	public List<Cour> findByLibelleContainingIgnoreCase(String libelle);
	
	public Cour findOneByLibelleContainingIgnoreCase(String libelle);
}
