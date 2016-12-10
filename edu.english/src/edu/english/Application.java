package edu.english;

import java.util.Collections;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import edu.english.data.Status;
import edu.english.data.User;
import edu.english.data.UserDataListener;
import edu.english.data.UserDataListener.Type;
import edu.english.data.Vocabulary;
import edu.english.data.Word2Translate;
import edu.english.model.AbstractWordsModel;
import edu.english.model.StatusModel;
import edu.english.tests.Test;

public class Application {

	public final static String KNOWN_WORDS_MODEL_ID = "KNOWN_WORDS_MODEL_ID";

	public final static String UNKNOWN_WORDS_MODEL_ID = "UNKNOWN_WORDS_MODEL_ID";

	public final static String VOCABULARY_WORDS_MODEL_ID = "VOCABULARY_WORDS_MODEL_ID";

	public final static String STATUS_MODEL_ID = "STATUS_MODEL_ID";

	private Vocabulary vocabulary;

	private User user;

	private UserWordService userWordService;

	private Test currentTest;

	public Application(User u, Vocabulary v) {
		this.user = u;
		this.vocabulary = v;
		this.userWordService = new UserWordService(u, v);
		this.user.addListener(userWordService);
		new Thread(userWordService).start();
	}

	public List<Status> processTestResults(List<Word2Translate> words) {
		List<Status> result = currentTest.checkAnswers(words);
		user.addStatus(result);
		return result;
	}

	public int getUnknownWordsSize() {
		return user.getWordsAmount();
	}

	public void setUnknownWordsAmount(int newSize) {
		if (newSize < 1) throw new IllegalArgumentException("New size must be > 0. new size:" + newSize);
		user.setWordsAmount(newSize);
	}

	public int getRepeatCount() {
		return user.getRate();
	}

	public void setRepeatCount(int repeatCount) {
		if (repeatCount < 1) throw new IllegalArgumentException("Repeat count must be > 0. Repeat count:" + repeatCount);
		user.setWordsRate(repeatCount);
	}

	public void addUnknownWord(Word2Translate word) {
		user.addWord(word, 0);
	}

	public int getTestAmount() {
		return 3;
	}

	public Test getNextTest(int words, int answers) {
		return currentTest = new Test(words, answers, user);
	}

	public void dispose() {
		if (user != null) {
			UserManager.getInstance().saveUser(user);
			user = null;
		}
		vocabulary = null;
	}

	public DefaultTableModel getAdapter(String id) {
		DefaultTableModel result = null;
		switch (id) {
		case KNOWN_WORDS_MODEL_ID:
			result = new AbstractWordsModel(Collections.emptyList(), Type.KNOWN_WORDS_CHANGED);
			break;
		case UNKNOWN_WORDS_MODEL_ID:
			result = new AbstractWordsModel(Collections.emptyList(), Type.UNKNOWN_WORDS_CHANGED);
			break;
		case VOCABULARY_WORDS_MODEL_ID: 
			result = new AbstractWordsModel(vocabulary.getWords(), null);
			break;
		case STATUS_MODEL_ID:
			result = new StatusModel();
		}
		addUserListener((UserDataListener)result);
		return result;
	}

	public void addUserListener(UserDataListener listener) {
		if (user != null && listener != null) {
			user.addListener(listener);
		}
	}

	public void removeUserListener(UserDataListener listener) {
		if (user != null && listener != null) {
			user.removeListener(listener);
		}
	}

	private static class UserWordService implements UserDataListener, Runnable {

		private User user;

		private Vocabulary v;

		private boolean lock = false;

		public UserWordService(User user, Vocabulary v) {
			this.user = user;
			this.v = v;
		}

		private boolean addNewWord() {
			for (Word2Translate word : v.getWords()) {
				if (!user.containsUnknowns(word) && !user.containsKnowns(word)) {
					user.addWord(word, 0);
					return true;
				}
			}
			return false;
		}

		@Override
		public void run() {
			notify(Type.UNKNOWN_WORDS_CHANGED, user.getUnknownWords());
		}

		@Override
		public void notify(Type type, Object words) {
			if (lock) {
				return;
			}
			try {
				lock = true;
				int wordsAmount = user.getWordsAmount();
				while ((wordsAmount > user.getUnknownWords().size()) && addNewWord())
				;
			} finally {
				lock = false;
			}
		}
	}
}
