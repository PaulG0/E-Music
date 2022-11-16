package sio.javanaise.emusic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import sio.javanaise.emusic.models.Cour;

public interface ICoursRepository extends JpaRepository<Cour, Integer> {

}
