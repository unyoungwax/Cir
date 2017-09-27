package cir.util;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "title")
@XmlAccessorType(XmlAccessType.FIELD)
public class Title {

	@XmlValue
	private String title;

	@XmlAttribute(name = "confidence")
	private double confidence;

	public String getTitle() {
		return title;
	}
	public double getConfidence() {
		return confidence;
	}
}
