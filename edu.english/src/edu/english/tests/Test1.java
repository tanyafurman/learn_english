package edu.english.tests;

import java.util.Collections;
import java.util.List;

import edu.english.data.User;
import edu.english.data.Word2Translate;

public class Test1 extends Test {

	private List<Word2Translate> words;

	private List<Word2Translate> answers;

	private User user;

	public Test1(User user) {
		this.user = user;
	}

	@Override
	public List<Word2Translate> getTestAnswers() {
		if (answers == null) {
			answers = collectTestWords(user.getUnknownWords(), TEST_SIZE);
		}
		return answers;
	}

	@Override
	protected List<Word2Translate> getTestWords() {
		if (words == null) {
			List<Word2Translate> answers = getTestAnswers();
			words = answers.size() == 0 
					? Collections.emptyList()
					: shuffle(answers).subList(0, 1);
		}
		return words;
	}
	
	@Override
	protected List<Word2Translate> getCorrectAnswer() {
		return getTestWords();
	}
}
