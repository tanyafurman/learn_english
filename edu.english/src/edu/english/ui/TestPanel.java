package edu.english.ui;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import edu.english.Application;
import edu.english.data.Status;

/**
 * ����� TestPanel ������������ ��� �������� ������, ��� ������������ �������� ����.<br>
 * <br>
 * ������ ����� �������� � ���� ��� ������, � ������ Answer � Next, ������� ���������� ���� 
 * ������� ��� ����������� �����.<br>
 * <br>
 * ����� TestPanel ��������� ����� �������, ���:<br>
 * 1) ��������� ����, ����� ������������ ��� ���������.<br>
 * 2) ��������� ������ ������������.<br>
 * 3) �������� ���������� �����, ����� ����, ��� ������������ ����� �� ������ Next.<br>
 */

public class TestPanel extends JPanel {

	private static final int TEST_AMOUNT = 3;

	private static final long serialVersionUID = 1L;

	private Application app;

	private JButton answerButton;

	private JButton exitButton;

	private TestUI testPanel;

	private int currentTestNumber = 0;

	private boolean answered = false;

	public TestPanel(final ApplicationFrame frame) {
		this.app = frame.getApplication();

		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		JPanel buttonPanel = new JPanel();
		add(buttonPanel);
		buttonPanel.setAlignmentX(CENTER_ALIGNMENT);
		buttonPanel.setLayout(new FlowLayout());
		buttonPanel.add(answerButton = new JButton("Answer"));
		buttonPanel.add(exitButton = new JButton("Next"));

		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateTest();
			}
		});
		answerButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				checkAnswer();
			}
		});
		updateTest();
	}

	protected void updateTest() {
		answered = false;
		remove(testPanel);
		testPanel = createNextTestPanel();
		add(testPanel);
		revalidate();
	}

	protected void checkAnswer() {
		if (answered) return;
		answered = true;
		List<Status> statuses = app.processTestResults(testPanel.getAnswerStatus());
		testPanel.setResult(statuses);
	}

	protected TestUI createNextTestPanel() {
		if (currentTestNumber < TEST_AMOUNT - 1) {
			currentTestNumber++;
		} else {
			currentTestNumber = 0;
		}
		int words = 0;
		int answers = 0;

		switch (currentTestNumber) {
		case 0:
			words = 5;
			answers = 5;
			break;
		case 1:
			words = 1;
			answers = 5;
			break;
		case 2:
			words = 5;
			answers = 1;
			break;
		}

		return new TestUI(app.getNextTest(words, answers));
	}

	protected void dispose() {

	}

	@Override
	public void remove(Component comp) {
		if (comp != null) {
			super.remove(comp);
		}
	}

	@Override
	public Component add(Component comp) {
		if (comp != null) {
			return super.add(comp);
		}
		return null;
	}
}
