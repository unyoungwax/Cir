package cir.util;

public class Citation {

	private String source;
	private String target;

	public Citation(String source, String target) {
		this.source = source;
		this.target = target;
	}

	public String getSource() {
		return source;
	}

	public String getTarget() {
		return target;
	}
}
