package sio.Javanaise.emusic.services;

import org.springframework.stereotype.Service;

@Service
public class UIResponsableService {

	public String modalDelete() {
		return "$('.ui.modal').modal('show');";
	}
	
}
