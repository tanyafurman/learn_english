package edu.furman.english.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Vocabulary {

	private List<Word> words;

	private boolean loaded = false;

	public Vocabulary() {
		words = new ArrayList<>(1000);
	}

	/**
	 * load from default file
	 */
	public void load() {
		List<Word> words = new ArrayList<>();
		URL url = getClass().getResource("vocabulary.txt");
		InputStreamReader reader;
		try {
			reader = new InputStreamReader(url.openStream());
			BufferedReader br = new BufferedReader(reader);
			String line = null;
			while ((line = br.readLine()) != null) {
				String[] splitted = line.trim().split("=");
				if (splitted.length != 2) {
					System.out.println("Wrong line: " + line);
				}
				String word = splitted[0];
				String translation = splitted[1];
				words.add(new Word(word, translation));
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Check the vocabulary file.");
		}
		load(words);
	}

	public void load(List<Word> words) {
		this.words.addAll(words);
		Collections.sort(words, new Comparator<Word>(){
			@Override
			public int compare(Word o1, Word o2) {
				return o1.getWord().compareTo(o2.getWord());
			}
		});
		loaded = true;
	}

	
	public boolean isLoaded() {
		return loaded;
	}

	public Vocabulary(List<Word> words) {
		this.words = words;
	}

	public List<Word> getWords() {
		return words;
	}
}
