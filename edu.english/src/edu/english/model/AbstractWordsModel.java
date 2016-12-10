package edu.english.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import edu.english.data.UserDataListener;
import edu.english.data.Word2Translate;

public class AbstractWordsModel extends DefaultTableModel implements UserDataListener {

	private List<Word2Translate> words;

	private Type userEventType;

	public AbstractWordsModel(List<Word2Translate> words, Type userEventType) {
		this.userEventType = userEventType;
		setWords(words);
	}

	public AbstractWordsModel(List<Word2Translate> words) {
		
	}

	@Override
	public int getRowCount() {
		return words == null ? 0 : words.size();
	}

	public void setWords(List<Word2Translate> words) {
		this.words = words;
		fireTableChanged(new TableModelEvent(this));
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Word2Translate word = words.get(rowIndex);
		switch (columnIndex) {
		case 0:
			return word.getWord();
		case 1:
			return word.getTranslate();
		}
		return null;
	}

	@Override
	public String getColumnName(int column) {
		switch (column) {
		case 0:
			return "Word";
		case 1:
			return "Translate";
		}
		return super.getColumnName(column);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void notify(Type type, Object element) {
		if (userEventType == type) {
			ArrayList<Word2Translate> words = new ArrayList<>((Collection<Word2Translate>)element);
			Collections.sort(words);
			setWords(words == null ? Collections.emptyList() : words);
		}
	}

}
