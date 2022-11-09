package sio.Javanaise.emusic.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import sio.Javanaise.emusic.models.User;

@Repository
public interface IUserDAO extends JpaRepository<User, Integer> {
	public Optional<User> findByLogin(String login);
}
