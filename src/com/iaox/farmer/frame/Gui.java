package com.iaox.farmer.frame;




import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.iaox.farmer.IaoxAIO;
import com.iaox.farmer.ai.IaoxIntelligence;
import com.iaox.farmer.ai.RandomBreak;
import com.iaox.farmer.assignment.Assignment;
import com.iaox.farmer.assignment.AssignmentType;
import com.iaox.farmer.assignment.agility.AgilityAssignment;
import com.iaox.farmer.assignment.combat.FightingAssignment;
import com.iaox.farmer.assignment.fishing.FishingAssignment;
import com.iaox.farmer.assignment.mining.MiningAssignment;
import com.iaox.farmer.assignment.woodcutting.WoodcuttingAssignment;
import com.iaox.farmer.data.Data;
import com.iaox.farmer.data.ExperienceTable;
import com.iaox.farmer.task.Task;

public class Gui {

	public JFrame frame;

	JList<String> list;
	private JScrollPane taskSourceScroll;
	private JList<Assignment> taskSourceList;
	private JScrollPane taskDestScroll;
	private JList<Task> taskDestList;
	private JButton addTaskButton;
	private JButton btnRemoveTask;
	public static DefaultListModel<Task> taskDestModel;
	public static DefaultListModel<RandomBreak> breakDestModel;
	DefaultListModel<Assignment> sourcemodel;
	JButton btnStart;

	// private JComboBox<FightingAssignment> fightAssignment;
	private JPanel panel;

	private JComboBox<Integer> comboBoxSkillGoal;
	private JCheckBox chooseAssignmentCheckBox;

	protected Assignment currentSourceAssignment;

	private JComboBox comboBoxMiningAssignment;

	private JComboBox<WoodcuttingAssignment> comboBoxWoodcuttingAssignment;

	private JComboBox<FightingAssignment> comboBoxFightingAssignment;

	private JComboBox<AgilityAssignment> comboBoxAgilityAssignment;

	private JComboBox<FishingAssignment> comboBoxFishingAssignment;

	private JCheckBox pureCheckBox;

	private JCheckBox breaksCheckBox;

	private JPanel breakPanel;

	private JButton switchToStartPanelButton;
	private JButton switchToBreakPanelButton;
	private JScrollPane breakDestScroll;
	private JList<RandomBreak> breakDestList;
	private JComboBox<Integer> playTimeComboBox;
	private JButton addBreakButton;
	private JButton removeBreakButton;
	private JLabel lblAdd;

	private JComboBox<Integer> breakTimeComboBox;
	private JLabel lblNewLabel;
	private JLabel lblMinutesToBreak;

	/**
	 * Launch the application.
	 * 
	 * 
	 * 
	 * /** Create the application.
	 */
	public Gui() {
		initialize();
		Data.PLAYER_GENERATED_BREAKS = new ArrayList<RandomBreak>();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		initStartPanel();
		initBreakPanel();
		initTopPanel();

	}

