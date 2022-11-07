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

}
