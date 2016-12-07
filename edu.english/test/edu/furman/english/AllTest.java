package edu.furman.english;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	InternalizerTest.class,
	VocabularyTest.class,
	Test0Test.class,
	Test1Test.class,
	Test2Test.class
} )
public class AllTest {
}
