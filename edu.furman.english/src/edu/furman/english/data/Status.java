package edu.furman.english.data;

public class Status {

	private final StatusType type;

	private final String message;

	private final Word uw;

	public Status(String message, Word uw, StatusType type) {
		this.message = message;
		this.type = type;
		this.uw = uw;
	}

	public String getMessage() {
		return message;
	}

	public StatusType getType() {
		return type;
	}

	public Word getUw() {
		return uw;
	}

	public static enum StatusType {
		ERROR, OK
	}
}
