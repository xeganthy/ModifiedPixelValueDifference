import java.awt.image.BufferedImage;
import java.util.Scanner;

public class MetricsCalculator {
	
	public static double getPSNR(String coverDir, String stegoDir){
		BufferedImage coverImage = ImageHelper.getImage(coverDir);
		BufferedImage stegoImage = ImageHelper.getImage(stegoDir);
		
		int[][] coverGrid = new int[coverImage.getHeight()][coverImage.getWidth()];
		int[][] stegoGrid = new int[stegoImage.getHeight()][coverImage.getWidth()];
		
		coverGrid = ImageHelper.getImagePixelValues(coverImage, coverGrid);
		stegoGrid = ImageHelper.getImagePixelValues(stegoImage, stegoGrid);
		
		int mse = 0;
		int sum = 0;
		for(int i = 0; i < coverGrid.length; i++) {
			for(int j = 0; j < coverGrid[0].length; j++) {
				sum += Math.pow(coverGrid[i][j]-stegoGrid[i][j], 2);
//				System.out.println(origImage[i][j] + "  " + stegoImage[i][j]);
			}
		}
		mse = sum/(coverGrid.length * coverGrid[0].length);
		double PSNR = 10 * (Math.log((Math.pow(255, 2)/mse)));
		
		return PSNR;
	}
	
	public static double getEmbeddingCapacity(double embeddedSecretDataLength, double imageSize){
		return (embeddedSecretDataLength/imageSize)*1000;
	}
}
