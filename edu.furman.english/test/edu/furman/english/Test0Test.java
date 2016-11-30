package edu.furman.english;

import java.util.ArrayList;
import java.util.List;

import edu.furman.english.data.Status;
import edu.furman.english.data.Status.StatusType;
import edu.furman.english.data.TestAdapter;
import edu.furman.english.data.User;
import edu.furman.english.data.Vocabulary;
import edu.furman.english.data.Word;
import junit.framework.TestCase;

public class Test0Test extends TestCase {

	public void testCorrect() {
		Vocabulary v = new Vocabulary();
		List<Word> words = new ArrayList<>();
		words.add(new Word("A", "AA"));
		words.add(new Word("B", "BB"));
		words.add(new Word("C", "ACC"));
		words.add(new Word("A", "AB"));
		v.load(words);

		Application a = new Application(createTestUser(v), v);
		TestAdapter t = a.getNextTest();

		//check result
		List<Word> expectedResult = new ArrayList<>(4);
		expectedResult.add(new Word("A", "AA"));
		expectedResult.add(new Word("B", "BB"));
		expectedResult.add(new Word("C", "ACC"));
		expectedResult.add(new Word("A", "AB"));

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
		for (Word word: v.getWords()) {
			user.addWord(word, 0);
		}
		return user;
	}
}
