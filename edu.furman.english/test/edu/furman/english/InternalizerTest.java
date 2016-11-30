package edu.furman.english;

import org.junit.Assert;
import org.junit.Test;

import edu.furman.english.resources.Internalizer;

public class InternalizerTest {

	@Test
	public void testRU() {
		Internalizer inter = new Internalizer("ru");
		Assert.assertEquals("���", inter.getMessage("firstName"));
		Assert.assertEquals("�������", inter.getMessage("secondName"));
		
	}
}
