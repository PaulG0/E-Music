package sio.javanaise.emusic.repositories;

import org.springframework.data.repository.CrudRepository;

import sio.javanaise.emusic.models.Instrument;

public interface IInstrumentRepository extends CrudRepository<Instrument, Integer> {

}
