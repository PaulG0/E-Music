package sio.javanaise.emusic.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sio.javanaise.emusic.models.Prof;

public interface IProfRepository extends JpaRepository<Prof, Integer> {

	public Optional<Prof> findByEmail(String email);
	
}
