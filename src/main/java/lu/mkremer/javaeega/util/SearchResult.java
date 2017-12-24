package lu.mkremer.javaeega.util;

public class SearchResult {
	
	private final String title;
	private final String description;
	private final String outcome;
	private final OutcomeId outcomeId;

	public SearchResult(String title, String description, String outcome, OutcomeId outcomeId) {
		this.title = title;
		this.description = description;
		this.outcome = outcome;
		this.outcomeId = outcomeId;
	}
	
	public SearchResult(String title, String description, String outcome) {
		this(title, description, outcome, null);
	}

	public String getTitle() {
		return title;
	}

	public String getDescription() {
		return description;
	}

	public String getOutcome() {
		return outcome;
	}

	public OutcomeId getOutcomeId() {
		return outcomeId;
	}
	
	public boolean hasOutcomeId() {
		return outcomeId != null;
	}
	
	public static class OutcomeId{
		
		private final String name, value;
		
		public OutcomeId(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public String getValue() {
			return value;
		}
	}
}
