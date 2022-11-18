package sio.javanaise.emusic.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class UIMessage {

	private String type;
	private String title;
	private String icon;
	private String content;

	private List<UILink> links;

	public UIMessage() {
		this.title = "";
		this.type = "";
		this.icon = "";
		this.content = "";
		this.links = new ArrayList<>();
	}

	public UIMessage addLinks(UILink... links) {
		this.links.addAll(Arrays.asList(links));
		return this;
	}

	public UIMessage(String type, String title, String icon, String content) {
		this.title = title;
		this.type = type;
		this.icon = icon;
		this.content = content;
		this.links = new ArrayList<>();
	}

	public static UIMessage error(String title, String content) {
		return new UIMessage("error", title, "error", content);
	}
}
