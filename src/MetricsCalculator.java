import java.awt.image.BufferedImage;
import java.util.Scanner;

public class MetricsCalculator {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Enter original image file name: ");
		String originalFile = sc.nextLine();
		System.out.println("Enter Arc Image file name: ");
		String arcFile = sc.nextLine();
		System.out.println("Enter Khoez Image file name: ");
		String khoFile = sc.nextLine();
		
		BufferedImage originalImage = ImageHelper.getImage("/Users/MARK ANTONIO/Documents/GitHub/ModifiedPixelValueDifference/original_Images/"+originalFile+".bmp");				//bmp image
		int[][] origImage = new int[originalImage.getHeight()][originalImage.getWidth()];	
		origImage = ImageHelper.getImagePixelValues(originalImage, origImage);
		BufferedImage arcStegoImage = ImageHelper.getImage("/Users/MARK ANTONIO/Documents/GitHub/ModifiedPixelValueDifference/stego_Images/"+arcFile+".bmp");
		int[][] arcstegoImage = new int[arcStegoImage.getHeight()][arcStegoImage.getWidth()];	
		arcstegoImage = ImageHelper.getImagePixelValues(arcStegoImage, arcstegoImage); 
		BufferedImage khoezStegoImage = ImageHelper.getImage("/Users/MARK ANTONIO/Documents/GitHub/ModifiedPixelValueDifference/stego_Images/"+khoFile+".bmp");
		int[][] khoezstegoImage = new int[arcStegoImage.getHeight()][arcStegoImage.getWidth()];	
		khoezstegoImage = ImageHelper.getImagePixelValues(khoezStegoImage, khoezstegoImage); 
		
		System.out.println("ARC image PSNR: "+calculatePSNR(origImage, arcstegoImage));
		System.out.println("Khoez image PSNR: "+calculatePSNR(origImage, khoezstegoImage));
		
	}
	
	public static double calculatePSNR(int[][] origImage, int[][] stegoImage){
		int mse=0;
		for(int i = 0; i < origImage.length; i++) {
			for(int j = 0; j < origImage[0].length; j++) {
				mse += Math.pow(origImage[i][j]-stegoImage[i][j], 2);
//				System.out.println(origImage[i][j] + "  " + stegoImage[i][j]);
			}
		}
		int mean = mse/512;
		double PSNR = 10*Math.log((Math.pow(512, 2)/mean));
		return PSNR;
	}
}
