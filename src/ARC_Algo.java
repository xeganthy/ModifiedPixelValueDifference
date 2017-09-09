import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

public class ARC_Algo {

	public static void main(String[] args) {
		int rangeTable[][] = new int[1][1];
		//input
		BufferedImage image = getImage("lena_gray.bmp");
		MessageHelper secretMessage = new MessageHelper("test.txt");
		
		int[][] stegoGrid = new int[image.getHeight()][image.getWidth()];
		stegoGrid = getImagePixelValues(image, stegoGrid); 	//[col][row]
		
		List<Block> blocks = pixelDivision(stegoGrid);		//pixel division
		//embedBlock(blocks.get(1), secretMessage, rangeTable);
		for(int i = 0; i < blocks.size(); i++){			//embedding phase
			embedBlock(blocks.get(i), secretMessage, rangeTable);
		}
		
		int counter = 0;
		for(int i = 0; i < stegoGrid.length; i++) { //replace each block's value to image
			for(int j = 0; j < stegoGrid[0].length - 2; j += 3){
				stegoGrid[j][i] = blocks.get(counter).getLeftPixel();
				stegoGrid[j+1][i] = blocks.get(counter).getBasePixel();
				stegoGrid[j+2][i] = blocks.get(counter).getRightPixel();
				counter++;
			}
		}
		createStegoImage(stegoGrid);
	}
	public static BufferedImage getImage(String path) {
		BufferedImage img = null;
		try {
			img = ImageIO.read(KhodFaez_Algo.class.getResource(path));
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
	
	public static void embedBlock(Block block, MessageHelper secretMessage, int[][] rangeTable) {
		int basePixel3LSB = (byte) (block.getBasePixel() & ((1 << 3) - 1)); 
		int secretMessageLSB = Integer.parseInt(secretMessage.peekSecretBits(3), 2);
		int embeddedBasePixel = replaceLeastSignificantBit
				(block.getBasePixel(), 3, Integer.parseInt(secretMessage.getSecretBits(3), 2)); //LSB
		block.setBasePixel(opapPixel(basePixel3LSB, secretMessageLSB, embeddedBasePixel));
		//DETERMINE RANGE
		block.setLeftRange(locateTableRange(Math.abs(block.getBasePixel() - block.getLeftPixel()), rangeTable));
		block.setRightRange(locateTableRange(Math.abs(block.getBasePixel() - block.getRightPixel()), rangeTable));
		
		//PVD ((EMBEDDING OF SECRET MESSAGE))
		int embeddedLeftPixel = embedPixelPVD("left", block, secretMessage, rangeTable);
		int embeddedRightPixel = embedPixelPVD("right", block, secretMessage, rangeTable);
		
		block.setLeftPixel(embeddedLeftPixel);
		block.setRightPixel(embeddedRightPixel);
	}
	
	public static int embedPixelPVD(String pixel, Block block, MessageHelper secretMessage, int[][] rangeTable) {
		int embeddableBits = rangeTable[2][block.getRangeIndex(pixel)];
		int secretM = Integer.parseInt(secretMessage.getSecretBits(embeddableBits), 2);
		int newDiff = rangeTable[0][block.getRangeIndex(pixel)] + secretM;
		
		int embeddedPixelTemp1 = block.getBasePixel() + newDiff;
		int embeddedPixelTemp2 = block.getBasePixel() - newDiff;
		
		if(pixel.equals("left")) {
			if(Math.abs(block.getLeftPixel() - embeddedPixelTemp1) < Math.abs(block.getLeftPixel() - embeddedPixelTemp2)) {
				return embeddedPixelTemp1;
			} else {
				return embeddedPixelTemp2;
			}
		} else { 
			if(Math.abs(block.getRightPixel() - embeddedPixelTemp1) < Math.abs(block.getRightPixel() - embeddedPixelTemp2)) {
				return embeddedPixelTemp1;
			} else {
				return embeddedPixelTemp2;
			}
		}
		
		
	}
	public static int locateTableRange(int diff, int[][] rangeTable) {
		int index = 0;
		for(int i = 0; i < rangeTable[0].length; i++){
			if(diff >= rangeTable[0][i] && diff <= rangeTable[1][i]){
				index = i;
				break;
			}
		}
		return index;
	}
	
	public static int opapPixel(int basePixel3LSB, int secretMessageLSB, int embeddedBasePixel) {
		int baseDifference = basePixel3LSB - secretMessageLSB;
		
		if(baseDifference > Math.pow(2, 2) && (embeddedBasePixel + Math.pow(2, 3) >= 0) && (embeddedBasePixel + Math.pow(2, 3) <= 255)) {
			embeddedBasePixel += Math.pow(2, 3); 
		} else if(baseDifference < (-1 * Math.pow(2,2)) && (embeddedBasePixel - Math.pow(2, 3) <= 255) && (embeddedBasePixel - Math.pow(2, 3) >= 0)) {
			embeddedBasePixel -= Math.pow(2, 3);
		}
		
		return embeddedBasePixel;
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
