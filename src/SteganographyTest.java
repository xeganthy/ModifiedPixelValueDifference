import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class SteganographyTest {
	public static void main(String[] args) {
		
	}
	
	public static BufferedImage getImage(String path) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File("path"));
		} catch(IOException e) {
			System.out.println("File Error!");
		}
		return img;
	}
	
	//returns all grayscale pixel value in 2d array
	//bc why not?
	public static int[][] getPixelValue(BufferedImage image, int imageArray[][]){
		int height = image.getHeight();
		int width = image.getWidth();
		
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				
			}
		} 
		return imageArray;
	}
}
