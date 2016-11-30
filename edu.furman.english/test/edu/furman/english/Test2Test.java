package edu.furman.english;

import java.util.ArrayList;
import java.util.List;

import edu.furman.english.data.Status;
import edu.furman.english.data.Status.StatusType;
import edu.furman.english.data.TestAdapter;
import edu.furman.english.data.Vocabulary;
import edu.furman.english.data.Word;

public class Test2Test extends TestTest {

	public void testCollectAnswers() {
		Vocabulary v = new Vocabulary();
		List<Word> words = new ArrayList<>();
		words.add(new Word("A", "AA"));
		words.add(new Word("B", "BB"));
		words.add(new Word("C", "ACC"));
		v.load(words);

		//random
		checkAnswers(v);
		checkAnswers(v);
		checkAnswers(v);
		checkAnswers(v);
		checkAnswers(v);

		v = new Vocabulary();
		words = new ArrayList<>();
		words.add(new Word("A", "AA"));
		v.load(words);

		checkAnswers(v);
	}

	public static void checkAnswers(Vocabulary v) {
		Application a = new Application(createTestUser(v), v);
		a.getNextTest();
		a.getNextTest();
		TestAdapter t = a.getNextTest();

		String answer = t.getAnswers().get(0);

		//check result
		@SuppressWarnings("serial")
		List<Word> expectedResult = new ArrayList<Word>() {
			{
				add(new Word(v.getWords().stream().filter(w->w.getTranslate().equals(answer)).findFirst().get().getWord(), answer));
			}
		};

		for (Status s : a.processTestResults(expectedResult)) {
			assertEquals(StatusType.OK, s.getType());
		}
	}
}
