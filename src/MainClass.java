import java.awt.image.BufferedImage;
import java.io.IOException;

public class MainClass {

	public static void main(String[] args) throws IOException {
		String imgDir = "/home/renzo/git/ModifiedPixelValueDifference/lena_gray.bmp";	//input
		String msgDir = "/home/renzo/git/ModifiedPixelValueDifference/toEmbed.txt";	//input
		BufferedImage image = ImageHelper.getImage(imgDir);							//bmp image
		MessageHelper secretMessage = new MessageHelper("ARC");						//secretMessage ARC
		MessageHelper secretMessage2 = new MessageHelper("KF");						//secretMessage KF ((it's the same))
		int[][] imageGrid = new int[image.getHeight()][image.getWidth()];				//initialize array
		imageGrid = ImageHelper.getImagePixelValues(image, imageGrid); 				//module 2
		
		secretMessage.messageToBinary(msgDir);											//module 3
		secretMessage2.messageToBinary(msgDir);											//module 3
		
		System.out.println("embedding");												//EMBEDDING
		ARC_Algo arc = new ARC_Algo(imageGrid, secretMessage);						//Arc Algo
		arc.embedImage();
		KhodFaez_Algo khodFaez = new KhodFaez_Algo(imageGrid, secretMessage2);
		khodFaez.embedImage();
		
		String stgImgDirARC = "/home/renzo/git/ModifiedPixelValueDifference/ARCStegoImage.bmp"; 	//ARC STEGO IMG
		String stgImgDirKF = 	"/home/renzo/git/ModifiedPixelValueDifference/KFStegoImage.bmp";	//KF STEGO IMG
		BufferedImage arcStegoImage = ImageHelper.getImage(stgImgDirARC);
		BufferedImage kFStegoImage = ImageHelper.getImage(stgImgDirKF);

		System.out.println("extracting");
		arc.extractMessage(arcStegoImage, "ARC");
//		khodFaez.extractMessage(kFStegoImage, "KF");
		System.out.println("END");
	}

}
