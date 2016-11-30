package edu.furman.english.tests;

import java.util.Collections;
import java.util.List;

import edu.furman.english.data.User;
import edu.furman.english.data.Word;

public class Test1 extends Test {

	private List<Word> words;

	private List<Word> answers;

	private User user;

	public Test1(User user) {
		this.user = user;
	}

	@Override
	public List<Word> getTestAnswers() {
		if (answers == null) {
			answers = collectTestWords(user.getUnknownWords(), TEST_SIZE);
		}
		return answers;
	}

	@Override
	protected List<Word> getTestWords() {
		if (words == null) {
			List<Word> answers = getTestAnswers();
			words = answers.size() == 0 ? Collections.emptyList(): shuffle(answers).subList(0, 1);
		}
		return words;
	}
	
	@Override
	protected List<Word> getCorrectAnswer() {
		return getTestWords();
	}
}
