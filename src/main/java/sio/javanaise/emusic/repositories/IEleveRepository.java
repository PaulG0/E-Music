package sio.javanaise.emusic.repositories;


import org.springframework.data.jpa.repository.JpaRepository;

import sio.javanaise.emusic.models.Eleve;

import java.util.Optional;

public interface IEleveRepository extends JpaRepository<Eleve, Integer> {

    public Optional<Eleve> findByToken(String token);

}
