package edu.english;

import java.util.ArrayList;
import java.util.List;

import edu.english.Application;
import edu.english.data.Status;
import edu.english.data.TestAdapter;
import edu.english.data.User;
import edu.english.data.Vocabulary;
import edu.english.data.Word2Translate;
import edu.english.data.Status.StatusType;
import junit.framework.TestCase;

public class Test0Test extends TestCase {

	public void testCorrect() {
		Vocabulary v = new Vocabulary();
		List<Word2Translate> words = new ArrayList<>();
		words.add(new Word2Translate("A", "AA"));
		words.add(new Word2Translate("B", "BB"));
		words.add(new Word2Translate("C", "ACC"));
		words.add(new Word2Translate("A", "AB"));
		v.load(words);

		Application a = new Application(createTestUser(v), v);
		TestAdapter t = a.getNextTest();

		//check result
		List<Word2Translate> expectedResult = new ArrayList<>(4);
		expectedResult.add(new Word2Translate("A", "AA"));
		expectedResult.add(new Word2Translate("B", "BB"));
		expectedResult.add(new Word2Translate("C", "ACC"));
		expectedResult.add(new Word2Translate("A", "AB"));

		for (Status s: a.processTestResults(expectedResult)) {
			assertEquals(StatusType.OK, s.getType());
		}

		List<String> offer = t.getWords();
		assertTrue(offer.contains("A"));
		assertTrue(offer.contains("B"));
		assertTrue(offer.contains("C"));

		List<String> answers = t.getAnswers();
		assertTrue(answers.contains("AA"));
		assertTrue(answers.contains("BB"));
		assertTrue(answers.contains("ACC"));
		assertTrue(answers.contains("AB"));
	}

	private User createTestUser(Vocabulary v) {
		User user = new User("", "", "login", "");
		for (Word2Translate word: v.getWords()) {
			user.addWord(word, 0);
		}
		return user;
	}
}
