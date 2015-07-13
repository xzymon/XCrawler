package com.xzymon.xcrawler.client.gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourcesList extends JPanel{
	private static Logger logger = LoggerFactory.getLogger("com.xzymon.xcrawler.client.gui.ResourcesList");
	
	private JPanel topP, bottomP;
	private String panelName;
	private JTextField resourceInput;
	private DefaultListModel<String> model;
	private JList<String> list;
	private JScrollPane scroller;
	private JButton addB, removeB;
	
	public ResourcesList(String name){
		super();
		this.panelName = name;
		initComponents();
	}
	
	private void initComponents(){
		this.setLayout(new BorderLayout());
		this.setBorder(new TitledBorder(this.panelName));
		
		topP = new JPanel(new BorderLayout());
		bottomP = new JPanel(new BorderLayout());
		
		resourceInput = new JTextField();
		addB = new JButton(new AddResourceAction());
		model = new DefaultListModel<String>();
		
		list = new JList<String>(model);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(-1);
		
		scroller = new JScrollPane(list);
		
		removeB = new JButton(new RemoveResourceAction());
		
		topP.add(resourceInput, BorderLayout.CENTER);
		topP.add(addB, BorderLayout.EAST);
		
		bottomP.add(removeB, BorderLayout.CENTER);
		
		this.add(topP, BorderLayout.NORTH);
		this.add(scroller, BorderLayout.CENTER);
		this.add(bottomP, BorderLayout.SOUTH);
	}
	
	public List<String> getResources(){
		ArrayList<String> retList = new ArrayList<String>();
		ListModel<String> model = list.getModel();
		int size = model.getSize();
		if(size>0){
			for(int loop=0; loop<size; loop++){
				retList.add(model.getElementAt(loop));
			}
			return retList;
		}
		return null;
	}
	
	public void deactivate(){
		list.setEnabled(false);
		resourceInput.setEnabled(false);
		addB.setEnabled(false);
		removeB.setEnabled(false);
	}
	
	public void activate(){
		list.setEnabled(true);
		resourceInput.setEnabled(true);
		addB.setEnabled(true);
		removeB.setEnabled(true);
	}
	
	class AddResourceAction extends AbstractAction{
		public AddResourceAction(){
			this.putValue(Action.NAME, "+");
		}
		
		public void actionPerformed(ActionEvent e) {
			String txt = resourceInput.getText();
			model.addElement(txt);
			logger.info("::" + panelName + ":: added element: " + txt);
			resourceInput.setText("");
		}
		
	}
	
	class RemoveResourceAction extends AbstractAction{
		public RemoveResourceAction(){
			this.putValue(Action.NAME, "-");
		}
		
		public void actionPerformed(ActionEvent e) {
			int selected;
			if((selected = list.getSelectedIndex())>-1){
				String txt = model.remove(selected);
				logger.info("::" + panelName + ":: removed element: " + txt);
				list.setSelectedIndex(-1);
			}
		}
		
	}

}
