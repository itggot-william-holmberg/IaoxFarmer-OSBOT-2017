package com.iaox.farmer.ai;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollBar;

public class AIFrame extends JFrame {

	private JPanel contentPane;
	private JTextArea textArea;

	

	/**
	 * Create the frame.
	 */
	public AIFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		textArea = new JTextArea();
		textArea.setBounds(10, 11, 414, 193);
		contentPane.add(textArea);
	}
	
	public void newMessage(String string){
		textArea.append(string);
	}
}
