package edu.english.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import edu.english.data.UserDataListener;
import edu.english.data.Word2Translate;

public class KnownWordsModel extends WordsModel implements UserDataListener {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	@Override
	public void notify(Type type, Object element) {
		if (Type.KNOWN_WORDS_CHANGED == type) {
			ArrayList<Word2Translate> words = new ArrayList<>((Collection<Word2Translate>)element);
			Collections.sort(words);
			this.setNewWordMap(words);
		}
	}
}
