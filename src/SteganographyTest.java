import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
public class SteganographyTest {
	public static void main(String[] args) {
		int[][] stegoGrid = new int[512][512];
		//TODO RANGE TABLE 1
		//ang format ng table is like this: (example of range table ni khodae & Faez 'to)
		/******************************
		 *RANGE	:	R1	R2	R3	R4	Rn 
		 *Lower	:	0	8	16	32	64
		 *Upper	:	7	15	31	63	255
		 ******************************/
		int[][] rangeTable1 = 	{{0,8,16,32,64},
								 {7,15,31,63,255}};
		//TODO RANGE TABLE 2 
		int[][] rangeTable2 = 	{{0,8,16,32,64},
				 				 {7,15,31,63,255}};
		SteganographyTest image = new SteganographyTest();
		stegoGrid = getImagePixelValues(image.getImage("lena_gray.bmp"), stegoGrid); 		//create new grid
		//determine if image is jagged or smooth
		replaceLeastSignificantBit(stegoGrid[0][3], 3, 2);
		if(isSmooth(stegoGrid)) {
			//embed using smooth table
			stegoGrid[0][0] = (byte) (stegoGrid[0][0] & ~(1 << 0)) & 0xff;					//notice i used bitwise operations, not convert to binary
			embedHiddenMessage("test", stegoGrid, rangeTable1);
		} else {
			//embed using edgy table
			stegoGrid[0][0] = (byte) (stegoGrid[0][0] | (1 << 0)) & 0xff;					//here as well.
			embedHiddenMessage("test", stegoGrid, rangeTable2);
		}
		
		createStegoImage(stegoGrid);
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
																				//ang R G B values. we are using TYPE_INT_ARGB here
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
		File imageFile = new File("Sample.bmp");
		try {
			ImageIO.write(image, "bmp", imageFile);
			System.out.println("success printed @: "+System.getProperty("user.dir"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	//TODO File na yung i-input, instead na string
	//I do not convert the integer value of the pixel
	//rather, I just use the bitwise operations like <<, >> and the likes.
	
	//***READ THIS*******READ THIS*******READ THIS****//
	//i don't knwo kung saan mag sta-start sa pag block, kasi yung pixel 1 won't be compared 
	//	to pixel 2 bc it contains info about the image eh. this will make the pixel comparisons
	// 	odd. may isang pixel na hindi ma cocompare, so ang workaround for this (as of now) is
	// 	to ignore the first 2 pixels.
	public static void embedHiddenMessage(String message, int[][] stegoGrid, int[][] rangeTable) {
		//divide grid into blocks <-- probably won't be using this
		
		//start LSB + PVD
		for(int i = 0; i < stegoGrid.length; i = i + 2) {
			for(int j = 0; j < stegoGrid[0].length; j = j + 2) {
				if(!(i == 0 && j == 0) || !(i == 0 && j == 1)) { 				//just to skip the first two pixels
					int firstBinaryPixel = stegoGrid[i][j];
					int secondBinaryPixel = stegoGrid[i][j+1];
					//use LSB
					int firstEmbeddedPixel = replaceLeastSignificantBit
								(firstBinaryPixel, 3, Integer.parseInt(MessageHelper.getSecretBits(3), 2)); 						
					int diff1 = firstBinaryPixel - secondBinaryPixel;
					
					int index = locateTableRange(diff1, rangeTable);
					
					int secretBits = (int)Math.round(	
							Math.log((rangeTable[1][index]-rangeTable[0][index]) //log2(x) = Math.log(x)/Math.log(2)
									/ Math.log(2)));  							 //speed of this can be improved
					
					
					int newDiff1 = 2 /*getSecretBits() +  rangeTable[1][index] */;
					
				}
			}
		}
		
	}
	
	
	public static int replaceLeastSignificantBit(int pixel, int bitsToReplace, int toEmbed) {
		//make the bits 0
		int modifiedPixel = pixel;
		for(int i = 0; i <= bitsToReplace; i++) {
			modifiedPixel = (byte) (modifiedPixel & ~(1 << i)) & 0xff; 			//make 3 LSB 0
		}
		System.out.println(Integer.toBinaryString(modifiedPixel));
		modifiedPixel = ((byte) modifiedPixel | (byte)toEmbed)& 0xff ; 
		System.out.println(Integer.toBinaryString(modifiedPixel));
		return modifiedPixel;
	}
	
	public static int locateTableRange(int diff, int[][] rangeTable) {
		int index = 0;
		
		
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
