package edu.english;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import edu.english.data.Status;
import edu.english.data.User;
import edu.english.data.UserDataListener;
import edu.english.data.UserDataListener.Type;
import edu.english.data.Word2Translate;
import edu.english.model.AbstractWordsModel;
import edu.english.model.StatusModel;
import edu.english.tests.Test;

public class Application {

	public final static String KNOWN_WORDS_MODEL_ID = "KNOWN_WORDS_MODEL_ID";

	public final static String UNKNOWN_WORDS_MODEL_ID = "UNKNOWN_WORDS_MODEL_ID";

	public final static String VOCABULARY_WORDS_MODEL_ID = "VOCABULARY_WORDS_MODEL_ID";

	public final static String STATUS_MODEL_ID = "STATUS_MODEL_ID";

	private List<Word2Translate> vocabulary ;

	private User user;

	private UserWordService userWordService;

	private Test currentTest;

	public Application(User u) {
		this(u, load());
	}

	public Application(User u, List<Word2Translate> words) {
		this.user = u;
		vocabulary = words;
		this.userWordService = new UserWordService(u, vocabulary);
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
			result = new AbstractWordsModel(vocabulary, null);
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

	/**
	 * load from default file
	 */
	public static List<Word2Translate> load() {
		List<Word2Translate> words = new ArrayList<>();
		URL url = Application.class.getResource("vocabulary.txt");
		InputStreamReader reader;
		try {
			reader = new InputStreamReader(url.openStream());
			BufferedReader br = new BufferedReader(reader);
			String line = null;
			int i = -1;
			while ((line = br.readLine()) != null) {
				i++;
				String[] splitted = line.trim().split("=");
				if (splitted.length != 2) {
					System.out.println("Wrong line: " + line + ", Line number:" + i);
					continue;
				}
				String word = splitted[0];
				String translation = splitted[1];
				words.add(new Word2Translate(word, translation));
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Check the vocabulary file.");
		}
		Collections.sort(words, (o1, o2)->o1.getWord().compareTo(o2.getWord()));
		return words;
	}

	private static class UserWordService implements UserDataListener, Runnable {

		private User user;

		private List<Word2Translate> vocabulary;

		private boolean lock = false;

		public UserWordService(User user, List<Word2Translate> vocabulary) {
			this.user = user;
			this.vocabulary = vocabulary;
		}

		private boolean addNewWord() {
			for (Word2Translate word : vocabulary) {
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
				while ((wordsAmount > user.getUnknownWords().size()))
					if (!addNewWord()) {
						break;
					}
			} finally {
				lock = false;
			}
		}
	}
}
