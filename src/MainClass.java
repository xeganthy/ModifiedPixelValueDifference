import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Scanner;

public class MainClass {

	public static void main(String[] args) throws IOException {
		String mainDir = System.getProperty("user.dir");
		System.out.println("Working Directory: "+mainDir);
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter filename: ");
		String fileName = sc.nextLine();
//		String imgDir = "/Users/MARK ANTONIO/Documents/GitHub/ModifiedPixelValueDifference/original_Images/"+fileName+".bmp";	//input
//		String msgDir = "/Users/MARK ANTONIO/Documents/GitHub/ModifiedPixelValueDifference/toEmbed.txt";	//input
		
		String imgDir = mainDir+"/original_Images/"+fileName+".bmp";
		String msgDir = mainDir+"/toEmbed.txt";
		
		BufferedImage image = ImageHelper.getImage(imgDir);							//bmp image
		MessageHelper secretMessage = new MessageHelper("ARC");						//secretMessage ARC
		MessageHelper secretMessage2 = new MessageHelper("KF");						//secretMessage KF ((it's the same))
		int[][] imageGrid = new int[image.getHeight()][image.getWidth()];				//initialize array
		int[][] imageGrid2 = new int[image.getHeight()][image.getWidth()];
		imageGrid = ImageHelper.getImagePixelValues(image, imageGrid); 				//module 2
		imageGrid2 = ImageHelper.getImagePixelValues(image, imageGrid2);
		
		secretMessage.messageToBinary(msgDir);											//module 3
		secretMessage2.messageToBinary(msgDir);											//module 3
		
		System.out.println("embedding arc");												//EMBEDDING
		ARC_Algo arc = new ARC_Algo(imageGrid, secretMessage);						//Arc Algo
		arc.embedImage(fileName+"_arc");
		System.out.println("embedding khoez");
		KhodFaez_Algo khodFaez = new KhodFaez_Algo(imageGrid2, secretMessage2);
		khodFaez.embedImage(fileName+"_khoez");
		
//		System.out.println("Enter filename of arc image:");
//		String arcFile = sc.nextLine();
//		System.out.println("Enter filename of kf image:");
//		String kfFile = sc.nextLine();
		
		String stgImgDirARC = mainDir+"/stego_Images/"+fileName+"_arc.bmp";
		String stgImgDirKF = mainDir+"/stego_Images/"+fileName+"_khoez.bmp";
//		String stgImgDirARC = "/Users/MARK ANTONIO/Documents/GitHub/ModifiedPixelValueDifference/stego_Images/"+arcFile+".bmp"; 	//ARC STEGO IMG
//		String stgImgDirKF = 	"/Users/MARK ANTONIO/Documents/GitHub/ModifiedPixelValueDifference/stego_Images/"+kfFile+".bmp";	//KF STEGO IMG
		
		
		BufferedImage arcStegoImage = ImageHelper.getImage(stgImgDirARC);
		BufferedImage kFStegoImage = ImageHelper.getImage(stgImgDirKF);
		
//		System.out.println("Enter filename of arc text file:");
//		String arcMessage = sc.nextLine();
//		System.out.println("Enter filename of kf text file:");
//		String kfMessage =sc.nextLine();
		
		System.out.println("extracting arc");
		arc.extractMessage(arcStegoImage, "ARC", fileName+"_arc");
		System.out.println();
		System.out.println("extracting khoez");
		khodFaez.extractMessage(kFStegoImage, "KF",fileName+"_khoez");
		System.out.println("END");
	}

}
