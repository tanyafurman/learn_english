package edu.english;

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


/**
 * Данный класс создан и предназначен для тестов.<br>
 * <br>
 * <br>
 * Он хранит в себе два списка со словами:<br>
 * 1) список с русскими словами.<br>
 * 2) список с переводами, то есть английскими словами.<br>
 * <br>
 * <br>
 * Класс Test выполняет такие функции, как:<br>
 * 1) проверяет результаты пользователя после прохождения теста.<br>
 * 2) мешает словарь перед тем, как добавить рандомное новое слово в список изучаемых слов.<br>
 * <br>
 * <br>
 * Класс Test может менять размеры для:<br>
 * 1) wordsSize - количество слов в списке изучаемых.<br>
 * 2) answersSize - количество слов в списке выученных.<br>
 * <br>
 * <br>
 * Русские слова могут иметь только один английский перевод.<br>
 * <br>
 * <br>
 * Наприме:<br>
 * <code><br>
 * Test newTest = new Test(1, 2, user);<br>
 * List<Word2Translate> ourAnswers = new ArrayList<>();<br>
 * ourAnswers.add(new Word2Translate(newTest.getWords().get(0), newTest.getAswers().get(1)));<br>
 * List<Status> result = newTest.checkAnswers(ourAnswers);<br>
 * </code><br>
 * Если ответы при прохождении теста являются правильными, то result возвращается сос значеним OK, иначе ERROR.<br>
 */
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
		return words.size() == 0
				? Collections.emptyList()
				: shuffle(words).stream().map(w->w.getWord()).collect(Collectors.toList());//else 
	}

	public List<String> getAnswers() {
		return answers.size() == 0 
				? Collections.emptyList()
				: shuffle(answers).stream().map(w->w.getTranslate()).collect(Collectors.toList());
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

	protected static <T> List<T> shuffle(List<T> baseList) {
		long seed = System.nanoTime();
		ArrayList<T> result = new ArrayList<>(baseList);
		Collections.shuffle(result, new Random(seed));
		return result;
	}

	protected static List<Word2Translate> collectTestWords(Collection<Word2Translate> unknownWords, int length) {// �������� �� ������ ��������� ��������� ���� length
		length = Math.min(length, unknownWords.size());
		if (length == 0) {
			return Collections.emptyList();
		}
		return shuffle(new ArrayList<>(unknownWords)).subList(0, length);
	}
}
