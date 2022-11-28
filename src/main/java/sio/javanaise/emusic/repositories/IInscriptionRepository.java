package sio.javanaise.emusic.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import sio.javanaise.emusic.models.Inscription;
import sio.javanaise.emusic.models.Planning;

public interface IInscriptionRepository extends CrudRepository<Inscription, Integer> {

	public List<Inscription> findAllByPlanning(Planning planning);

}
