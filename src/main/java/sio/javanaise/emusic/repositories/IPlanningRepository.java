package sio.javanaise.emusic.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sio.javanaise.emusic.models.Planning;

@Repository
public interface IPlanningRepository extends JpaRepository<Planning, Integer> {

	public List<Planning> findAllByOrderByDateDebut();

}
