import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageHelper {
	public static BufferedImage getImage(String dir){
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(dir));
		} catch(IOException e) {
			System.out.println("File Error!");
	        e.printStackTrace();
		}
		return img;
	}
	public static int[][] getImagePixelValues(BufferedImage image, int pixelValueArray[][]){
		int height = image.getHeight();
		int width = image.getWidth();
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				pixelValueArray[i][j] = image.getRGB(i, j) & 0xFF;
			}
		} 

		return pixelValueArray;
	}
	public static void createStegoImage(int pixelValueArray[][], String fileName) {
		BufferedImage image = new BufferedImage(512, 512, 
									BufferedImage.TYPE_BYTE_GRAY); 
		for(int i = 0; i < pixelValueArray.length; i++) {
			for(int j = 0; j < pixelValueArray[0].length; j++) {
				int pixelDec = pixelValueArray[i][j];
				image.setRGB(i, j,  (pixelDec << 16) | 							//shift the pixels according to their bit position
									(pixelDec << 8)  | 							//same values on 0-7, 8-15, 16-23
									(pixelDec << 0)); 
			}
		}
		RenderedImage rendImage = image;
		File imageFile = new File(fileName+".bmp");
		try {
			ImageIO.write(rendImage, "bmp", imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public static List<Block> pixelDivision(int[][] stegoGrid){
		List<Block> blocks = new ArrayList<>();
		for(int i = 0; i < stegoGrid.length; i++){
			for(int j = 0; j < stegoGrid[0].length - 2; j += 3){
				Block block = new Block(stegoGrid[j][i], stegoGrid[j+1][i], stegoGrid[j+2][i], j+1, i);
				blocks.add(block);
			}
		}
		return blocks;
	}
}
