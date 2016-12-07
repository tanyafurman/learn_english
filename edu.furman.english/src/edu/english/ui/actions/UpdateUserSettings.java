package edu.english.ui.actions;

import java.awt.event.ActionEvent;
import java.text.NumberFormat;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.text.NumberFormatter;

import edu.english.ui.ApplicationFrame;

public class UpdateUserSettings extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private ApplicationFrame app;

	public UpdateUserSettings(ApplicationFrame app) {
		super("Settings");
		this.app = app;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (app.getApplication() == null) {
			return;
		}
		JLabel repeatCountLabel = new JLabel("Repeat Count:");
		JLabel wordsAmountLabel = new JLabel("Unknown words size:");
		

		NumberFormat numberFormat = NumberFormat.getInstance();
		NumberFormatter formatter = new NumberFormatter(numberFormat);
		formatter.setValueClass(Integer.class);
		formatter.setMinimum(1);
		formatter.setMaximum(Integer.MAX_VALUE);

		JSpinner repeatCountText = new JSpinner(createModel(app.getApplication().getRepeatCount()));
		JSpinner wordsAmountText = new JSpinner (createModel(app.getApplication().getUnknownWordsSize()));

		int result = JOptionPane.showConfirmDialog(null, new JComponent[]{repeatCountLabel, repeatCountText, wordsAmountLabel, wordsAmountText}, "Change user settings", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			app.getApplication().setRepeatCount(getSpinnerValue(repeatCountText));
			app.getApplication().setUnknownWordsAmount(getSpinnerValue(wordsAmountText));
		}
	}

	private int getSpinnerValue(JSpinner spinner) {
		return Integer.parseInt(((JSpinner.NumberEditor)spinner.getEditor()).getTextField().getText());
	}
	
	private SpinnerNumberModel createModel(int initValue) {
		return new SpinnerNumberModel(initValue, 1, Integer.MAX_VALUE, 1);
	}
}
