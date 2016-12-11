package edu.english.tests;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import edu.english.data.Status;
import edu.english.data.Status.StatusType;
import edu.english.data.User;
import edu.english.data.Word2Translate;

public class Test {

	private List<Word2Translate> words = null;

	private List<Word2Translate> answers = null;

	public Test(int wordsSize, int answersSize, User user) {
		if (wordsSize > answersSize) {
			words = collectTestWords(user.getUnknownWords(), wordsSize);
			answers = words.size() == 0
					? Collections.emptyList()
					: shuffle(words).subList(0, Math.min(words.size(), answersSize));
		} else {
			answers = collectTestWords(user.getUnknownWords(), answersSize);
			words = answers.size() == 0
					? Collections.emptyList()
					: shuffle(answers).subList(0, Math.min(answers.size(), wordsSize));
		}
	}

	public List<String> getWords(){
		return words.size() == 0//условие
				? Collections.emptyList()//true
				: shuffle(words).stream().map(w->w.getWord()).collect(Collectors.toList());//else 
				//сперва мешаем слова потом обращаем в поток после строим map и собираем список в коллекцию
	}

	public List<String> getAnswers() {
		return answers.size() == 0 
				? Collections.emptyList()
				: shuffle(answers).stream().map(w->w.getTranslate()).collect(Collectors.toList());
				//сперва мешаем переводы потом обращаем в поток после строим map и собираем список в коллекцию
	}

	protected List<Word2Translate> getCorrectAnswer() {
		return words.size() > answers.size() ? answers : words;
	}

	public List<Status> checkAnswers(List<Word2Translate> answers) {
		List<Status> result = new ArrayList<>();
		for (Word2Translate word: getCorrectAnswer()) {
			Status status = null;
			if (!answers.contains(word)) {
				status = new Status("Bad or no answer!", word, StatusType.ERROR);
			} else {
				status = new Status("Ok", word, StatusType.OK);
			}
			result.add(status);
		}
		return result;
	}

	protected static <T> List<T> shuffle(List<T> baseList) { // перемешивает коллекцию
		long seed = System.nanoTime();
		ArrayList<T> result = new ArrayList<>(baseList);
		Collections.shuffle(result, new Random(seed));
		return result;
	}

	protected static List<Word2Translate> collectTestWords(Collection<Word2Translate> unknownWords, int length) {// выбирает из данной коллекции несколько слов length
		length = Math.min(length, unknownWords.size());
		if (length == 0) {
			return Collections.emptyList();
		}
		return shuffle(new ArrayList<>(unknownWords)).subList(0, length);
	}
}
