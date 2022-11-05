package sio.Javanaise.emusic.services;

import org.springframework.stereotype.Service;

@Service
public class UIResponsableService {

	public String modalDelete() {
		return "$('.ui.modal.delet').modal('show');";
	}
	
	public String modalSuspendre() {
		return "$('.ui.modal.suspendre').modal('show');";
	}
	
}
