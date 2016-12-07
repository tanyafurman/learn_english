package edu.english.model;

import java.util.Collections;
import java.util.List;

import javax.swing.event.TableModelEvent;

import edu.english.data.Word2Translate;

public class WordsModel extends AbstractWordsModel {

	private static final long serialVersionUID = 1L;

	private List<Word2Translate> words;

	public void setNewWordMap(List<Word2Translate> words) {
		this.words = words;
		fireTableChanged(new TableModelEvent(this));
	}

	@Override
	protected List<Word2Translate> getWords() {
		return words == null ? Collections.emptyList() : words;
	}
}