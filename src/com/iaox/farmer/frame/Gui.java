package com.iaox.farmer.frame;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.assignment.Assignment;
import com.iaox.farmer.assignment.AssignmentType;
import com.iaox.farmer.data.Data;
import com.iaox.farmer.task.Task;

import javax.swing.JPanel;

public class Gui {

	public JFrame frame;

	JList<String> list;
	private JScrollPane sourceScroll;
	private JList<Assignment> sourceList;
	private JScrollPane destScroll;
	private JList<Task> destList;
	private JButton addButton;
	private JButton btnRemoveTask;
	public static DefaultListModel<Task> destmodel;
	DefaultListModel<Assignment> sourcemodel;
	JButton btnStart;

	// private JComboBox<FightingAssignment> fightAssignment;
	private JPanel panel;

	private JComboBox<Integer> comboBoxSkillGoal;

	/**
	 * Launch the application.
	 * 
	 * 
	 * 
	 * /** Create the application.
	 */
	public Gui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setSize(600, 500);
		frame.setVisible(true);

		sourcemodel = new DefaultListModel<Assignment>();
		destmodel = new DefaultListModel<Task>();

		Arrays.asList(Assignment.values()).forEach(stage -> {
			sourcemodel.addElement(stage);
		});

		panel = new JPanel();
		panel.setBounds(0, 0, 594, 375);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		comboBoxSkillGoal = new JComboBox<Integer>();
		comboBoxSkillGoal.setBounds(203, 46, 52, 27);
		for (int i = 1; i <= 99;) {
			comboBoxSkillGoal.addItem(i);
			i++;
		}
		panel.add(comboBoxSkillGoal);
		sourceScroll = new JScrollPane();
		sourceScroll.setBounds(42, 44, 122, 242);
		panel.add(sourceScroll);

		sourceList = new JList<Assignment>(sourcemodel);
		sourceScroll.setViewportView(sourceList);

		destScroll = new JScrollPane();
		destScroll.setBounds(334, 40, 117, 247);
		panel.add(destScroll);

		destList = new JList<Task>(destmodel);
		destScroll.setViewportView(destList);

		addButton = new JButton("ADD >>");
		addButton.setBounds(202, 217, 83, 16);
		panel.add(addButton);

		btnRemoveTask = new JButton("<< REMOVE");
		btnRemoveTask.setBounds(201, 240, 86, 16);
		panel.add(btnRemoveTask);
		
		JCheckBox pureCheckBox = new JCheckBox("Pure?");
		pureCheckBox.setBounds(463, 111, 128, 23);
		panel.add(pureCheckBox);
		
		JCheckBox breaksCheckBox = new JCheckBox("Use breaks?");
		breaksCheckBox.setSelected(true);
		breaksCheckBox.setBounds(463, 158, 128, 23);
		panel.add(breaksCheckBox);

		btnStart = new JButton("START");
		btnStart.setBounds(48, 386, 488, 67);
		frame.getContentPane().add(btnStart);
		btnStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				IaoxAIO.TASK_HANDLER.clear();
				for (int i = 0; i < destList.getModel().getSize();) {

					Task selectedTask = destList.getModel().getElementAt(i);
					if (selectedTask.getAssignment().getType() == AssignmentType.SKILL
							&& Integer.parseInt(comboBoxSkillGoal.getSelectedItem().toString()) > 0) {
						IaoxAIO.TASK_HANDLER.add(selectedTask);
					}else if (selectedTask.getAssignment().getType() == AssignmentType.COMBAT
							&& Integer.parseInt(comboBoxSkillGoal.getSelectedItem().toString()) > 0) {
						IaoxAIO.TASK_HANDLER.add(selectedTask);
					}
					i++;
				}
				if(pureCheckBox.isSelected()){
				Data.trainDefence = false;
				}
				
				if(!breaksCheckBox.isSelected()){
					Data.USE_BREAKS = false;
				}
				IaoxAIO.guiWait = false;
			}

		});
		btnRemoveTask.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (destList.getModel().getSize() > 0 && !destList.isSelectionEmpty()) {
					if (destList.getModel().getElementAt(destList.getSelectedIndex()).getAssignment()
							.getType() == AssignmentType.QUEST) {
						sourcemodel.addElement(destList.getSelectedValue().getAssignment());
					}
					destmodel.remove(destList.getSelectedIndex());
				}
			}

		});
		addButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				destmodel.addElement(new Task(sourceList.getSelectedValue(),
						Integer.parseInt(comboBoxSkillGoal.getSelectedItem().toString()),
						sourceList.getSelectedValue().getSkill()));

				if (sourceList.getModel().getElementAt(sourceList.getSelectedIndex())
						.getType() == AssignmentType.QUEST) {
					sourcemodel.remove(sourceList.getSelectedIndex());
				}
			}

		});

	}

	public class IngredientListCellRenderer extends DefaultListCellRenderer {
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (value instanceof Task) {
				Task ingredient = (Task) value;
				setText("Stage: " + ingredient.getLevelGoal() + ingredient.getAssignment());
			}
			return this;
		}
	}

	public boolean isShowing() {
		return frame.isShowing();
	}
}
