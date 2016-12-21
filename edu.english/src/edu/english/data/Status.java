package edu.english.data;

/**
 * Статус ответа юзера. Коасс создается тестом во время проверки ответов юзера
 * Хранит:
 * 1) тип статуса
 * 2) сообщение 
 * 3) слово с переводом
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
