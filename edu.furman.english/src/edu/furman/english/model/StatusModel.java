package edu.furman.english.model;

import java.util.Collections;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import edu.furman.english.data.Status;
import edu.furman.english.data.UserDataListener;

public class StatusModel extends DefaultTableModel implements UserDataListener {

	private static final long serialVersionUID = 1L;

	private List<Status> statuses = Collections.emptyList();

	public StatusModel() {
		super(new String[][]{{"","",""}}, new String[]{"Status","Word","Message"});
	}

	@Override
	public int getRowCount() {
		return statuses == null ? 0 : statuses.size();
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Status stauts = statuses.get(rowIndex);
		switch (columnIndex) {
		case 0:return stauts.getType().toString();
		case 1:return stauts.getUw();
		case 2:return stauts.getMessage();
		default:
			break;
		}
		return "Error";
	}

	@SuppressWarnings("unchecked")
	@Override
	public void notify(Type type,Object statuses) {
		if (Type.STATUS_CHANGED == type) {
			this.statuses = (List<Status>) statuses;
			fireTableChanged(new TableModelEvent(this));
		}
	}
}
