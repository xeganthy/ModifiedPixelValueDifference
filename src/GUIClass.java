import java.awt.EventQueue;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import java.awt.Font;


public class GUIClass extends javax.swing.JFrame {

	public JFrame embeddingFrame;
	private JTextField inputImageString;
	private JTextField inputTextString;
	private JButton inputTextButton;
	private JTextField outputImageString;
	private JButton outputImageButton;
	private JTextField outputTextString;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUIClass window = new GUIClass();
					window.embeddingFrame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUIClass() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		embeddingFrame = new JFrame();
		embeddingFrame.getContentPane().setFont(new Font("Tahoma", Font.PLAIN, 12));
		embeddingFrame.setVisible(true);

		embeddingFrame.setBounds(10, 10, 697, 329);
		embeddingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		embeddingFrame.getContentPane().setLayout(null);
		
		inputImageString = new JTextField();
		inputImageString.setBounds(136, 81, 414, 23);
		embeddingFrame.getContentPane().add(inputImageString);
		inputImageString.setColumns(10);
		
		JButton inputImageButton = new JButton("Explore...");
		inputImageButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		
		inputImageButton.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				
				JFileChooser fc = new JFileChooser();
				FileNameExtensionFilter filter = new FileNameExtensionFilter("BMP Image", "bmp"); //FILTERS THE TYPE OF FILES THAT CAN ONLY BE OPENED
					fc.setFileFilter(filter);
				fc.setDialogTitle("Hello");
				fc.setCurrentDirectory(new java.io.File(System.getProperty("user.dir"))); //PUT THE DIRECTORY PATH HERE
				fc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int returnVal = fc.showOpenDialog(inputImageButton);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
				   inputImageString.setText(fc.getSelectedFile().getAbsolutePath()); //ETO YUNG KUKUHA NUNG FILE NAME + PATH NAME
				}
			}
		});
		inputImageButton.setBounds(562, 81, 105, 23);
		embeddingFrame.getContentPane().add(inputImageButton);
		
		inputTextString = new JTextField();
		inputTextString.setColumns(10);
		inputTextString.setBounds(136, 115, 414, 23);
		embeddingFrame.getContentPane().add(inputTextString);
		
		inputTextButton = new JButton("Explore...");
		inputTextButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		inputTextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fs = new JFileChooser();
				FileNameExtensionFilter filterText = new FileNameExtensionFilter("Text Files", "txt");
				fs.setFileFilter(filterText);
				fs.setDialogTitle("Hello");
				fs.setCurrentDirectory(new java.io.File(System.getProperty("user.dir")));
				fs.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				int returnVal = fs.showOpenDialog(inputTextButton);
				if(returnVal == JFileChooser.APPROVE_OPTION) {
					inputTextString.setText( fs.getSelectedFile().getAbsolutePath());
				   
			}
			}
		});
		inputTextButton.setBounds(562, 115, 105, 23);
		embeddingFrame.getContentPane().add(inputTextButton);
		
		outputImageString = new JTextField();
		outputImageString.setColumns(10);
		outputImageString.setBounds(136, 149, 414, 23);
		embeddingFrame.getContentPane().add(outputImageString);
		
		outputImageButton = new JButton("Explore...");
		outputImageButton.setFont(new Font("Tahoma", Font.PLAIN, 12));
		outputImageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fs = new JFileChooser();
				fs.setCurrentDirectory(new java.io.File(System.getProperty("user.dir")));//lagay mo nalang dito kung saan isasave yung pics
				fs.setAcceptAllFileFilterUsed(false);
		        fs.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
		        fs.setDialogTitle("Save to");
		        if (fs.showOpenDialog(outputImageButton) == JFileChooser.APPROVE_OPTION) { 
		            outputImageString.setText(fs.getSelectedFile().getAbsolutePath());
		            }
			}
		});
		outputImageButton.setBounds(562, 150, 105, 23);
		embeddingFrame.getContentPane().add(outputImageButton);
		
		JLabel lblNewLabel = new JLabel("STEGANOGRAPHY");
		lblNewLabel.setBounds(273, 32, 136, 14);
		embeddingFrame.getContentPane().add(lblNewLabel);
		
		outputTextString = new JTextField();
		outputTextString.setColumns(10);
		outputTextString.setBounds(136, 184, 414, 23);
		embeddingFrame.getContentPane().add(outputTextString);
		
		JButton outputTextButton = new JButton("Explore...");
		outputTextButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser fs = new JFileChooser();
				fs.setCurrentDirectory(new java.io.File(System.getProperty("user.dir")));//lagay mo nalang dito kung saan isasave yung pics
				fs.setAcceptAllFileFilterUsed(false);
		        fs.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY); 
		        fs.setDialogTitle("Save to");
		        if (fs.showOpenDialog(outputImageButton) == JFileChooser.APPROVE_OPTION) { 
		        	outputTextString.setText(fs.getSelectedFile().getAbsolutePath());
		            }
			}
		});
		outputTextButton.setFont(new Font("Dialog", Font.PLAIN, 12));
		outputTextButton.setBounds(562, 185, 105, 23);
		embeddingFrame.getContentPane().add(outputTextButton);
		
		JLabel lblCoverImage = new JLabel("Cover Image");
		lblCoverImage.setBounds(12, 85, 121, 19);
		embeddingFrame.getContentPane().add(lblCoverImage);
		
		JLabel lblSecretMessage = new JLabel("Secret Message");
		lblSecretMessage.setBounds(12, 119, 121, 19);
		embeddingFrame.getContentPane().add(lblSecretMessage);
		
		JLabel lblStegoImage = new JLabel("Stego Image");
		lblStegoImage.setBounds(12, 153, 121, 19);
		embeddingFrame.getContentPane().add(lblStegoImage);
		
		JLabel lblExtractedMessage = new JLabel("Extracted Message");
		lblExtractedMessage.setBounds(12, 185, 121, 19);
		embeddingFrame.getContentPane().add(lblExtractedMessage);
		
		JButton btnProcess = new JButton("PROCESS");				//caller
		btnProcess.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println(inputImageString.getText());
				System.out.println(inputTextString.getText());
				System.out.println(outputImageString.getText());
				System.out.println(outputTextString.getText());
				try {
					String imgDir = inputImageString.getText();
					String msgDir = inputTextString.getText();
					String outputImgDir = outputImageString.getText()+"/";
					String outputTextDir = outputTextString.getText()+"/";
					
					
					File f = new File(imgDir);
					String gg = f.getName();
					String[] split = gg.split(Pattern.quote("."));
					String imgFileName = split[0];												//0 = filename, 1 = extension
					System.out.println(imgFileName);
					BufferedImage image = ImageHelper.getImage(imgDir);							//bmp image
					MessageHelper secretMessage = new MessageHelper("ARC");						//secretMessage ARC
					MessageHelper secretMessage2 = new MessageHelper("KF");						//secretMessage KF ((it's the same))
					
					int[][] imageGrid = new int[image.getHeight()][image.getWidth()];				//initialize array for 
					int[][] imageGrid2 = new int[image.getHeight()][image.getWidth()];			//image grid
					
					imageGrid = ImageHelper.getImagePixelValues(image, imageGrid); 				//module 2
					imageGrid2 = ImageHelper.getImagePixelValues(image, imageGrid2);
					
					secretMessage.messageToBinary(msgDir);											//module 3
					secretMessage2.messageToBinary(msgDir);											//module 3
					
					System.out.println("embedding arc");											//EMBEDDING START
					ARC_Algo arc = new ARC_Algo(imageGrid, secretMessage);						//Arc Algo
					arc.embedImage(imgFileName+"_arc", outputImgDir);
					
					System.out.println("embedding khoez");
					KhodFaez_Algo khodFaez = new KhodFaez_Algo(imageGrid2, secretMessage2);
					khodFaez.embedImage(imgFileName+"_khoez", outputImgDir);											//EMBEDDING END
					
					String stgImgDirARC = outputImgDir+imgFileName+"_arc.bmp";
					String stgImgDirKF = outputImgDir+imgFileName+"_khoez.bmp";
					
					
					BufferedImage arcStegoImage = ImageHelper.getImage(stgImgDirARC);
					BufferedImage kFStegoImage = ImageHelper.getImage(stgImgDirKF);
					
					System.out.println("extracting arc");
					arc.extractMessage(arcStegoImage, "ARC", imgFileName+"_arc", outputTextDir);
					System.out.println();
					System.out.println("extracting khoez");
					khodFaez.extractMessage(kFStegoImage, "KF",imgFileName+"_khoez", outputTextDir);
					System.out.println("END");
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		});
		
		btnProcess.setBounds(568, 237, 99, 42);
		embeddingFrame.getContentPane().add(btnProcess);
	}
}

