import java.awt.image.BufferedImage;
import java.io.IOException;

public class MainClass {

	public static void main(String[] args) throws IOException {
		String imgDir = "/home/renzo/git/ModifiedPixelValueDifference/lena_gray.bmp";	//input
		String msgDir = "/home/renzo/git/ModifiedPixelValueDifference/toEmbed.txt";	//input
		BufferedImage image = ImageHelper.getImage(imgDir);				//bmp image
		MessageHelper secretMessage = new MessageHelper();				//secretMessage
		int[][] imageGrid = new int[image.getHeight()][image.getWidth()];	//initialize array
		imageGrid = ImageHelper.getImagePixelValues(image, imageGrid); 	//module 2
		secretMessage.messageToBinary(msgDir); 								//module 3
		
		ARC_Algo arc = new ARC_Algo(imageGrid, secretMessage);			//Arc Algo
		arc.embedImage();
		
		BufferedImage arcStegoImage = ImageHelper.getImage("/home/renzo/git/ModifiedPixelValueDifference/ARCStegoImage.bmp");
		arc.extractMessage(arcStegoImage);
	}

}
