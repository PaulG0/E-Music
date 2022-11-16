package sio.javanaise.emusic.ui;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UILink {
	private String libelle;
	private String href;

	public UILink(String href) {
		this(href, href);
	}

	public UILink(String libelle, String href) {
		this.libelle = libelle;
		this.href = href;
	}

}