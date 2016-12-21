package edu.english;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import javax.swing.table.DefaultTableModel;

import edu.english.data.Status;
import edu.english.data.User;
import edu.english.data.UserDataListener;
import edu.english.data.UserDataListener.Type;
import edu.english.data.Word2Translate;
import edu.english.model.AbstractWordsModel;
import edu.english.model.StatusModel;

/**
 * ƒанный класс €вл€етс€ онтроллером данного приложени€, то есть он управл€ет программой.<br>
 * Ётот класс имеет такие фуркции, как:<br>
 * 1) создаение модели дл€ доступа к даным, а именно к выученным словам, невыученным словам, статусу выполени€ тестов (результаты пройденных тестов) и словарю.<br>
 * 2) создаение объекта тестовю.<br>
 * 3) дополнение словар€ пользовател€ новыми словами.<br>
 * 4) обработка результатов пройденного теста пользователем.<br>
 * 5) изменение количества изучаемых слов.<br>
 * 6) изменение количества повторени€ слов дл€ изучени€.<br>
 * <br>
 * <br>
 * „тобы создать класс необходимо в конструктор класса передать пользовател€ и словарь.<br>
 * <code>
 * User user = UserManager.getInstance().login(login, pass);<br>
 * List<Word2Translate> vocabulary = createVocabulary();<br>
 * Application application = new Application(user, vocabulary);<br>
 * <br>
 * Test test = application.getNextTest(5, 5);<br>
 * ...<br>
 * List<Word2Translate> answers = getAnswers(...);<br>
 * List<Status> testResult = application.processTestResults(answers);<br>
 * </code>
 */
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

	// этот класс наполн€ем словарь юзера, если слов меньше чем он хочет учить
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
				vocabulary = shuffle(vocabulary);
				while ((wordsAmount > user.getUnknownWords().size()))
					if (!addNewWord()) {
						break;
					}
			} finally {
				lock = false;
			}
		}
		protected <T> List<T> shuffle(List<T> baseList) {
			long seed = System.nanoTime();
			ArrayList<T> result = new ArrayList<>(baseList);
			Collections.shuffle(result, new Random(seed));
			return result;
		}
	}
}
