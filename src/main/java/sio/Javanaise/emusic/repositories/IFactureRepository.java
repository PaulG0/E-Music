package sio.Javanaise.emusic.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import sio.Javanaise.emusic.models.Facture;

public interface IFactureRepository extends JpaRepository<Facture, Integer> {

	public List<Facture> findAllByOrderByDateFacture();
	
	public List<Facture> findByDateFactureBetween(LocalDate start, LocalDate end);
	
}
