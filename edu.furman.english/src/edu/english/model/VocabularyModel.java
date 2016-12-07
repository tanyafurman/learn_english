package edu.english.model;

import java.util.Collections;
import java.util.List;

import javax.swing.event.TableModelEvent;

import edu.english.data.Vocabulary;
import edu.english.data.Word;

public class VocabularyModel extends AbstractWordsModel {

	private static final long serialVersionUID = 1L;

	private Vocabulary v;

	public VocabularyModel(Vocabulary v) {
		this.v = v;
		fireTableChanged(new TableModelEvent(this));
	}

	@Override
	protected List<Word> getWords() {
		return v == null ? Collections.emptyList() : v.getWords();
	}
}
