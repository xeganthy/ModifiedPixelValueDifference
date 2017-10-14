import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

public class ImageHelper {
	public static BufferedImage srcImg = null;
	public static BufferedImage getImage(String dir){
		BufferedImage img = null;
		try {
			img = ImageIO.read(new File(dir));
			srcImg = img;
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
				//int[] pixel = image.getRaster().getPixel(i, j, new int[3]);
				pixelValueArray[i][j] = image.getRaster().getSample(i, j, 0);
			}
		} 

		return pixelValueArray;
	}
	public static void createStegoImage(int pixelValueArray[][], String fileName) {
		BufferedImage image = new BufferedImage(pixelValueArray.length, pixelValueArray[0].length, 
								srcImg.getType()); 
		
		for(int i = 0; i < pixelValueArray.length; i++) {
			for(int j = 0; j < pixelValueArray[0].length; j++) {
				int pixelDec = pixelValueArray[i][j];
				image.getRaster().setSample(i, j, 0, 	(255 	  << 24) |
														(pixelDec << 16) |		//shift the pixels according to their bit position
														(pixelDec << 8)  |		//same values on 0-7, 8-15, 16-23
														(pixelDec << 0)); 
			}
		}
//		BufferedImage trueColor = new BufferedImage(pixelValueArray.length, 
//				pixelValueArray[0].length, BufferedImage.TYPE_BYTE_GRAY);
//		Graphics2D g = trueColor.createGraphics();
//		try {
//		    g.drawImage(image, 0, 0, null);
//		}
//		finally {
//		    g.dispose();
//		}
//
//		image = trueColor;
//		RenderedImage rendImage = image;
		String path="/Users/MARK ANTONIO/Documents/GitHub/ModifiedPixelValueDifference/stego_Images/"+fileName+".bmp";
		File imageFile = new File(path);
		try {
			ImageIO.write(image, "bmp", imageFile);
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
