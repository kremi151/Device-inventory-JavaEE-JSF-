package lu.mkremer.javaeega.intervention;

import java.io.Serializable;

public enum ReportStatus implements Serializable{
	OPEN("Open", "warning"),
	PROCESSING("In process", "info"),
	SOLVED("Solved", "success"),
	UNSOLVABLE("Unsolvable", "danger"),
	PAUSED("Paused for the moment", "default");
	
	private final String description, bsSeverity;
	
	private ReportStatus(String description, String severity) {
		this.description = description;
		this.bsSeverity = severity;
	}

	public String getDescription() {
		return description;
	}

	public String getBsSeverity() {
		return bsSeverity;
	}
}
