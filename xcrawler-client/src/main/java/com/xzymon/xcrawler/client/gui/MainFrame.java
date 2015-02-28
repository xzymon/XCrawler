package com.xzymon.xcrawler.client.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Properties;

import javax.naming.NamingException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.KeyStroke;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xzymon.xcrawler.client.embryo.EJBsInvocator;


public class MainFrame extends JFrame {

	private static final Logger logger = LoggerFactory.getLogger(MainFrame.class.getName());

    private final String format = "dd.MM.yyyy";
    private final SimpleDateFormat dateFormat = new SimpleDateFormat(format);
    private final ParsePosition prspos = new ParsePosition(0);

    private final String PROPS_DEF_LOCATION = "/default.properties";
    private final Properties prop;
    
    private JMenuBar menuBar;
    private JMenu fileM;
    
    private JScrollPane tableScrollPane;
    private JTable table;
    
    private JTextField textField;
    private JButton button;
    private JLabel label1;
    private JLabel label2;
    
    private EJBsInvocator invocator;
	
	public MainFrame() {
        prop = loadProperties();
        
        invocator = new EJBsInvocator();
        
        initComponents();
    }

    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        menuBar = new JMenuBar();
        fileM = new JMenu("File");
        fileM.add(new CloseAction());

        menuBar.add(fileM);
        this.setJMenuBar(menuBar);
        
        /*
        table = new JTable(new DvdTableModel(this));
        tableScrollPane = new JScrollPane(table);
        
        table.setFillsViewportHeight(true);
        this.add(tableScrollPane);
        */
        
        JPanel panel = new JPanel(new GridBagLayout());
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
        GridBagConstraints gbcSparse = new GridBagConstraints();
        gbcSparse.anchor = GridBagConstraints.LINE_START;
        gbcSparse.fill = GridBagConstraints.HORIZONTAL;
        gbcSparse.gridx = 0;
        gbcSparse.gridy = 1;
        gbcSparse.gridwidth = 2;
        
        textField = new JTextField(10);
        panel.add(textField, gbcLeft);
        button = new JButton(new InvokeEJBsAction());
        panel.add(button, gbcRight);
        label1 = new JLabel();
        panel.add(label1, gbcSparse);
        label2 = new JLabel();
        gbcSparse.gridy = 2;
        panel.add(label2, gbcSparse);
        this.add(panel);
        
        pack();
    }// </editor-fold>                
	
	public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
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
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainFrame().setVisible(true);
            }
        });
    }
	
	public Properties loadProperties() {
        Properties result = new Properties();
        try {
            result.load(MainFrame.class.getResourceAsStream(PROPS_DEF_LOCATION));
        } catch (IOException ex) {
        	logger.info(ex.getMessage());
        }
        printProperties(result);
        return result;
    }
	
	private void printProperties(Properties properties){
        for (String s : properties.stringPropertyNames()) {
            logger.info(String.format("Property %1$s=%2$s%n", s, properties.getProperty(s)));
        }
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
	
	class InvokeEJBsAction extends AbstractAction{

		private static final long serialVersionUID = -7694841052607298459L;

		public InvokeEJBsAction(){
			putValue(Action.NAME, "Invoke EJBs");
		}
		
		public void actionPerformed(ActionEvent e) {
			logger.info("Invoking EJBs");
			try {
				String result1 = invocator.invokeStateful(textField.getText());
				String result2 = invocator.invokeStateless(textField.getText());
				label1.setText(String.format("%1$s", result1));
				label2.setText(String.format("%1$s", result2));
			} catch (NamingException e1) {
				e1.printStackTrace();
			}
		}
		
	}
}
