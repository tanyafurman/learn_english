package edu.english.data;

/**
 * ����� Status ��������� ������ �� ����� �������� ������� �������������.<br>
 * �������� ������� ������ �������� ���������� ������� ������������.<br>
 * <br>
 * <br>
 * ������ ����� ������ ����� ������, ���:<br>
 * 1) ��� �������.<br>
 * 2) ���������.<br> 
 * 3) ����� � ��� �������.<br>
 */
public class Status {

	private final StatusType type;

	private final String message;

	private final Word2Translate uw;

	public Status(String message, Word2Translate uw, StatusType type) {
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

	public Word2Translate getUw() {
		return uw;
	}

	public static enum StatusType {
		ERROR, OK
	}
}
