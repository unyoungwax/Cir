package cir.util;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "citation")
@XmlAccessorType(XmlAccessType.FIELD)
public class CitedPaperData {

	@XmlElementWrapper(name = "authors")
	@XmlElement(name = "author")
	private List<String> authors = new ArrayList<>();

	@XmlElement(name = "title")
	private String title;

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
}
