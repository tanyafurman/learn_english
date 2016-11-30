package edu.furman.english;

import edu.furman.english.data.User;
import edu.furman.english.data.Vocabulary;
import edu.furman.english.data.Word;
import junit.framework.TestCase;

public class TestTest extends TestCase {

	protected static User createTestUser(Vocabulary v) {
		User user = new User("", "", "login", "");
		for (Word word : v.getWords()) {
			user.addWord(word, 0);
		}
		return user;
	}

}
