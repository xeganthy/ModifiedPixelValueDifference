import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class ARC_Algo {

	public static void main(String[] args) {
		ARC_Algo ARC = new ARC_Algo();
		
		//input
		BufferedImage image = ARC.getImage("lena_gray.bmp");
		MessageHelper secretMessage = new MessageHelper("test.txt");
		
		int[][] stegoGrid = new int[image.getHeight()][image.getWidth()];
		stegoGrid = getImagePixelValues(image, stegoGrid); 	//[col][row]
		
		List<Block> blocks = pixelDivision(stegoGrid);		//pixel division
		
		for(int i = 0; i < blocks.size(); i++){			//embedding phase
			Block newBlockValues = embedBlock(blocks.get(i));
			blocks.set(i, newBlockValues);
		}
		
		int counter = 0;
		for(int i = 0; i < stegoGrid.length; i++) { //replace each block's value to image
			for(int j = 0; j < stegoGrid[0].length - 2; j += 3){
				stegoGrid[j][i] = blocks.get(counter).leftPixel;
				stegoGrid[j+1][i] = blocks.get(counter).basePixel;
				stegoGrid[j+2][i] = blocks.get(counter).rightPixel;
				counter++;
			}
		}
		createStegoImage(stegoGrid);
	}
	public BufferedImage getImage(String path) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(getClass().getResource(path));
		} catch(IOException e) {
			System.out.println("File Error!");
		}
		return img;
	}
	public static int[][] getImagePixelValues(BufferedImage image, int pixelValueArray[][]){
		int height = image.getHeight();
		int width = image.getWidth();
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width; j++) {
				pixelValueArray[i][j] = image.getRGB(i, j);
				pixelValueArray[i][j] = (pixelValueArray[i][j] >> 8) & 0xff;	//we can shift >> 16 OR 0, pareparehas lang naman
																				//ang R G B values.
			}
		} 

		return pixelValueArray;
	}
	public static void createStegoImage(int pixelValueArray[][]) {
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
		File imageFile = new File("stegoImage.bmp");
		try {
			ImageIO.write(image, "bmp", imageFile);
			System.out.println("success printed @: "+System.getProperty("user.dir"));
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
	
	public static Block embedBlock(Block block) {
		//3-LSB
		
		//DETERMINE RANGE
		
		//PVD ((EMBEDDING OF SECRET MESSAGE))
		
		
		return block;
	}
	
	public static int replaceLeastSignificantBit(int pixel, int bitsToReplace, int toEmbed) {
		//make the bits 0
		int modifiedPixel = pixel;
		for(int i = 0; i <= bitsToReplace; i++) {
			modifiedPixel = (byte) (modifiedPixel & ~(1 << i)) & 0xff; 			//make 3 LSB 0
		}
		modifiedPixel = ((byte) modifiedPixel | (byte)toEmbed)& 0xff ; 
		
		return modifiedPixel;
	}
	
}
