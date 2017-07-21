import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
public class SteganographyTest {
	public static void main(String[] args) {
		int[][] newGrid512 = new int[512][512];
		//TODO RANGE TABLE 1
		int[] rangeTable1 = null;
		//TODO RANGE TABLE 2 
		int[] rangeTable2 = null;
		SteganographyTest image = new SteganographyTest();
		newGrid512 = getImagePixelValues(image.getImage("lena_gray.bmp"), newGrid512); 		//create new grid
		//determine if image is jagged or smooth
		if(isSmooth(newGrid512)) {
			//embed using smooth table
		} else {
			//embed using edgy table
		}
		
		createStegoImage(newGrid512);
	}
	public static boolean isSmooth(int[][] pixelGrid) { //true if smooth, else image is edgy
		int height = pixelGrid.length;
		int width = pixelGrid[0].length;
		int edgeBlock = 0, smoothBlock = 0;
		final int THRESHOLD = 3;
		
		for(int i = 0; i < height; i++) {
			for(int j = 0; j < width-1; j++) {
				if(Math.abs(pixelGrid[i][j] - pixelGrid[i][j+1]) > THRESHOLD) 
					edgeBlock++;
				else
					smoothBlock++;
			}
		}
		
		if(edgeBlock > smoothBlock) {
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
									(pixelDec << 8)  | 
									(pixelDec << 0)); 
			}
		}
		System.out.println(Integer.toBinaryString(image.getRGB(0,0)));
		File imageFile = new File("Sample.bmp");
		try {
			ImageIO.write(image, "bmp", imageFile);
			System.out.println("success printed @: "+System.getProperty("user.dir"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}


/***************************************FOR TESTING, PLEASE IGNORE******************************

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
