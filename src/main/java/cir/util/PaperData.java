package cir.util;
import java.util.ArrayList;
import java.util.List;

public class PaperData {

	private String title;
	private String conference;
	private Integer year;
	private List<String> authors = new ArrayList<>();
	private List<CitedPaperData> citations = new ArrayList<>();

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getConference() {
		return conference;
	}

	public void setConference(String conference) {
		this.conference = conference;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public List<String> getAuthors() {
		return authors;
	}

	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}

	public List<CitedPaperData> getCitations() {
		return citations;
	}

	public void setCitations(List<CitedPaperData> citations) {
		this.citations = citations;
	}
}
