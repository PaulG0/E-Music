package sio.javanaise.emusic.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import sio.javanaise.emusic.models.Eleve;

public interface IEleveRepository extends JpaRepository<Eleve, Integer> {
	
}
