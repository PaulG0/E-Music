package sio.javanaise.emusic.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sio.javanaise.emusic.models.Responsable;

public interface IResponsableDAO extends JpaRepository<Responsable, Integer> {

	public Optional<Responsable> findByEmail(String email);

}