package com.xzymon.xcrawler.client.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.naming.NamingException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xzymon.xcrawler.client.CrawlerCommunicator;
import com.xzymon.xcrawler.client.gui.ExecutorFrame.CloseAction;

public class ClientFrame extends JFrame {
	private static final Logger logger = LoggerFactory.getLogger(ClientFrame.class.getName());
	
	private JMenuBar menuBar;
	private JMenu fileM;
	
	private JPanel communicatorP, runP, startP, branchP, leafP, intervalP, statusP;
	
	private JLabel label;
	
	private GridBagConstraints gbcLeft, gbcRight;
	private CrawlerCommunicator communicator;
    
    public ClientFrame() throws NamingException{
    	communicator = new CrawlerCommunicator();
    	initComponents();
    }
    
    private void initComponents() {

    	initGBCs();
    	
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        menuBar = new JMenuBar();
        fileM = new JMenu("File");
        fileM.add(new CloseAction());

        menuBar.add(fileM);
        this.setJMenuBar(menuBar);
        
        //createExecutorPanel();
        //createTesterPanel();
        
        GridLayout layout = new GridLayout(1,2);
        this.setLayout(layout);
        //this.add();
        //this.add();
        
        communicatorP = new JPanel(new BorderLayout());
        startP = new JPanel(new BorderLayout());
        runP = new JPanel();
        
        statusP = new JPanel(new GridLayout(5,1));
        
        
        pack();
    }
    
    private void initGBCs(){
    	gbcLeft = new GridBagConstraints();
        gbcLeft.anchor = GridBagConstraints.LINE_START;
        gbcLeft.fill = GridBagConstraints.HORIZONTAL;
        gbcLeft.gridx = 0;
        gbcLeft.gridy = 0;
        gbcRight = new GridBagConstraints();
        gbcRight.anchor = GridBagConstraints.LINE_END;
        gbcRight.fill = GridBagConstraints.HORIZONTAL;
        gbcRight.gridx = 1;
        gbcRight.gridy = 0;
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
                try {
					new ExecutorFrame().setVisible(true);
				} catch (NamingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
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
			//<>
			//String result = communicator.crawl(urlTF.getText());
			//responseTP.setText(result);
			//</>
			
			//parseAndColor(result, responseTA);
		}
    	
    }
}
