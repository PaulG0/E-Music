package sio.javanaise.emusic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import sio.javanaise.emusic.models.ClasseCours;


public interface IClasseCoursRepository extends JpaRepository<ClasseCours, Integer> {
	

}