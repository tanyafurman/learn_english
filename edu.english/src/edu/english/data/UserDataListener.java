package edu.english.data;

/**
 * Интерфейс слушателя Юзера
 * 
 * Надо реализовать тот интерфейс классом который хочет получать сообщения о измененияз в ДАТЕ юзера
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
