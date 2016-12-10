package edu.english;

import java.util.ArrayList;
import java.util.List;

import edu.english.data.Status;
import edu.english.data.Status.StatusType;
import edu.english.data.Vocabulary;
import edu.english.data.Word2Translate;
import edu.english.tests.Test;

public class Test1Test extends TestTest {

	public void testCollectAnswers() {
		Vocabulary v = new Vocabulary();
		List<Word2Translate> words = new ArrayList<>();
		words.add(new Word2Translate("A", "AA"));
		words.add(new Word2Translate("B", "BB"));
		words.add(new Word2Translate("C", "ACC"));
		v.load(words);

		//random
		checkAnswers(v);
		checkAnswers(v);
		checkAnswers(v);
		checkAnswers(v);
		checkAnswers(v);

		v = new Vocabulary();
		words = new ArrayList<>();
		words.add(new Word2Translate("A", "AA"));
		v.load(words);

		checkAnswers(v);
	}

	public static void checkAnswers(Vocabulary v) {
		Application a = new Application(createTestUser(v), v);
		a.getNextTest();
		Test t = a.getNextTest();

		String testWord = t.getWords().get(0);

		//check result
		@SuppressWarnings("serial")
		List<Word2Translate> expectedResult = new ArrayList<Word2Translate>() {
			{
				add(new Word2Translate(testWord, v.getWords().stream().filter(w->w.getWord().equals(testWord)).findFirst().get().getTranslate()));
			}
		};

		for (Status s : a.processTestResults(expectedResult)) {
			assertEquals(StatusType.OK, s.getType());
		}
	}
}
