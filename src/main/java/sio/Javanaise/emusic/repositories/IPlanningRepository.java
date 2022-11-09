package sio.Javanaise.emusic.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import sio.Javanaise.emusic.models.Cour;
import sio.Javanaise.emusic.models.Planning;
import sio.Javanaise.emusic.models.Prof;

@Repository
public interface IPlanningRepository extends CrudRepository<Planning, Integer> {

	public Iterable<Planning> findAllByCour(Cour cour);

	public Iterable<Planning> findallByProf(Prof prof);
	Fin

}
