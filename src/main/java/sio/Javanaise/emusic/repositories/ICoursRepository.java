package sio.Javanaise.emusic.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import sio.Javanaise.emusic.models.Cour;
import sio.Javanaise.emusic.models.Prof;

public interface ICoursRepository extends CrudRepository<Cour, Integer> {
	public List<Cour> findAllByProf(Prof prof);

}
