package edu.english;

import java.util.ArrayList;
import java.util.List;

import edu.english.data.Status;
import edu.english.data.Status.StatusType;
import edu.english.data.User;
import edu.english.data.Word2Translate;
import junit.framework.TestCase;

public class Test2Test extends TestCase {

	public void testCollectAnswers() {
		List<Word2Translate> words = new ArrayList<>();
		words.add(new Word2Translate("A", "AA"));
		words.add(new Word2Translate("B", "BB"));
		words.add(new Word2Translate("C", "ACC"));

		//random
		checkAnswers(words);
		checkAnswers(words);
		checkAnswers(words);
		checkAnswers(words);
		checkAnswers(words);

		words = new ArrayList<>();
		words.add(new Word2Translate("A", "AA"));

		checkAnswers(words);
	}

	public static void checkAnswers(List<Word2Translate> v) {
		Application a = new Application(createTestUser(v), v);
		Test t = a.getNextTest(5,1);

		String answer = t.getAnswers().get(0);

		//check result
		@SuppressWarnings("serial")
		List<Word2Translate> expectedResult = new ArrayList<Word2Translate>() {
			{
				add(new Word2Translate(v.stream().filter(w->w.getTranslate().equals(answer)).findFirst().get().getWord(), answer));
			}
		};

		for (Status s : a.processTestResults(expectedResult)) {
			assertEquals(StatusType.OK, s.getType());
		}
	}
	
	protected static User createTestUser(List<Word2Translate> words) {
		User user = new User("", "", "login", "");
		words.forEach(w->user.addWord(w, 0));
		return user;
	}
}
