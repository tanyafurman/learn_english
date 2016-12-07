package edu.english.model;

import java.util.Collections;
import java.util.List;

import javax.swing.event.TableModelEvent;

import edu.english.data.Word;

public class WordsModel extends AbstractWordsModel {

	private static final long serialVersionUID = 1L;

	private List<Word> words;

	public void setNewWordMap(List<Word> words) {
		this.words = words;
		fireTableChanged(new TableModelEvent(this));
	}

	@Override
	protected List<Word> getWords() {
		return words == null ? Collections.emptyList() : words;
	}
}