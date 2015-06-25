package com.xzymon.xcrawler.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.naming.NamingException;
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
import javax.swing.JTextPane;
import javax.swing.KeyStroke;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.xzymon.xcrawler.client.CrawlerCommunicator;

public class ExecutorFrame extends JFrame {

	private static final Logger logger = LoggerFactory.getLogger(ExecutorFrame.class.getName());
	
	private CrawlerCommunicator communicator;
	
	private JMenuBar menuBar;
    private JMenu fileM;
    
    private JPanel executorP;
    private JPanel inputEP;
    private JTextField urlTF;
    private JButton executeB;
    private JScrollPane responseSP;
    private JTextPane responseTP;
    
    private JPanel testerP;
    private JTextField findTF;
    private JPanel buttonsP;
    private JScrollPane testerSP;
    private JTextPane testerTP;
    private JButton findNextB;
    
    private GridBagConstraints gbcLeft, gbcRight;
    
    public ExecutorFrame() throws NamingException{
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
        
        createExecutorPanel();
        createTesterPanel();
        
        GridLayout layout = new GridLayout(1,2);
        this.setLayout(layout);
        this.add(executorP);
        this.add(testerP);
        
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
    
    private void createExecutorPanel(){
    	executorP = new JPanel(new BorderLayout());
    	inputEP = new JPanel(new GridBagLayout());
    	urlTF = new JTextField(20);
    	executeB = new JButton(new CrawlAction());
    	responseTP = new JTextPane();
    	responseTP.setAutoscrolls(true);
    	responseSP = new JScrollPane(responseTP);
    	
        inputEP.add(urlTF, gbcLeft);
        inputEP.add(executeB, gbcRight);
        
        executorP.add(inputEP, BorderLayout.NORTH);
        executorP.add(responseSP, BorderLayout.CENTER);
    }
    
    private void createTesterPanel(){
    	testerP = new JPanel(new BorderLayout());
    	buttonsP = new JPanel(new GridBagLayout());
    	findTF = new JTextField(20);
    	testerTP = new JTextPane();
    	testerTP.setAutoscrolls(true);
    	testerSP = new JScrollPane(testerTP);
    	findNextB = new JButton(new FindNextAction());
        
        buttonsP.add(findNextB, gbcLeft);
        buttonsP.add(findTF, gbcRight);
        
        testerP.add(buttonsP, BorderLayout.NORTH);
        testerP.add(testerSP, BorderLayout.CENTER);
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
    
    class FindNextAction extends AbstractAction{
    	
		private static final long serialVersionUID = 7826979399351818553L;

		public FindNextAction(){
    		putValue(Action.NAME, "Find Next");
    	}

		public void actionPerformed(ActionEvent e) {
			String textToInsert = findTF.getText();
			
			StyledDocument styled = testerTP.getStyledDocument();
			
			SimpleAttributeSet aset = new SimpleAttributeSet();
			StyleConstants.setForeground(aset, Color.RED);
			StyleConstants.setBackground(aset, Color.BLUE);
			StyleConstants.setItalic(aset, true);
			
			try{
				styled.insertString(styled.getLength(), textToInsert, aset);
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {
				
			}
			
		}
    }
    
    class StainTextAction extends AbstractAction{

		private static final long serialVersionUID = -6601907756795734542L;

		public StainTextAction(){
			putValue(Action.NAME, "Stain Text");
		}
		
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			StyledDocument doc = testerTP.getStyledDocument();
			/*
			SimpleAttributeSet aset = new SimpleAttributeSet();
			StyleConstants.
			*/
		}
    	
    }
    
    private void parseAndColor(String str, JTextPane tp){
    	Parser parser = Parser.xmlParser();
    	
    	org.jsoup.nodes.Document doc = parser.parseInput(str, null);
    	
    	//doc.
    	
    	//tp.getDocument().
    	//tp.setText(result);
    }
    
    private void build(String str){
    	try{
	    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	    	DocumentBuilder builder = factory.newDocumentBuilder();
	    	InputStream stringAsStream = new ByteArrayInputStream(str.getBytes(Charset.forName("UTF-8")));
	    	Document xmlDoc = builder.parse(stringAsStream);
	    	
	    	XPath xPath = XPathFactory.newInstance().newXPath();
	    	String expression = ""; //XPath expression here
	    	
	    	//get result as String
	    	String someValue = (String) xPath.compile(expression).evaluate(xmlDoc, XPathConstants.STRING);
	    	//get result as Double
	    	Double number = (Double) xPath.compile(expression).evaluate(xmlDoc, XPathConstants.NUMBER);
	    	//get result as a list of nodes
	    	NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(xmlDoc, XPathConstants.NODESET);
	    	
	    	DOMSource domSource = new DOMSource(xmlDoc);
	    	StringWriter writer = new StringWriter();
	    } catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XPathExpressionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	finally{
    		
    	}
    }
}
