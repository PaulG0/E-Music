package sio.Javanaise.emusic.services;

import org.springframework.stereotype.Service;

@Service
public class UIPaiementService {

	public String calendarUI() {
		return "$('#standard_calendar').calendar({"
				+ "type:'date',"
				+ "formatter: {"
					+ "date:'YYYY-MM-DD'"
				+ "}"
				+ "});";
	}
	
}
