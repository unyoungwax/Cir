package cir.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

@Entity
public class Paper {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(length = 2047)
	private String title;

	private String conference;
	private Integer year;

	@ManyToMany(fetch = FetchType.EAGER)
	@Column(nullable = false)
	private Set<Author> authors = new HashSet<>();

	@ManyToMany(fetch = FetchType.EAGER)
	@Column(nullable = false)
	private Set<Paper> inCitations = new HashSet<>();

	@ManyToMany(fetch = FetchType.EAGER)
	@Column(nullable = false)
	private Set<Paper> outCitations = new HashSet<>();

	public Paper() {
	}

	public Paper(String title) {
		this.title = title;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public void setYear(int year) {
		this.year = year;
	}
	
	public Set<Author> getAuthors() {
		return authors;
	}

	public void addAuthor(Author author) {
		authors.add(author);
	}
	
	public void addInCitation(Paper paper) {
		inCitations.add(paper);
	}

	public void addOutCitation(Paper paper) {
		outCitations.add(paper);
	}
	
	public void update(Paper paper) {
		
		if (paper.getConference() != null) {
			conference = paper.getConference();
		}
		
		if (paper.getYear() != null) {
			year = paper.getYear();
		}
	}
}
