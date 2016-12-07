package edu.english;

import java.util.List;

public class StatusListAdapter<T> {

	private List<T> list;

	public StatusListAdapter(List<T> list) {
		this.list = list;
	}

	public void addAll(List<T> list) {
		this.list.addAll(list);
	}

	public void add(T value) {
		this.list.add(value);
	}
}