	private void initTopPanel() {
		

		

		panel.add(comboBoxSkillGoal);
		switchToStartPanelButton = new JButton("Task Handler");
		switchToStartPanelButton.setBounds(10, 3, 99, 16);
		frame.getContentPane().add(switchToStartPanelButton);

		switchToStartPanelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!panel.isVisible()) {
					panel.setVisible(true);
					breakPanel.setVisible(false);
				}
			}

		});

		switchToBreakPanelButton = new JButton("Break Handler");
		switchToBreakPanelButton.setBounds(119, 3, 99, 16);
		frame.getContentPane().add(switchToBreakPanelButton);

		switchToBreakPanelButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!breakPanel.isVisible()) {
					breakPanel.setVisible(true);
					panel.setVisible(false);
				}
			}

		});

	}

	private void initBreakPanel() {

		breakPanel = new JPanel();
		breakPanel.setBounds(0, 22, 574, 353);
		frame.getContentPane().add(breakPanel);
		breakPanel.setLayout(null);

		breakDestScroll = new JScrollPane();
		breakDestScroll.setBounds(373, 80, 191, 247);
		breakPanel.add(breakDestScroll);
		
		breakDestModel = new DefaultListModel<RandomBreak>();
		
		breakDestList = new JList<RandomBreak>(breakDestModel);
		breakDestList.setFont(new Font("Tahoma", Font.PLAIN, 8));
		breakDestScroll.setColumnHeaderView(breakDestList);

		playTimeComboBox = new JComboBox<Integer>();
		playTimeComboBox.setBounds(224, 93, 52, 27);

		for (int i = 30; i <= 250;) {
			playTimeComboBox.addItem(i);
			i++;
		}

		breakPanel.add(playTimeComboBox);

		addBreakButton = new JButton("ADD >>");
		addBreakButton.setBounds(214, 181, 83, 16);
		breakPanel.add(addBreakButton);

		removeBreakButton = new JButton("<< REMOVE");
		removeBreakButton.setBounds(211, 208, 86, 16);
		breakPanel.add(removeBreakButton);

		breakTimeComboBox = new JComboBox<Integer>();
		breakTimeComboBox.setBounds(224, 131, 52, 27);

		for (int i = 5; i <= 250;) {
			breakTimeComboBox.addItem(i);
			i++;
		}
		breakPanel.add(breakTimeComboBox);

		JLabel lblBreakHandler = new JLabel("Break Handler");
		lblBreakHandler.setFont(new Font("Tahoma", Font.PLAIN, 26));
		lblBreakHandler.setBounds(171, 25, 172, 52);
		breakPanel.add(lblBreakHandler);

		lblNewLabel = new JLabel("Minutes to play before break");
		lblNewLabel.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblNewLabel.setBounds(50, 99, 164, 14);
		breakPanel.add(lblNewLabel);

		lblMinutesToBreak = new JLabel("Minutes to break");
		lblMinutesToBreak.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblMinutesToBreak.setBounds(50, 137, 145, 14);
		breakPanel.add(lblMinutesToBreak);
		breakPanel.setVisible(false);
		
		addBreakButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				breakDestModel.addElement(new RandomBreak(Integer.parseInt(playTimeComboBox.getSelectedItem().toString()),Integer.parseInt(breakTimeComboBox.getSelectedItem().toString())));
			}

		});
		
		removeBreakButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				RandomBreak selectedBreak = breakDestList.getSelectedValue();
				if(selectedBreak != null){
					breakDestModel.remove(breakDestList.getSelectedIndex());
				}
			}

		});

	}

	private void initStartPanel() {
		initFrame();
		initSourceAndDestList();
		initButtonAndCheckBoxes();
		initChooseAssignment();
		initSkillGoal();

	}

	private void initChooseAssignment() {
		chooseAssignmentCheckBox = new JCheckBox("Choose assignment ");
		chooseAssignmentCheckBox.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		chooseAssignmentCheckBox.setBounds(189, 125, 148, 44);
		panel.add(chooseAssignmentCheckBox);

		comboBoxMiningAssignment = new JComboBox<MiningAssignment>();
		comboBoxMiningAssignment.setBounds(187, 198, 137, 27);
		panel.add(comboBoxMiningAssignment);
		comboBoxMiningAssignment.setVisible(false);

		comboBoxWoodcuttingAssignment = new JComboBox<WoodcuttingAssignment>();
		comboBoxWoodcuttingAssignment.setBounds(187, 198, 137, 27);
		panel.add(comboBoxWoodcuttingAssignment);
		comboBoxWoodcuttingAssignment.setVisible(false);

		comboBoxFightingAssignment = new JComboBox<FightingAssignment>();
		comboBoxFightingAssignment.setBounds(188, 196, 137, 27);
		panel.add(comboBoxFightingAssignment);
		comboBoxFightingAssignment.setVisible(false);

		comboBoxAgilityAssignment = new JComboBox<AgilityAssignment>();
		comboBoxAgilityAssignment.setBounds(188, 198, 137, 27);
		panel.add(comboBoxAgilityAssignment);
		comboBoxAgilityAssignment.setVisible(false);

		comboBoxFishingAssignment = new JComboBox<FishingAssignment>();
		comboBoxFishingAssignment.setBounds(189, 197, 137, 27);
		panel.add(comboBoxFishingAssignment);
		comboBoxFishingAssignment.setVisible(false);
		for (MiningAssignment ass : MiningAssignment.values()) {
			comboBoxMiningAssignment.addItem(ass);
		}
		for (WoodcuttingAssignment ass : WoodcuttingAssignment.values()) {
			comboBoxWoodcuttingAssignment.addItem(ass);
		}
		for (FightingAssignment ass : FightingAssignment.values()) {
			comboBoxFightingAssignment.addItem(ass);
		}
		for (AgilityAssignment ass : AgilityAssignment.values()) {
			comboBoxAgilityAssignment.addItem(ass);
		}
		for (FishingAssignment ass : FishingAssignment.values()) {
			comboBoxFishingAssignment.addItem(ass);
		}

	}

	private void initButtonAndCheckBoxes() {

		addTaskButton = new JButton("ADD >>");
		addTaskButton.setBounds(216, 299, 83, 16);
		panel.add(addTaskButton);

		btnRemoveTask = new JButton("<< REMOVE");
		btnRemoveTask.setBounds(213, 326, 86, 16);
		panel.add(btnRemoveTask);

		pureCheckBox = new JCheckBox("Pure?");
		pureCheckBox.setBounds(466, 137, 128, 23);
		panel.add(pureCheckBox);

		breaksCheckBox = new JCheckBox("Use custom breaks?");
		breaksCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 9));
		breaksCheckBox.setSelected(true);
		breaksCheckBox.setBounds(466, 184, 128, 23);
		panel.add(breaksCheckBox);

		lblAdd = new JLabel("Custom Task Handler");
		lblAdd.setFont(new Font("Tahoma", Font.PLAIN, 26));
		lblAdd.setBounds(133, -18, 272, 128);
		panel.add(lblAdd);

		btnRemoveTask.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (taskDestList.getModel().getSize() > 0 && !taskDestList.isSelectionEmpty()) {
					if (taskDestList.getModel().getElementAt(taskDestList.getSelectedIndex()).getAssignment()
							.getType() == AssignmentType.QUEST) {
						sourcemodel.addElement(taskDestList.getSelectedValue().getAssignment());
					}
					taskDestModel.remove(taskDestList.getSelectedIndex());
				}
			}

		});
		addTaskButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				taskDestModel.addElement(getTask());

				if (taskSourceList.getModel().getElementAt(taskSourceList.getSelectedIndex())
						.getType() == AssignmentType.QUEST) {
					sourcemodel.remove(taskSourceList.getSelectedIndex());
				}
			}

		});
		
		
		
		
		
		
		
		
		
		
		addTaskButton = new JButton("ADD >>");
		addTaskButton.setBounds(216, 299, 83, 16);
		panel.add(addTaskButton);

		btnRemoveTask = new JButton("<< REMOVE");
		btnRemoveTask.setBounds(213, 326, 86, 16);
		panel.add(btnRemoveTask);

		pureCheckBox = new JCheckBox("Pure?");
		pureCheckBox.setBounds(466, 137, 128, 23);
		panel.add(pureCheckBox);

		breaksCheckBox = new JCheckBox("Use custom breaks?");
		breaksCheckBox.setFont(new Font("Tahoma", Font.PLAIN, 9));
		breaksCheckBox.setSelected(true);
		breaksCheckBox.setBounds(466, 184, 128, 23);
		panel.add(breaksCheckBox);
		btnStart = new JButton("START");
		btnStart.setBounds(48, 386, 488, 67);
		frame.getContentPane().add(btnStart);

		btnStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				IaoxAIO.TASK_HANDLER.clear();
				for (int i = 0; i < taskDestList.getModel().getSize();) {

					Task selectedTask = taskDestList.getModel().getElementAt(i);
					if (selectedTask.getAssignment().getType() == AssignmentType.SKILL
							&& ExperienceTable.getXp(Integer.parseInt(comboBoxSkillGoal.getSelectedItem().toString())) > 0) {
						IaoxAIO.TASK_HANDLER.add(selectedTask);

					} else if (selectedTask.getAssignment().getType() == AssignmentType.MELEE
							&& ExperienceTable.getXp(Integer.parseInt(comboBoxSkillGoal.getSelectedItem().toString())) > 0) {
						IaoxAIO.TASK_HANDLER.add(selectedTask);
					}
					i++;
				}
				if (!pureCheckBox.isSelected()) {
					Data.trainDefence = true;
				}

				if (breaksCheckBox.isSelected()) {
					Data.USE_BREAKS = true;
					for (int i = 0; i < breakDestList.getModel().getSize();) {

						RandomBreak selectedBreak = breakDestList.getModel().getElementAt(i);
						if(selectedBreak != null){
							Data.PLAYER_GENERATED_BREAKS.add(selectedBreak);
						}
						i++;
					}
				}
				IaoxAIO.guiWait = false;
			}

		});
		
		comboBoxSkillGoal = new JComboBox<Integer>();
		comboBoxSkillGoal.setBounds(229, 89, 52, 27);
	}

	private void initSourceAndDestList() {

		sourcemodel = new DefaultListModel<Assignment>();
		taskDestModel = new DefaultListModel<Task>();

		Arrays.asList(Assignment.values()).forEach(stage -> {
			sourcemodel.addElement(stage);
		});
		
		taskSourceScroll = new JScrollPane();
		taskSourceScroll.setBounds(45, 93, 122, 242);
		panel.add(taskSourceScroll);

		taskSourceList = new JList<Assignment>(sourcemodel);
		taskSourceScroll.setViewportView(taskSourceList);

		taskSourceList.addListSelectionListener(new ListSelectionListener() {

			@Override
			public void valueChanged(ListSelectionEvent e) {
				setAssignmentDropDownList(taskSourceList.getSelectedValue());
			}

		});

		taskDestScroll = new JScrollPane();
		taskDestScroll.setBounds(343, 93, 117, 247);
		panel.add(taskDestScroll);

		taskDestList = new JList<Task>(taskDestModel);
		taskDestScroll.setColumnHeaderView(taskDestList);

	}

	private void initSkillGoal() {

		for (int i = 1; i <= 99;) {
			comboBoxSkillGoal.addItem(i);
			i++;
		}

	}

	private void initFrame() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setSize(600, 500);
		frame.setVisible(true);
		
		panel = new JPanel();
		panel.setBounds(0, 22, 594, 353);
		frame.getContentPane().add(panel);
		panel.setLayout(null);
	}

	public Task getTask() {
		Task task = null;
		switch (taskSourceList.getSelectedValue()) {
		case AGILITY:
			if (chooseAssignmentCheckBox.isSelected() && comboBoxAgilityAssignment.getSelectedItem() != null) {
				return new Task(taskSourceList.getSelectedValue(),
						ExperienceTable.getXp(Integer.parseInt(comboBoxSkillGoal.getSelectedItem().toString())),
						taskSourceList.getSelectedValue().getSkill(),
						(AgilityAssignment) comboBoxAgilityAssignment.getSelectedItem());
			}
			break;
		case ATTACK:
			if (chooseAssignmentCheckBox.isSelected() && comboBoxFightingAssignment.getSelectedItem() != null) {
				return new Task(taskSourceList.getSelectedValue(),
						ExperienceTable.getXp(Integer.parseInt(comboBoxSkillGoal.getSelectedItem().toString())),
						taskSourceList.getSelectedValue().getSkill(),
						(FightingAssignment) comboBoxFightingAssignment.getSelectedItem());
			}
			break;
		case DEFENCE:
			if (chooseAssignmentCheckBox.isSelected() && comboBoxFightingAssignment.getSelectedItem() != null) {
				return new Task(taskSourceList.getSelectedValue(),
					ExperienceTable.getXp(Integer.parseInt(comboBoxSkillGoal.getSelectedItem().toString())),
						taskSourceList.getSelectedValue().getSkill(),
						(FightingAssignment) comboBoxFightingAssignment.getSelectedItem());
			}
			break;
		case MINING:
			if (chooseAssignmentCheckBox.isSelected() && comboBoxMiningAssignment.getSelectedItem() != null) {
				return new Task(taskSourceList.getSelectedValue(),
						ExperienceTable.getXp(Integer.parseInt(comboBoxSkillGoal.getSelectedItem().toString())),
						taskSourceList.getSelectedValue().getSkill(),
						(MiningAssignment) comboBoxMiningAssignment.getSelectedItem());
			}
		case STRENGTH:
			if (chooseAssignmentCheckBox.isSelected() && comboBoxFightingAssignment.getSelectedItem() != null) {
				return new Task(taskSourceList.getSelectedValue(),
						ExperienceTable.getXp(Integer.parseInt(comboBoxSkillGoal.getSelectedItem().toString())),
						taskSourceList.getSelectedValue().getSkill(),
						(FightingAssignment) comboBoxFightingAssignment.getSelectedItem());
			}
			break;
		case WOODCUTTING:
			if (chooseAssignmentCheckBox.isSelected() && comboBoxWoodcuttingAssignment.getSelectedItem() != null) {
				return new Task(taskSourceList.getSelectedValue(),
						ExperienceTable.getXp(Integer.parseInt(comboBoxSkillGoal.getSelectedItem().toString())),
						taskSourceList.getSelectedValue().getSkill(),
						(WoodcuttingAssignment) comboBoxWoodcuttingAssignment.getSelectedItem());
			}
		case FISHING:
			if (chooseAssignmentCheckBox.isSelected() && comboBoxFishingAssignment.getSelectedItem() != null) {
				return new Task(taskSourceList.getSelectedValue(),
						ExperienceTable.getXp(Integer.parseInt(comboBoxSkillGoal.getSelectedItem().toString())),
						taskSourceList.getSelectedValue().getSkill(),
						(FishingAssignment) comboBoxFishingAssignment.getSelectedItem());
			}

		}
		return new Task(taskSourceList.getSelectedValue(),
				ExperienceTable.getXp(Integer.parseInt(comboBoxSkillGoal.getSelectedItem().toString())),
				taskSourceList.getSelectedValue().getSkill());
	}

	public void setAssignmentDropDownList(Assignment assignment) {
		comboBoxMiningAssignment.setVisible(false);
		comboBoxWoodcuttingAssignment.setVisible(false);
		comboBoxFightingAssignment.setVisible(false);
		comboBoxAgilityAssignment.setVisible(false);
		comboBoxFishingAssignment.setVisible(false);

		if (assignment.equals(Assignment.MINING)) {
			comboBoxMiningAssignment.setVisible(true);
		} else if (assignment.equals(Assignment.WOODCUTTING)) {
			comboBoxWoodcuttingAssignment.setVisible(true);
		} else if (assignment.getType().equals(AssignmentType.MELEE)) {
			comboBoxFightingAssignment.setVisible(true);
		} else if (assignment.equals(Assignment.AGILITY)) {
			comboBoxAgilityAssignment.setVisible(true);
		} else if (assignment.equals(Assignment.FISHING)) {
			comboBoxFishingAssignment.setVisible(true);
		}

	}

	public class IngredientListCellRenderer extends DefaultListCellRenderer {
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {
			super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (value instanceof Task) {
				Task ingredient = (Task) value;
				setText("Stage: " + ingredient.getGoalExperience() + ingredient.getAssignment());
			}
			return this;
		}
	}

	public boolean isShowing() {
		return frame.isShowing();
	}
}
