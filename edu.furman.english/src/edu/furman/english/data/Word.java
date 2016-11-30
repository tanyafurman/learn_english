package edu.furman.english.data;

import java.io.Serializable;

public class Word implements Comparable<Word>, Serializable {

	private static final long serialVersionUID = 1L;
	private final String word;
	private final String translate;
	public Word(String word, String translate) {
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
		if (false == obj instanceof Word) return false;
		Word comp = (Word) obj;
		return word.equals(comp.word) && translate != null & translate.equals(comp.translate);
	}

	@Override
	public int hashCode() {
		return word.hashCode() + translate == null ? 0 : translate.hashCode();
	}

	@Override
	public int compareTo(Word o) {
		return this.word.compareTo(o.getWord());
	}
	
	@Override
	public String toString() {
		return "[" + word +":" + translate + "]";
	}
}
