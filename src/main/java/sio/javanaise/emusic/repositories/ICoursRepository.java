package sio.javanaise.emusic.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import sio.javanaise.emusic.models.Cour;
import sio.javanaise.emusic.models.Prof;

public interface ICoursRepository extends CrudRepository<Cour, Integer> {
	public List<Cour> findAllByProf(Prof prof);

}
