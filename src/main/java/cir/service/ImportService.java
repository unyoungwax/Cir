package cir.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cir.dao.AuthorDao;
import cir.dao.PaperDao;
import cir.entity.Author;
import cir.entity.Paper;
import cir.util.Authorship;
import cir.util.Citation;
import cir.util.CitedPaperData;
import cir.util.PaperData;
import cir.util.XmlDataImporter;

@Service
public class ImportService {

	private static final String PATH_DATA = "";

	@Autowired
	private AuthorDao authorDao;

	@Autowired
	private PaperDao paperDao;

	@PostConstruct
	public void init() {
		importData();
	}

	@Transactional
	private void importData() {

		List<PaperData> dataList = importXml();

	    Map<String, Author> authors = new HashMap<>();
		Map<String, Paper> papers = new HashMap<>();
		Set<Citation> citations = new HashSet<>();
		Set<Authorship> authorships = new HashSet<>();

		System.out.println("Processing extracted data");

		for (PaperData data : dataList) {

			String basePaperTitle = data.getTitle();

			Paper paper = new Paper(basePaperTitle);

			paper.setConference(data.getConference());
			paper.setYear(data.getYear());

			if (papers.containsKey(basePaperTitle)) {
				papers.get(basePaperTitle).update(paper);
			} else {
				papers.put(basePaperTitle, paper);
			}

			for (String authorName : data.getAuthors()) {
				String name = authorName.toUpperCase();
				authors.put(name, new Author(name));
				authorships.add(new Authorship(name, basePaperTitle));
			}

			for (CitedPaperData citedPaper : data.getCitations()) {

				String citedPaperTitle = citedPaper.getTitle();

				Paper referencePaper = new Paper(citedPaperTitle);

				if (papers.containsKey(citedPaperTitle)) {
					papers.get(citedPaperTitle).update(referencePaper);
				} else {
					papers.put(citedPaperTitle, referencePaper);
				}

				for (String authorName : citedPaper.getAuthors()) {
					String name = authorName.toUpperCase();
					authors.put(name, new Author(name));
					authorships.add(new Authorship(name, citedPaperTitle));
				}

				citations.add(new Citation(basePaperTitle, citedPaperTitle));
			}
		}

		System.out.println(String.format("Saving entities: %d authors, %d papers", authors.size(), papers.size()));

		Iterable<Author> savedAuthors = authorDao.save(authors.values());
		Iterable<Paper> savedPapers = paperDao.save(papers.values());

		System.out.println("Linking entities");

		authors = new HashMap<>(authors.size());
		papers = new HashMap<>(papers.size());

		for (Author author : savedAuthors) {
			authors.put(author.getName(), author);
		}

		for (Paper paper : savedPapers) {
			papers.put(paper.getTitle(), paper);
		}

		for (Authorship authorship : authorships) {

			Author author = authors.get(authorship.getAuthor());
			Paper paper = papers.get(authorship.getPaper());

			paper.addAuthor(author);
		}

		for (Citation citation : citations) {

			Paper source = papers.get(citation.getSource());
			Paper target = papers.get(citation.getTarget());

			source.addOutCitation(target);
			target.addInCitation(source);
		}

		System.out.println(String.format("Saving linked entities: %d citations", citations.size()));

		authorDao.save(authors.values());
		paperDao.save(papers.values());
	}

	private List<PaperData> importXml() {

		File directory = new File(PATH_DATA);

		Collection<File> files = FileUtils.listFiles(directory, new String[] { "xml" }, true);

		XmlDataImporter importer = new XmlDataImporter();

		List<PaperData> dataList = new ArrayList<>(files.size());

		for (File file : files) {

			System.out.println("Processing: " + file.getName());

			try {
				dataList.add(importer.extract(file));
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}
		}

		return dataList;
	}
}
