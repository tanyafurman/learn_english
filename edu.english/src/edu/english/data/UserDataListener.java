package edu.english.data;

/**
 * ��������� UserDataListener ��������� ������������.<br>
 * <br>
 * ������ ����� ���� ������������� ��� ���������, ����� �������� �������� ��������� � ���������� ������ ������������.<br>
 * <br>
 * 
 * ���� ��������� �� ���������� ������ �� ������������.
	
	public static enum Type {
		KNOWN_WORDS_CHANGED,
		UNKNOWN_WORDS_CHANGED,
		STATUS_CHANGED
	}
 */
public interface UserDataListener {

	public void notify(Type type, Object data);

	/**
	 * ���� ��������� �� �����
	 */
	public static enum Type {
		KNOWN_WORDS_CHANGED,
		UNKNOWN_WORDS_CHANGED,
		STATUS_CHANGED
	}
}
