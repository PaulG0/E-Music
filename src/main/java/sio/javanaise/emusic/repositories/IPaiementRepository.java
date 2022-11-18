package sio.javanaise.emusic.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import sio.javanaise.emusic.models.Paiement;

public interface IPaiementRepository extends JpaRepository<Paiement, Integer> {

	public List<Paiement> findAllByOrderByDateTransmission();
	
	public List<Paiement> findByDateTransmissionBetween(LocalDate start, LocalDate end);
	
}
