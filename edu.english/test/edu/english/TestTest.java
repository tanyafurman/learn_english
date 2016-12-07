package edu.english;

import edu.english.data.User;
import edu.english.data.Vocabulary;
import edu.english.data.Word;
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
