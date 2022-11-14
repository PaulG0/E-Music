package sio.Javanaise.emusic.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import sio.Javanaise.emusic.models.Eleve;

public interface IEleveDAO extends JpaRepository<Eleve, Integer> {

}
