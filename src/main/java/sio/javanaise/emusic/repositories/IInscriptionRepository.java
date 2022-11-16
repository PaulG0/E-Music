package sio.javanaise.emusic.repositories;

import org.springframework.data.repository.CrudRepository;

import sio.javanaise.emusic.models.Inscription;

public interface IInscriptionRepository extends CrudRepository<Inscription, Integer> {

}
