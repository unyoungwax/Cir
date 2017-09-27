package cir.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XmlDataImporter {

	private static XPathExpression xPathAuthors;
	private static XPathExpression xPathCitation;
	private static XPathExpression xPathTitle;

	static {

		XPath xPath = XPathFactory.newInstance().newXPath();

		try {

			xPathAuthors = xPath.compile("//variant/author");
			xPathCitation = xPath.compile("/algorithms/algorithm[@name='ParsCit']/citationList/citation");
			xPathTitle = xPath.compile("//variant/title");

		} catch (XPathExpressionException e) {
			e.printStackTrace();
		}
	}

	public PaperData extract(File file) throws SAXException, IOException, XPathExpressionException, NoSuchElementException, JAXBException  {

		DocumentBuilder builder;
		
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}

		Document document = builder.parse(file);

		PaperData data = new PaperData();

		data.setConference(extractConference(file));
		data.setYear(extractYear(file));
		data.setTitle(extractTitle(document));
		data.setAuthors(extractAuthors(document));
		data.setCitations(extractCitations(document));

		return data;
	}

	private String extractConference(File file) {
		return file.getName().substring(0, 1);
	}

	private int extractYear(File file) {
		return Integer.parseInt("20" + file.getName().substring(1, 3));
	}

	private String extractTitle(Document document)
			throws JAXBException, XPathExpressionException, NoSuchElementException {

		NodeList titleNodes = (NodeList) xPathTitle.evaluate(document, XPathConstants.NODESET);
		Unmarshaller unmarshaller = JAXBContext.newInstance(Title.class).createUnmarshaller();

		List<Title> candidateTitles = new ArrayList<>(titleNodes.getLength());

		for (int i = 0; i < titleNodes.getLength(); i++) {

			Node titleNode = titleNodes.item(i);

			Title title = (Title) unmarshaller.unmarshal(titleNode);

			candidateTitles.add(title);
		}

		Optional<Title> optional = candidateTitles.stream()
				.reduce((title1, title2) -> title1.getConfidence() < title2.getConfidence() ? title2 : title1);

		return optional.get().getTitle();
	}

	private List<String> extractAuthors(Document document) throws XPathExpressionException {

		NodeList authorNodes = (NodeList) xPathAuthors.evaluate(document, XPathConstants.NODESET);
		List<String> authors = new ArrayList<>();

		for (int i = 0; i < authorNodes.getLength(); i++) {

			Node authorNode = authorNodes.item(i);

			authors.add(authorNode.getTextContent());
		}

		return authors;
	}

	private List<CitedPaperData> extractCitations(Document document) throws XPathExpressionException, JAXBException {

		NodeList citationNodes = (NodeList) xPathCitation.evaluate(document, XPathConstants.NODESET);
		Unmarshaller unmarshaller = JAXBContext.newInstance(CitedPaperData.class).createUnmarshaller();
		List<CitedPaperData> citations = new ArrayList<>(citationNodes.getLength());

		for (int i = 0; i < citationNodes.getLength(); i++) {

			Node citationsNode = citationNodes.item(i);

			CitedPaperData citation = (CitedPaperData) unmarshaller.unmarshal(citationsNode);

			// Skip citation with no title
			if (!StringUtils.isEmpty(citation.getTitle())) {
				citations.add(citation);
			}
		}

		return citations;
	}
}
