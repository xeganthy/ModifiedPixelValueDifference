import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Scanner;

public class MainClass {

	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter filename: ");
		String fileName = sc.nextLine();
//		String imgDir = "/Users/MARK ANTONIO/Documents/GitHub/ModifiedPixelValueDifference/original_Images/"+fileName+".bmp";	//input
//		String msgDir = "/Users/MARK ANTONIO/Documents/GitHub/ModifiedPixelValueDifference/toEmbed.txt";	//input
		
		String imgDir = "/home/renzo/git/ModifiedPixelValueDifference/original_Images/"+fileName+".bmp";
		String msgDir = "/home/renzo/git/ModifiedPixelValueDifference/toEmbed.txt";
		
		BufferedImage image = ImageHelper.getImage(imgDir);							//bmp image
		MessageHelper secretMessage = new MessageHelper("ARC");						//secretMessage ARC
		MessageHelper secretMessage2 = new MessageHelper("KF");						//secretMessage KF ((it's the same))
		int[][] imageGrid = new int[image.getHeight()][image.getWidth()];				//initialize array
		int[][] imageGrid2 = new int[image.getHeight()][image.getWidth()];
		imageGrid = ImageHelper.getImagePixelValues(image, imageGrid); 				//module 2
		imageGrid2 = ImageHelper.getImagePixelValues(image, imageGrid2);
		
		secretMessage.messageToBinary(msgDir);											//module 3
		secretMessage2.messageToBinary(msgDir);											//module 3
		
		System.out.println("embedding");												//EMBEDDING
		ARC_Algo arc = new ARC_Algo(imageGrid, secretMessage);						//Arc Algo
		arc.embedImage();
		
		KhodFaez_Algo khodFaez = new KhodFaez_Algo(imageGrid2, secretMessage2);
		khodFaez.embedImage();
		
//		String stgImgDirARC = "/Users/MARK ANTONIO/Documents/GitHub/ModifiedPixelValueDifference/ARCStegoImage.bmp"; 	//ARC STEGO IMG
//		String stgImgDirKF = 	"/Users/MARK ANTONIO/Documents/GitHub/ModifiedPixelValueDifference/KFStegoImage.bmp";	//KF STEGO IMG
//		BufferedImage arcStegoImage = ImageHelper.getImage(stgImgDirARC);
//		BufferedImage kFStegoImage = ImageHelper.getImage(stgImgDirKF);
//
//		System.out.println("extracting");
//		arc.extractMessage(arcStegoImage, "ARC");
//		khodFaez.extractMessage(kFStegoImage, "KF");
		System.out.println("END");
	}

}
