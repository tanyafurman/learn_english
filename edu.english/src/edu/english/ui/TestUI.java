package edu.english.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import edu.english.data.Status;
import edu.english.data.Status.StatusType;
import edu.english.data.Word2Translate;
import edu.english.resources.IconProvier;
import edu.english.tests.Test;

public class TestUI extends JPanel {

	private static final long serialVersionUID = 1L;

	private static final Color[] COLORS = new Color[] { Color.RED, Color.GREEN, Color.BLUE, Color.CYAN, Color.ORANGE };

	private HashMap<JLabel, JLabel> answersMap = new HashMap<>(5);

	private List<JLabel> words = new ArrayList<>();

	private List<JLabel> answers = new ArrayList<>();

	private JLabel lastWordLabel;

	public TestUI(Test test) {
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		JPanel wordsPanel = createHorizontalPanel(test.getWords(), (i)->COLORS[i], (label)->new WordsMousListener(label), words);
		JPanel answerPanel = createHorizontalPanel(test.getAnswers(), (i)->Color.black, (label)-> new AnswerMouseListener(label), answers);

		this.add(wordsPanel);
		this.add(answerPanel);
	}

	private JPanel createHorizontalPanel(List<String> items, ColorProvider colorProvider, ListenerCreatore listenerProvider, List<JLabel> labels) {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0, 1));
		for (int i = 0; i < items.size(); i++) {
			JLabel label = new JLabel(items.get(i));
			label.setAlignmentX(CENTER_ALIGNMENT);
			label.setForeground(colorProvider.getColor(i));
			label.addMouseListener(listenerProvider.createListener(label));
			label.setIcon(IconProvier.DEFAULT_ICON);
			panel.add(label);
			labels.add(label);
		}
		return panel;
	}

	public List<Word2Translate> getAnswerStatus() {
		return answersMap.entrySet().stream().map(e -> new Word2Translate(e.getKey().getText(), e.getValue().getText())).collect(Collectors.toList());
	}

	public void setResult(List<Status> statuses) {
		for (Status status: statuses) {
			if (status.getType() == StatusType.OK) {
				Entry<JLabel, JLabel> entry = answersMap.entrySet().stream()
				.filter(e-> e.getKey().getText().equals(status.getUw().getWord()) 
						&& e.getValue().getText().equals(status.getUw().getTranslate())).findFirst().get();
				entry.getValue().setIcon(IconProvier.TRUE_ICON);
				entry.getKey().setIcon(IconProvier.TRUE_ICON);
			}
		}
		for (Status status: statuses) {
			if (status.getType() == StatusType.ERROR) {
				JLabel word = words.stream().filter(w->w.getText().equals(status.getUw().getWord()) 
						&& w.getIcon() == IconProvier.DEFAULT_ICON).findFirst().get();
				JLabel answer = answers.stream().filter(a->a.getText().equals(status.getUw().getTranslate()) 
						&& a.getIcon() == IconProvier.DEFAULT_ICON).findFirst().get();
				word.setIcon(IconProvier.FALSE_ICON);
				answer.setIcon(IconProvier.FALSE_ICON);
			}
		}
	}

	public static interface ColorProvider {
		public Color getColor(int i);
	}

	public interface ListenerCreatore {
		public MouseListener createListener(JLabel label);
	}

	private class WordsMousListener extends JLabelMouseListener {

		public WordsMousListener(JLabel wordLabel) {
			super(wordLabel);
			if (lastWordLabel == null) {
				lastWordLabel = wordLabel;
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			lastWordLabel = getLabel();
		}
	}

	private class AnswerMouseListener extends JLabelMouseListener {

		public AnswerMouseListener(JLabel answerLabel) {
			super(answerLabel);
		}

		@Override
		public void mousePressed(MouseEvent e) {
			if (lastWordLabel == null) {
				return;
			}
			JLabel prevousAnsver = answersMap.get(lastWordLabel);
			if (prevousAnsver != null) {
				prevousAnsver.setForeground(Color.BLACK);
			}

			answersMap.put(lastWordLabel, getLabel());
			getLabel().setForeground(lastWordLabel.getForeground());
		}
	}
	
	private abstract static class JLabelMouseListener implements MouseListener {
		private final JLabel label;

		public JLabelMouseListener(JLabel answerLabel) {
			this.label = answerLabel;
		}

		public JLabel getLabel() {
			return label;
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}

		@Override
		public void mouseExited(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}
	}
}