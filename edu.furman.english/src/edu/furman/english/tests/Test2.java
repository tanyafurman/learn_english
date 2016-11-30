package edu.furman.english.tests;

import java.util.Collections;
import java.util.List;

import edu.furman.english.data.User;
import edu.furman.english.data.Word;

public class Test2 extends Test {

	private List<Word> words;

	private List<Word> answer;

	private User user;

	public Test2(User user) {
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
		List<Word> words = getTestWords();
		if (answer != null) return answer;
		return answer = words.size() == 0 
				? Collections.emptyList()
				: shuffle(words).subList(0, 1);
	}

	@Override
	protected List<Word> getCorrectAnswer() {
		return getTestAnswers();
	}
}