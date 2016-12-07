package edu.english.tests;

import java.util.Collections;
import java.util.List;

import edu.english.data.User;
import edu.english.data.Word2Translate;

public class Test2 extends Test {

	private List<Word2Translate> words;

	private List<Word2Translate> answer;

	private User user;

	public Test2(User user) {
		this.user = user;
	}

	@Override
	protected List<Word2Translate> getTestWords() {
		if (words == null) {
			words = collectTestWords(user.getUnknownWords(), TEST_SIZE);
		}
		return words;
	}

	@Override
	public List<Word2Translate> getTestAnswers() {
		List<Word2Translate> words = getTestWords();
		if (answer != null) return answer;
		return answer = words.size() == 0 
				? Collections.emptyList()
				: shuffle(words).subList(0, 1);
	}

	@Override
	protected List<Word2Translate> getCorrectAnswer() {
		return getTestAnswers();
	}
}