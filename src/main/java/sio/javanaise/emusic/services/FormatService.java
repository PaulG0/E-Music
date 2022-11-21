package sio.javanaise.emusic.services;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import org.springframework.stereotype.Service;

@Service
public class FormatService {

	private DateFormat formatter = new SimpleDateFormat("HH:mm");
	private LocalDate formDate;


	public LocalDate formatdate(String date) {
		String[] oldDate = date.split(" ");
		String Mois;
		String jour = oldDate[2];
		String anne = oldDate[3];
		if (oldDate[1].equals("Jan")) {
			Mois = "01";
		} else if (oldDate[1].equals("Feb")) {
			Mois = "02";
		} else if (oldDate[1].equals("Mar")) {
			Mois = "03";
		} else if (oldDate[1].equals("Apr")) {
			Mois = "04";
		} else if (oldDate[1].equals("May")) {
			Mois = "05";
		} else if (oldDate[1].equals("Jun")) {
			Mois = "06";
		} else if (oldDate[1].equals("Jul")) {
			Mois = "07";
		} else if (oldDate[1].equals("Aug")) {
			Mois = "08";
		} else if (oldDate[1].equals("Sep")) {
			Mois = "09";
		} else if (oldDate[1].equals("Oct")) {
			Mois = "10";
		} else if (oldDate[1].equals("Nov")) {
			Mois = "11";
		} else {
			Mois = "12";
		}
		String newformat = anne + "-" + Mois + "-" + jour;
		LocalDate newDate = formDate.parse(newformat);

		return newDate;
	}


}
