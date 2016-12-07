package edu.english.data;

import java.util.List;

import edu.english.tests.Test;

public class TestAdapter {

	private Test test;

	public TestAdapter(Test test) {
		this.test = test;
	}

	public List<String> getWords() {
		return test.getWords();
	}
	
	public List<String> getAnswers() {
		return test.getAnswers();
	}
}
