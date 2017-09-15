import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.BitSet;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class MessageHelper {
	private static BitSet bitset = null;
	private int currentBit = 0;
	private int finalBit = 0;
	
	MessageHelper(String filename) {
		try {
			messageToBinary(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	MessageHelper(int a) {
		String tester = "10010010110111";
		bitset =  new BitSet(tester.length());
		for(int i= 0; i < tester.length(); i++) {
			if (tester.charAt(i) == '1') {
	            bitset.set(i);
	        }
		}
	}
	public void messageToBinary(String fileName) throws IOException {
		String binaryMessage = "";
		String messChar = getFile(fileName);
		byte[] chars = messChar.getBytes("UTF-8");
		StringBuilder binary = new StringBuilder();
		for(byte b : chars) {
			int val = b;
			for(int i = 0; i < 8; i++) {
				binary.append((val & 128) == 0 ? 0 : 1);
				val <<= 1;
			}
		}
		
		binaryMessage = binary.toString();
		bitset =  new BitSet(binaryMessage.length());
		for(int i = 0; i < binaryMessage.length(); i++) {
			if (binaryMessage.charAt(i) == '1') {
	            bitset.set(i);
	        }
		}
		this.finalBit = bitset.length();
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("convertedMessage.txt"), "utf-8"))) {
			writer.write(binaryMessage);
			writer.flush();
		}
		
		
	}//end messageToBinary
	
	public static String getFile(String fileName) throws IOException {
		String everything;
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
		    StringBuilder sb = new StringBuilder();
		    String line= br.readLine();;
		    while (line != null) {
		        sb.append(line);
		       
		        sb.append('\n');
		        line = br.readLine();
		        
		    }
		     everything = sb.toString();
		     
		     return everything;
		} finally {
		    br.close();
		}
	}
	
	public static String[] splitBitStream(String fileName) throws IOException {
		String bitstream;
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try{
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();
			while(line != null) {
				sb.append(line);
				sb.append('\n');
				line = br.readLine();
			}
				bitstream = sb.toString();
		} finally {
			br.close();
		}
		String output[] = bitstream.split("(?<=\\G.{8})");
		//System.out.println(java.util.Arrays.toString(bitstream.split("(?<=\\G.{8})")));
		//System.out.println(output.length);
		return output;
	} 
	
	public static String binaryToASCII(String fileName) throws IOException {
		String[] binValues = splitBitStream(fileName);
		String output = "";
		for(int i = 0; i < binValues.length - 1; i++) {
			System.out.println(binValues[i]);
			int charCode = Integer.parseInt(binValues[i], 2);
			output += (char)charCode;
		}
		
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter
				(new FileOutputStream("messageConverted.txt"), "utf-8"));
		writer.write(output);
		writer.flush();
		writer.close();
		return output;
	}
	
	public String getSecretBits(int numberToRead) {
		int lastBit = currentBit + numberToRead;
		String secretBits = "";
		for(int i = currentBit; i < lastBit; i++) {
			if(bitset.get(i)) {
				secretBits += '1';
			} else {
				secretBits += '0';
			}
		}
		currentBit = lastBit;
		
		if(currentBit == finalBit) {
			currentBit++;
		}
		return secretBits;
	}
	
	public String peekSecretBits(int numberToRead) {
		int lastBit = currentBit + numberToRead;
		String secretBits = "";
		
		for(int i = currentBit; i < lastBit; i++) {
			if(bitset.get(i)) {
				secretBits += '1';
			} else {
				secretBits += '0';
			}
		}
		
		return secretBits;
	}
	public int getCurrentBit() {
		return this.currentBit;
	}

	public void setCurrentBit(int currentBit) {
		this.currentBit = currentBit;
	}

	public int getFinalBit() {
		return finalBit;
	}

	public  void setFinalBit(int finalBit) {
		this.finalBit = finalBit;
	}
}
