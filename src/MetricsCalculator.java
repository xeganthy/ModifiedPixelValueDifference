import java.awt.image.BufferedImage;

public class MetricsCalculator {
	
	public static void main(String[] args) {
		BufferedImage originalImage = ImageHelper.getImage("/Users/MARK ANTONIO/Documents/GitHub/ModifiedPixelValueDifference/lena_gray.bmp");				//bmp image
		int[][] origImage = new int[originalImage.getHeight()][originalImage.getWidth()];	//initialize array
		origImage = ImageHelper.getImagePixelValues(originalImage, origImage);
		BufferedImage arcStegoImage = ImageHelper.getImage("/Users/MARK ANTONIO/Documents/GitHub/ModifiedPixelValueDifference/ARCStegoImage.bmp");
		int[][] stegoImage = new int[arcStegoImage.getHeight()][arcStegoImage.getWidth()];	//initialize array
		stegoImage = ImageHelper.getImagePixelValues(arcStegoImage, stegoImage); 
		
	}
	
	public static double calculatePSNR(int[][] origImage, int[][] stegoImage){
		int mse=0;
		for(int i = 0; i < origImage.length; i++) {
			for(int j = 0; j < origImage[0].length; j++) {
				mse += Math.pow(origImage[i][j]-stegoImage[i][j], 2);
				
			}
		}
		mse = 1/mse;
		double PSNR = Math.log((Math.pow(512, 2)/mse))*10;
		return PSNR;
	}
}
