import java.awt.image.BufferedImage;
import java.io.IOException;

public class MainClass {

	public static void main(String[] args) throws IOException {
		String imgDir = "/home/renzo/git/ModifiedPixelValueDifference/lena_gray.bmp";	//input
		String msgDir = "/home/renzo/git/ModifiedPixelValueDifference/toEmbed.txt";	//input
		BufferedImage image = ImageHelper.getImage(imgDir);							//bmp image
		MessageHelper secretMessage = new MessageHelper("ARC");						//secretMessage
		MessageHelper secretMessage2 = new MessageHelper("KF");
		int[][] imageGrid = new int[image.getHeight()][image.getWidth()];				//initialize array
		imageGrid = ImageHelper.getImagePixelValues(image, imageGrid); 				//module 2
		secretMessage.messageToBinary(msgDir); 											//module 3
		secretMessage2.messageToBinary(msgDir);
		
		ARC_Algo arc = new ARC_Algo(imageGrid, secretMessage);						//Arc Algo
		arc.embedImage();
		
		KhodFaez_Algo khodFaez = new KhodFaez_Algo(imageGrid, secretMessage2);
		khodFaez.embedImage();
		
		BufferedImage arcStegoImage = ImageHelper.getImage("/home/renzo/git/ModifiedPixelValueDifference/ARCStegoImage.bmp");
		BufferedImage kFStegoImage = ImageHelper.getImage("/home/renzo/git/ModifiedPixelValueDifference/KFStegoImage.bmp");
		
		arc.extractMessage(arcStegoImage, "ARC");
		khodFaez.extractMessage(kFStegoImage, "KF");
	}

}
