package sio.javanaise.emusic.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sio.javanaise.emusic.repositories.IProfRepository;

@Service
public class ProfService {

	@Autowired
	private IProfRepository profRepo;

}
