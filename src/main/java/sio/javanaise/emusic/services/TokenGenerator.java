package sio.javanaise.emusic.services;

import java.security.SecureRandom;

import org.springframework.stereotype.Service;

@Service
public class TokenGenerator {

	protected static SecureRandom random = new SecureRandom();

	public synchronized String generateToken(String username) {
		long longToken = Math.abs(random.nextLong());
		String random = Long.toString(longToken, 16);
		return (username + ":" + random);
	}

	public synchronized String generateToken() {
		long longToken = Math.abs(random.nextLong());
		String random = Long.toString(longToken, 16);
		return (random);
	}

}
