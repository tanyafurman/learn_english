package edu.furman.english.data;

public interface UserDataListener {

	public void notify(Type type, Object data);

	public static enum Type {
		KNOWN_WORDS_CHANGED,
		UNKNOWN_WORDS_CHANGED,
		STATUS_CHANGED
	}
}
