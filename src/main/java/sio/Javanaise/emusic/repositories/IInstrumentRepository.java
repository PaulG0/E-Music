package sio.Javanaise.emusic.repositories;

import org.springframework.data.repository.CrudRepository;

import sio.Javanaise.emusic.models.Instrument;

public interface IInstrumentRepository extends CrudRepository<Instrument, Integer> {

}
