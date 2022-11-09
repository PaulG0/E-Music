package sio.Javanaise.emusic.services;

import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

@Service
public class ResponsableService {

	public boolean EmailEstValide(String email) {
		String regexPattern = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
				+ "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
		return Pattern.compile(regexPattern).matcher(email).matches();
	}

	public boolean NomEstValide(String nom) {
		String regexPattern = "^[A-Z][A-Za-z\\é\\è\\ê\\-]+$";
		return Pattern.compile(regexPattern).matcher(nom).matches();
	}

	public boolean CodePostalEstValide(int code) {
		String codeString = "" + code;
		return codeString.length() == 5;
	}

}
