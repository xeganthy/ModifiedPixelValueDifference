import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.imageio.ImageIO;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
public class SteganographyTest {
	
	public static int[][] stegoGrid = new int[512][512];
	static MessageHelper secretMessage = new MessageHelper("test.txt");					//Scan Message
	
	public static void main(String[] args) throws IOException {
		//TODO RANGE TABLE 1
		//ang format ng table is like this: (example of range table ni khodae & Faez 'to)
		/******************************
		 *RANGE	:	R1	R2	R3	R4	Rn 
		 *Lower	:	0	8	16	32	64
		 *Upper	:	7	15	31	63	255
		 *BITS  :   0 	0 	0	0	0    <- bits embeddable wherein it will be computed after determining which range table is going to be used
		 *									the formula for this is Log2(Upper - Lower) (floor)
		 ******************************/
		int[][] rangeTable1 = 	{{0,8,16,32,64},
							     {7,15,31,63,255},
								 {0,0,0,0,0}};
		//TODO RANGE TABLE 2 
		int[][] rangeTable2 = 	{{0,8,16,32,64},
				 				 {7,15,31,63,255},
								 {0,0,0,0,0}};
		
		SteganographyTest image = new SteganographyTest();
		stegoGrid = getImagePixelValues(image.getImage("lena_gray.bmp"), stegoGrid); 		//create new grid
		//determine if image is jagged or smooth
		if(isSmooth(stegoGrid)) {
			//embed using smooth table
			stegoGrid[0][0] = (byte) (stegoGrid[0][0] & ~(1 << 0)) & 0xff;					//notice i used bitwise operations, not convert to binary
			rangeTable1[2] = getEmbeddableBits(rangeTable1);
			embedHiddenMessage(stegoGrid, rangeTable1);
		} else {
			//embed using edgy table
			stegoGrid[0][0] = (byte) (stegoGrid[0][0] | (1 << 0)) & 0xff;					//here as well.
			rangeTable1[2] = getEmbeddableBits(rangeTable1);
			embedHiddenMessage(stegoGrid, rangeTable2);
		}
		createStegoImage(stegoGrid);
	}
	public static int[] getEmbeddableBits(int[][] rangeTable) {
		int[] embeddableSecretBits = new int[rangeTable[0].length];
		for(int i = 0; i < rangeTable[0].length; i++) {
			embeddableSecretBits[i] = (i <= 3) ? 
					(int)Math.round((Math.log(rangeTable[1][i]-rangeTable[0][i]) / Math.log(2))): 
					(int)Math.round((Math.log(rangeTable[0][i]) / Math.log(2)));
			System.out.println("embeddable "+embeddableSecretBits[i]);
		}
		return embeddableSecretBits;
	}
	public static boolean isSmooth(int[][] pixelGrid) { 									//true if smooth, else image is edgy
		int height = pixelGrid.length;
		int width = pixelGrid[0].length;
		int edgePixel = 0, smoothPixel = 0;
		final int THRESHOLD = 3;															//TODO someone update this
		
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width-1; j++) {
				if(Math.abs(pixelGrid[i][j] - pixelGrid[i][j+1]) > THRESHOLD) 
					edgePixel++;
				else
					smoothPixel++;
			}
		}
		
		if(edgePixel > smoothPixel) {
			return false;
		} else {
			return true;
		}
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
	//returns all grayscale pixel value in 2d array
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
	public static void embedHiddenMessage(int[][] stegoGrid, int[][] rangeTable) throws IOException {
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter
				(new FileOutputStream("log.txt"), "utf-8"));
		for(int j = 0; j < stegoGrid.length; j++) {
			for(int i = 0; i < stegoGrid[0].length - 2; i += 3) {
				if(secretMessage.getCurrentBit() <= secretMessage.getFinalBit()) {
					if((i != 0 || (j != 0 && j != 1 && j != 2))){ 				//just to skip the first three pixels
						String log1 = "P["+j+"]["+i+"]: "+stegoGrid[i][j];
						String log2 = "P["+j+"]["+(i+1)+"]: "+stegoGrid[i][j + 1];
						String log3 = "P["+j+"]["+(i+2)+"]: "+stegoGrid[i][j + 2];
						int[] newValues = embedTo3PixelBlock(stegoGrid[i][j], stegoGrid[i + 1][j], stegoGrid[i + 2][j], rangeTable);
						stegoGrid[i][j] = newValues[1];
						stegoGrid[i + 1][j] = newValues[0];
						stegoGrid[i + 2][j] = newValues[2];
	 					log1 += " | P["+j+"]["+(i)+"]: "+stegoGrid[i][j];
						log2 += " | P["+j+"]["+(i+1)+"]: "+stegoGrid[i][j + 1];
						log3 += " | P["+j+"]["+(i+2)+"]: "+stegoGrid[i][j + 2];
						writer.write(log1);
						writer.newLine();
						writer.write(log2);
						writer.newLine();
						writer.write(log3);
						writer.newLine();
					}
				}
			}
		}
		writer.flush();
		writer.close();
	}
	public static int[] embedTo3PixelBlock(int leftPixel, int basePixel, int rightPixel, int[][] rangeTable){
		int basePixel3LSB = (byte) (basePixel & ((1 << 3) - 1)); 
		int secretMessageLSB = Integer.parseInt(secretMessage.peekSecretBits(3), 2);
		int embeddedBasePixel = replaceLeastSignificantBit(basePixel, 2, Integer.parseInt(secretMessage.getSecretBits(3), 2));
		int baseDifference = basePixel3LSB - secretMessageLSB;
		
		if(baseDifference > Math.pow(2, 2) && (embeddedBasePixel + Math.pow(2, 3) >= 0) && (embeddedBasePixel + Math.pow(2, 3) <= 255)) {
			embeddedBasePixel += Math.pow(2, 3); 
		} else if(baseDifference < (-1 * Math.pow(2, 2)) && (embeddedBasePixel - Math.pow(2, 3) <= 255) && (embeddedBasePixel - Math.pow(2, 3) >= 0)) {
			embeddedBasePixel -= Math.pow(2, 3);
		}
		
		int diffLeft = Math.abs(embeddedBasePixel - leftPixel);
		int diffRight = Math.abs(embeddedBasePixel - rightPixel);
		
		int leftDiffRangeIndex = locateTableRange(diffLeft, rangeTable);
		int rightDiffRangeIndex = locateTableRange(diffRight, rangeTable);
//		System.out.println("indeces "+leftDiffRangeIndex+" "+rightDiffRangeIndex);
		int embeddableBitsLeft = rangeTable[2][leftDiffRangeIndex];		
		int embeddableBitsRight = rangeTable[2][rightDiffRangeIndex];
//		System.out.println("embeddable: "+embeddableBitsLeft+" "+embeddableBitsRight);
		
		int s1 = Integer.parseInt(secretMessage.getSecretBits(embeddableBitsLeft), 2);
		int s2 = Integer.parseInt(secretMessage.getSecretBits(embeddableBitsRight), 2);
		
//		System.out.println("s1 "+s1+" s2 "+s2);
		
		int newDiffLeft = rangeTable[0][leftDiffRangeIndex] + s1;
		int newDiffRight = rangeTable[0][rightDiffRangeIndex] + s2;
//		System.out.println(newDiffLeft+" "+newDiffRight);
		
		int leftPixelTemp1 = embeddedBasePixel + newDiffLeft;
		int leftPixelTemp2 = embeddedBasePixel - newDiffLeft;
		int rightPixelTemp1 = embeddedBasePixel + newDiffRight;
		int rightPixelTemp2 = embeddedBasePixel - newDiffRight;
		
		int embeddedLeftPixel = (Math.abs(leftPixel - leftPixelTemp1) < Math.abs(leftPixel - leftPixelTemp2)) ? 
								 leftPixelTemp1 : leftPixelTemp2;
		int embeddedRightPixel = (Math.abs(rightPixel - rightPixelTemp1) < Math.abs(rightPixel - rightPixelTemp2)) ?
								  rightPixelTemp1 : rightPixelTemp2;
		
//		System.out.println(embeddedLeftPixel+" "+embeddedRightPixel);
		//replace old val with new val
		basePixel = embeddedBasePixel;
		leftPixel = embeddedLeftPixel;
		rightPixel = embeddedRightPixel;
		
		return new int[] {basePixel, leftPixel, rightPixel};
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
}


