package edu.english.model;

import java.util.List;

import javax.swing.table.DefaultTableModel;

import edu.english.data.Word2Translate;

public abstract class AbstractWordsModel extends DefaultTableModel {

	private static final long serialVersionUID = 1L;

	protected abstract List<Word2Translate> getWords();

	@Override
	public int getRowCount() {
		return getWords().size();
	}

	@Override
	public int getColumnCount() {
		return 2;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Word2Translate word = getWords().get(rowIndex);
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

	

}
