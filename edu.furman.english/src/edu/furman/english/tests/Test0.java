package edu.furman.english.tests;

import java.util.List;

import edu.furman.english.data.User;
import edu.furman.english.data.Word;

public class Test0 extends Test {

	private List<Word> words = null;

	private final User user;

	public Test0(User user) {
		this.user = user;
	}

	@Override
	protected List<Word> getTestWords() {
		if (words == null) {
			words = collectTestWords(user.getUnknownWords(), TEST_SIZE);
		}
		return words;
	}

	@Override
	public List<Word> getTestAnswers() {
		return getTestWords();
	}
	
	@Override
	protected List<Word> getCorrectAnswer() {
		return getTestWords();
	}
}
