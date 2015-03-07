package com.xzymon.xcrawler.client.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xzymon.xcrawler.client.CrawlerCommunicator;

public class ExecutorFrame extends JFrame {

	private static final Logger logger = LoggerFactory.getLogger(ExecutorFrame.class.getName());
	
	private CrawlerCommunicator communicator;
	
	private JMenuBar menuBar;
    private JMenu fileM;
    
    private JPanel executorP;
    private JPanel inputP;
    private JTextField urlTF;
    private JButton executeB;
    private JScrollPane scrollPane;
    private JTextArea responseTA;
    
    public ExecutorFrame(){
    	communicator = new CrawlerCommunicator();
    	initComponents();
    }
    
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        menuBar = new JMenuBar();
        fileM = new JMenu("File");
        fileM.add(new CloseAction());

        menuBar.add(fileM);
        this.setJMenuBar(menuBar);
        
        createExecutorPanel();
        this.add(executorP);
        
        pack();
    }
    
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            logger.info(ex.getMessage());
        } catch (InstantiationException ex) {
        	logger.info(ex.getMessage());
        } catch (IllegalAccessException ex) {
        	logger.info(ex.getMessage());
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
        	logger.info(ex.getMessage());
        }
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ExecutorFrame().setVisible(true);
            }
        });
    }
    
    private void createExecutorPanel(){
    	executorP = new JPanel(new BorderLayout());
    	inputP = new JPanel(new GridBagLayout());
    	urlTF = new JTextField(20);
    	executeB = new JButton(new CrawlAction());
    	responseTA = new JTextArea();
    	responseTA.setAutoscrolls(true);
    	scrollPane = new JScrollPane(responseTA);
    	
    	GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.anchor = GridBagConstraints.LINE_START;
        gbcLeft.fill = GridBagConstraints.HORIZONTAL;
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 0;
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.anchor = GridBagConstraints.LINE_END;
        gbcRight.fill = GridBagConstraints.HORIZONTAL;
        gbcRight.gridx = 1;
        gbcRight.gridy = 0;
        inputP.add(urlTF, gbcLeft);
        inputP.add(executeB, gbcRight);
        
        executorP.add(inputP, BorderLayout.NORTH);
        executorP.add(scrollPane, BorderLayout.CENTER);
        
    }
    
    class CloseAction extends AbstractAction{

        private static final long serialVersionUID = -7789819639153350425L;

		public CloseAction(){
            putValue(Action.NAME, "Close");
            putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F4, KeyEvent.ALT_DOWN_MASK));
        }
        
        public void actionPerformed(ActionEvent e) {
            logger.info("Bye!");
            System.exit(0);
        }
        
    }
    
    class CrawlAction extends AbstractAction{

		private static final long serialVersionUID = 1917611653336592043L;

		public CrawlAction(){
			putValue(Action.NAME, "Crawl");
		}
		
		public void actionPerformed(ActionEvent e) {
			String result = communicator.crawl(urlTF.getText());
			responseTA.setText(result);
		}
    	
    }
}