/***************************************FOR TESTING, PLEASE IGNORE******************************
System.out.println("skipped pixel["+i+"]["+j+"]");
System.out.println("pixel["+i+"]["+j+"] vs pixel["+(i)+"]["+(j+1)+"]");
System.out.println("Before");
		System.out.println(Integer.toBinaryString(newGrid512[0][0]));
		System.out.println(Integer.toBinaryString(newGrid512[0][1]));
		System.out.println("After: "+isSmooth(newGrid512));

int blue = image.getRGB(0,0) & 0xff;
int green = (image.getRGB(0,0) & 0xff00) >> 8;
int red = (image.getRGB(0,0) & 0xff0000) >> 16;
testing byte values
System.out.println(Integer.toBinaryString(image.getRGB(0,0)));
System.out.println(pixelValueArray[0][0]);
int val = 255 << 24 | (pixelValueArray[0][0] << 16) | (pixelValueArray[0][0] << 8) | (pixelValueArray[0][0] << 0);
System.out.println(Integer.toBinaryString(val));


System.out.println(pixelValueArray[i][j]+"pixel #: "+counter);
int blue = pixelValueArray[i][j] & 0xff;
int green = (pixelValueArray[i][j] & 0xff00) >> 8;
int red = (pixelValueArray[i][j] & 0xff0000) >> 16;
System.out.println(blue+ " "+green+ " " +red);

****************************************************************************************************/
