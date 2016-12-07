package edu.english;

import org.junit.Assert;
import org.junit.Test;

import edu.english.data.Vocabulary;

public class VocabularyTest {

	@Test
	public void testVocabulary() {
		Vocabulary v = new Vocabulary();
		v.load();
		Assert.assertTrue(v.isLoaded());
		Assert.assertFalse(v.getWords().isEmpty());
	}

}
