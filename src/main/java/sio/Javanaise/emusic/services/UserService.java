package sio.Javanaise.emusic.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import sio.Javanaise.emusic.models.User;
import sio.Javanaise.emusic.repositories.IUserDAO;

public class UserService implements UserDetailsService {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private IUserDAO userRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<User> opt = userRepo.findByLogin(username);
		if (opt.isPresent()) {
			return opt.get();
		}
		throw new UsernameNotFoundException("Utilisateur non trouvé");
	}

	public User createUser(String login, String password) {
		User u = new User();
		u.setLogin(login);
		u.setPassword(passwordEncoder.encode(password)); // (3)
		return u;
	}

}
