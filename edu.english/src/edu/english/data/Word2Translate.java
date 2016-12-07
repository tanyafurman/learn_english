package edu.english.data;

import java.io.Serializable;

public class Word2Translate implements Comparable<Word2Translate>, Serializable {

	private static final long serialVersionUID = 1L;
	private final String word;
	private final String translate;
	public Word2Translate(String word, String translate) {
		super();
		this.word = word;
		this.translate = translate;
	}

	public String getTranslate() {
		return translate;
	}

	public String getWord() {
		return word;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (false == obj instanceof Word2Translate) return false;
		Word2Translate comp = (Word2Translate) obj;
		return word.equals(comp.word) && translate != null & translate.equals(comp.translate);
	}

	@Override
	public int hashCode() {
		return word.hashCode() + translate == null ? 0 : translate.hashCode();
	}

	@Override
	public int compareTo(Word2Translate o) {
		return this.word.compareTo(o.getWord());
	}
	
	@Override
	public String toString() {
		return "[" + word +":" + translate + "]";
	}
}
