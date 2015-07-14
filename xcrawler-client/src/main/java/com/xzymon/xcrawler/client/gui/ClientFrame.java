package com.xzymon.xcrawler.client.gui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.naming.NamingException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xzymon.xcrawler.client.CrawlerCommunicator;
import com.xzymon.xcrawler.util.CrawlingPolicy;
import com.xzymon.xcrawler.util.CrawlingPolicyImpl;
import com.xzymon.xcrawler.util.StatusReport;

public class ClientFrame extends JFrame {
	private static final Logger logger = LoggerFactory.getLogger(ClientFrame.class.getName());
	
	private JMenuBar menuBar;
	private JMenu fileM;
	
	private Long runId;
	
	private JPanel startP, startGridP;
	private JPanel inputP;
	private JPanel statusReportP;
	
	private JLabel report_milisL, report_branchesL, report_leavesL;
	private String NO_REPORT_STRING = "No report up to now. /nWait for report";
	
	private JTextField rootUrlTF;
	private JSpinner delaySp, maxLimitSp, statusReportSp;
	private ResourcesList branchRL, leafRL;
	
	private JButton startB;
	
	private JLabel label;
	
	private Timer timer;
	
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
        
        this.setLayout(new BorderLayout());
        
        startP = new JPanel(new BorderLayout());
        startGridP = new JPanel(new GridLayout(3,1));
        
        inputP = new JPanel(new GridBagLayout());
        JLabel rootL = new JLabel("Root URL:");
        JLabel delayL = new JLabel("Delay duration [ms]:");
        JLabel maxL = new JLabel("Max Limit:");
        JLabel statusL = new JLabel("Status Report Interval [s]");
        rootUrlTF = new JTextField();
        delaySp = new JSpinner(new SpinnerNumberModel(100, 10, 10000, 10));
        maxLimitSp = new JSpinner(new SpinnerNumberModel(10, -1, Integer.MAX_VALUE, 1));
        statusReportSp = new JSpinner(new SpinnerNumberModel(1, -1, 100, 1));
        GridBagConstraints gbcLeft = new GridBagConstraints();
        gbcLeft.gridx = 0;
        gbcLeft.anchor = GridBagConstraints.LINE_END;
        GridBagConstraints gbcRight = new GridBagConstraints();
        gbcRight.gridx = 1;
        gbcRight.fill = GridBagConstraints.HORIZONTAL;
        
        gbcLeft.gridy = 0;
        gbcRight.gridy = 0;
        inputP.add(rootL, gbcLeft);
        inputP.add(rootUrlTF, gbcRight);
        gbcLeft.gridy = 1;
        gbcRight.gridy = 1;
        inputP.add(delayL, gbcLeft);
        inputP.add(delaySp, gbcRight);
        gbcLeft.gridy = 2;
        gbcRight.gridy = 2;
        inputP.add(maxL, gbcLeft);
        inputP.add(maxLimitSp, gbcRight);
        gbcLeft.gridy = 3;
        gbcRight.gridy = 3;
        inputP.add(statusL, gbcLeft);
        inputP.add(statusReportSp, gbcRight);
        
        startB = new JButton(new CrawlAction());
        
        branchRL = new ResourcesList("Branch");
        leafRL = new ResourcesList("Leaf");
        
        startGridP.add(inputP, 0);
        startGridP.add(branchRL, 1);
        startGridP.add(leafRL, 2);
        
        startP.add(startGridP, BorderLayout.CENTER);
        startP.add(startB, BorderLayout.SOUTH);
        
        statusReportP = new JPanel(new GridLayout(5,1));
        report_branchesL = new JLabel(NO_REPORT_STRING);
        
        report_milisL = new JLabel(NO_REPORT_STRING);
        report_branchesL = new JLabel();
        report_leavesL = new JLabel();
        
        statusReportP.add(report_milisL, 0);
        statusReportP.add(report_branchesL, 1);
        statusReportP.add(report_leavesL, 2);
        
        this.add(startP, BorderLayout.CENTER);
        this.add(statusReportP, BorderLayout.SOUTH);
        
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
					new ClientFrame().setVisible(true);
				} catch (NamingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            
        });
    }
    
    private boolean isFormValid(){
    	String rootUrl = rootUrlTF.getText();
    	if(rootUrl!=null && rootUrl!="" && /*branchRL.getResources()!=null && */ leafRL.getResources()!=null){
    		return true;
    	}
    	return false;
    }
    
    private void lock(){
    	rootUrlTF.setEnabled(false);
    	delaySp.setEnabled(false);
    	maxLimitSp.setEnabled(false);
    	branchRL.deactivate();
    	leafRL.deactivate();
    }
    
    private void unlock(){
    	rootUrlTF.setEnabled(true);
    	delaySp.setEnabled(true);
    	maxLimitSp.setEnabled(true);
    	branchRL.activate();
    	leafRL.activate();
    }
    
    private void cancel(){
    	
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
			putValue(Action.NAME, "Start");
		}
		
		public void actionPerformed(ActionEvent e) {
			if(isFormValid()){
				CrawlingPolicy policy = new CrawlingPolicyImpl();
				policy.setRootURL(rootUrlTF.getText());
				policy.setTriggerTimeout(((Integer)delaySp.getValue()).longValue());
				policy.setMaxRetriesPerResource((Integer)maxLimitSp.getValue());
				policy.setBranchSpec(branchRL.getResources());
				policy.setLeafSpec(leafRL.getResources());
				
				lock();
				startB.setAction(new CancelAction());
				
				runId = communicator.crawl(policy);
				
				timer = new Timer();
				timer.scheduleAtFixedRate(new TimerTask(){

					@Override
					public void run() {
						StatusReport report = communicator.getStatusReport();
						report_milisL.setText(String.format("miliseconds from start: %1$d", report.getMilisFromStart()));
						report_branchesL.setText(String.format("Branches: set triggers: %1$d, successfully downloaded: %2$d", report.getBranchResourceDownloadTriggersCount(), report.getSuccessfullyDownloadedBranchResourcesCount()));
						report_leavesL.setText(String.format("Leaves: set triggers: %1$d, successfully downloaded %2$d", report.getLeafResourceDownloadTriggersCount(), report.getSuccessfullyDownloadedLeafResourcesCount()));
					}
					
				}, new Date(), ((Integer)statusReportSp.getValue()).longValue() * 1000);
			}
		}
    	
    }
    
    class CancelAction extends AbstractAction{
    	public CancelAction(){
    		putValue(Action.NAME, "Cancel");
    	}

		public void actionPerformed(ActionEvent e) {
			unlock();
		}
    	
    }
}
