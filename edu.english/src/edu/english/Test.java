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
 * ����� ��� �����. �������� �� ������ ��������� ���� ����� ����������� ����� � ��������� 2 ������:<br>
 * 1) ����� �� �������<br>
 * 2) ������������ �������<br>
 * <br>
 * ������ ������� �������� � �����������:<br>
 * 1) wordsSize -��������� ���� �� �������<br>
 * 2) answersSize - ���������� ������������ ���������<br>
 * <br>
 * ��� ������� ����� �� ������� ���� ������ 1 ��������� �������
 * <br>
 * <br>
 * ��������� ������������ ���������� ���� ������������ �� ������ ���� �������<br>
 * ������<br>
 * <code><br>
 * Test newTest = new Test(1, 2, user);<br>
 * List<Word2Translate> ourAnswers = new ArrayList<>();<br>
 * ourAnswers.add(new Word2Translate(newTest.getWords().get(0), newTest.getAswers().get(1)));<br>
 * List<Status> result = newTest.checkAnswers(ourAnswers);<br>
 * </code><br>
 * ���� ourAnswers ������ ���������� ����������, �� � result ����� ������ OK, ����� ERROR<br>
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
		return words.size() == 0//�������
				? Collections.emptyList()//true
				: shuffle(words).stream().map(w->w.getWord()).collect(Collectors.toList());//else 
				//������ ������ ����� ����� �������� � ����� ����� ������ map � �������� ������ � ���������
	}

	public List<String> getAnswers() {
		return answers.size() == 0 
				? Collections.emptyList()
				: shuffle(answers).stream().map(w->w.getTranslate()).collect(Collectors.toList());
				//������ ������ �������� ����� �������� � ����� ����� ������ map � �������� ������ � ���������
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

	protected static <T> List<T> shuffle(List<T> baseList) { // ������������ ���������
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
