package edu.furman.english;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import edu.furman.english.data.Status;
import edu.furman.english.data.TestAdapter;
import edu.furman.english.data.User;
import edu.furman.english.data.UserDataListener;
import edu.furman.english.data.Vocabulary;
import edu.furman.english.data.Word;
import edu.furman.english.model.KnownWordsModel;
import edu.furman.english.model.StatusModel;
import edu.furman.english.model.UnknownWordsModel;
import edu.furman.english.model.VocabularyModel;
import edu.furman.english.tests.Test;
import edu.furman.english.tests.Test0;
import edu.furman.english.tests.Test1;
import edu.furman.english.tests.Test2;

public class Application {

	private Vocabulary vocabulary;

	private User user;

	private UserWordService userWordService;

	private KnownWordsModel knownWordsModel;

	private UnknownWordsModel unknownWordsModel;

	private VocabularyModel vocabularyModel;

	private StatusModel statusModel;

	private Iterator<Test> testIterator = Collections.<Test>emptyList().iterator();

	private Test currentTest;

	public Application(User u, Vocabulary v) {
		this.user = u;
		this.vocabulary = v;
		this.userWordService = new UserWordService(u, v);
		this.user.addListener(userWordService);
		new Thread(userWordService).start();
	}

	public List<Status> processTestResults(List<Word> words) {
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

	public void addUnknownWord(Word word) {
		user.addWord(word, 0);
	}

	public int getTestAmount() {
		return 3;
	}

	public TestAdapter getNextTest() {
		if (!testIterator.hasNext()) {
			testIterator = Arrays.asList(new Test0(user),
				new Test1(user),
				new Test2(user)).iterator();
		}
		return new TestAdapter(currentTest = testIterator.next());
	}

	public void dispose() {
		removeUserListener(knownWordsModel);
		removeUserListener(unknownWordsModel);
		removeUserListener(userWordService);
		removeUserListener(statusModel);

		knownWordsModel = null;
		unknownWordsModel = null;
		userWordService = null;
		statusModel = null;
		vocabularyModel = null;

		if (user != null) {
			UserManager.getInstance().saveUser(user);
			user = null;
		}
		vocabulary = null;
	}

	@SuppressWarnings("unchecked")
	public <T> T getAdapter(Class<T> clazz) {
		Object result = null;
		if (KnownWordsModel.class == clazz) {
			if (knownWordsModel == null) {
				knownWordsModel = new KnownWordsModel();
				addUserListener(knownWordsModel);
			}
			result = knownWordsModel;
		} else if (StatusModel.class == clazz) {
			if (statusModel == null) {
				statusModel = new StatusModel();
				addUserListener(statusModel);
			}
			result = statusModel;
		} else if (UnknownWordsModel.class == clazz) {
			if (unknownWordsModel == null) {
				unknownWordsModel = new UnknownWordsModel();
				addUserListener(unknownWordsModel);
			}
			result = unknownWordsModel;
		} else if (VocabularyModel.class == clazz) {
			if (vocabularyModel == null) {
				vocabularyModel = new VocabularyModel(vocabulary);
			}
			result = vocabularyModel;
		}
		return (T) result;
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
			for (Word word : v.getWords()) {
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
