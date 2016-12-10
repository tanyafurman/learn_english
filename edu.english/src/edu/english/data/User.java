package edu.english.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import edu.english.data.Status.StatusType;
import edu.english.data.UserDataListener.Type;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;

	private String surname;

	private String login;

	private String pass;

	private int rate = 5;

	private int wordsAmount = 5;

	private List<Word2Translate> knownWords = new ArrayList<>();

	private HashMap<Word2Translate, Integer> wordToRate = new HashMap<>();

	private transient List<UserDataListener> listeners;

	private transient List<Status> statuses;

	public User(String name, String surname, String login, String pass) {
		super();
		if (login == null || login.trim().length() == 0) {
			// TODO: message
			throw new IllegalArgumentException();
		}
		this.name = name;
		this.surname = surname;
		this.login = login.trim();
		this.pass = pass;
	}

	public String getName() {
		return name;
	}

	public String getSurname() {
		return surname;
	}

	public String getLogin() {
		return login;
	}

	public String getPass() {
		return pass;
	}

	public Collection<Word2Translate> getUnknownWords() {
		if (wordToRate.isEmpty()) {
			return Collections.emptyList();
		}
		return wordToRate.keySet();
	}

	public List<Word2Translate> getKnownWords() {
		return knownWords;
	}

	public List<Status> getStatuses() {
		if (statuses == null) {
			statuses = new ArrayList<>();
		}
		return statuses;
	}

	public void addStatus(List<Status> statuses) {
		for (Status status : statuses) {
			if( status.getType() == StatusType.OK) {
				wordsRateUp(status.getUw());
			}
		}
		getStatuses().addAll(statuses);
		notify(Type.STATUS_CHANGED);
	} 

	public void addWord(Word2Translate word, int rate) {
		wordToRate.put(word, new Integer(rate));
		notify(Type.UNKNOWN_WORDS_CHANGED);
		if (knownWords.remove(word)) {
			notify(Type.KNOWN_WORDS_CHANGED);
		}
	}

	public int getRate() {
		return rate;
	}

	protected void wordsRateUp(Word2Translate word) {
		Integer rate = wordToRate.get(word);
		if (rate == null) {
			return;
		}
		rate = rate + 1;
		if (rate == this.rate) {
			knownWords.add(word);
			wordToRate.remove(word);
			notify(Type.UNKNOWN_WORDS_CHANGED);
			notify(Type.KNOWN_WORDS_CHANGED);
		} else {
			wordToRate.put(word, new Integer(rate));
		}
	}

	public boolean containsUnknowns(Word2Translate word) {
		return wordToRate.containsKey(word);
	}

	public boolean containsKnowns(Word2Translate word) {
		return knownWords.contains(word);
	}

	
	public int getWordsAmount() {
		return wordsAmount;
	}

	public void setWordsAmount(int wordsAmount) {
		this.wordsAmount = wordsAmount;
		notify(Type.UNKNOWN_WORDS_CHANGED);
	}

	public void setWordsRate(int rate) {
		this.rate = rate;
		for (Word2Translate word: getUnknownWords()) {
			if (wordToRate.get(word).intValue() >= rate) {
				wordsRateUp(word);
			}
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (false == obj instanceof User) {
			return false;
		}
		return this.login.equals(((User) obj).login);
	}

	public void addListener(UserDataListener listener) {
		if (listeners == null) {
			listeners = new ArrayList<>();
		}
		listeners.add(listener);
		// init notification
		notify(Type.KNOWN_WORDS_CHANGED);
		notify(Type.UNKNOWN_WORDS_CHANGED);
		notify(Type.STATUS_CHANGED);
	}

	public void removeListener(UserDataListener listener) {
		listeners.remove(listener);
	}

	public void notify(UserDataListener.Type type) {
		Collection<?> element = null;
		switch (type) {
		case KNOWN_WORDS_CHANGED:
			element = getKnownWords();
			break;
		case UNKNOWN_WORDS_CHANGED:
			element = getUnknownWords();
			break;
		case STATUS_CHANGED:
			element = getStatuses();
			break;
		}
		for (UserDataListener l: listeners) {
			l.notify(type, element);
		}
	}
}
