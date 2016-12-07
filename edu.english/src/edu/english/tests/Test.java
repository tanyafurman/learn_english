package edu.english.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import edu.english.data.Status;
import edu.english.data.Word;
import edu.english.data.Status.StatusType;

public abstract class Test {

	public static int TEST_SIZE = 5;

	public List<String> getWords(){
		return getTestWords().size() == 0 
				? Collections.emptyList()
				: shuffle(getTestWords()).stream().map(w->w.getWord()).collect(Collectors.toList());
	};

	public List<String> getAnswers() {
		return getTestAnswers().size() == 0 
				? Collections.emptyList()
				: shuffle(getTestAnswers()).stream().map(w->w.getTranslate()).collect(Collectors.toList());
	}

	protected abstract List<Word> getTestAnswers();

	protected abstract List<Word> getTestWords();

	protected abstract List<Word> getCorrectAnswer();

	public List<Status> checkAnswers(List<Word> answers) {
		List<Status> result = new ArrayList<>();
		for (Word word: getCorrectAnswer()) {
			Status status = null;
			if (!answers.contains(word)) {
				status = new Status("No answer!", word, StatusType.ERROR);
			} else {
				status = new Status("Ok", word, StatusType.OK);
			}
			result.add(status);
		}
		return result;
	};

	protected static <T> List<T> shuffle(List<T> baseList) {
		long seed = System.nanoTime();
		ArrayList<T> result = new ArrayList<>(baseList);
		Collections.shuffle(result, new Random(seed));
		return result;
	}

	protected static List<Word> collectTestWords(Collection<Word> unknownWords, int length) {
		length = Math.min(length, unknownWords.size());
		if (length == 0) {
			return Collections.emptyList();
		}
		return shuffle(new ArrayList<>(unknownWords)).subList(0, length);
	}
}
