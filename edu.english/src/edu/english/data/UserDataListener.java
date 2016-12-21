package edu.english.data;

/**
 * Интерфейс UserDataListener слушателя пользователя.<br>
 * <br>
 * Данный часть кода реализовывает тот интерфейс, класс которого получает сообщения о изменениях данных пользователя.<br>
 * <br>
 * 
 * Типы сообщений об изменениях данных от пользователя.
	
	public static enum Type {
		KNOWN_WORDS_CHANGED,
		UNKNOWN_WORDS_CHANGED,
		STATUS_CHANGED
	}
 */
public interface UserDataListener {

	public void notify(Type type, Object data);

	/**
	 * Типы сообщений от юзера
	 */
	public static enum Type {
		KNOWN_WORDS_CHANGED,
		UNKNOWN_WORDS_CHANGED,
		STATUS_CHANGED
	}
}
