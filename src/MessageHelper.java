import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.BitSet;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class MessageHelper {
	private static BitSet bitset = null;
	static int currentBit = 0;
	static int finalBit = 0;
	MessageHelper(String filename) {
		try {
			messageToBinary(filename);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	MessageHelper(int a) {
		String tester = "1101100011001";
		bitset =  new BitSet(tester.length());
		for(int i= 0; i < tester.length(); i++) {
			if (tester.charAt(i) == '1') {
	            bitset.set(i);
	        } else {
	        }
		}
	}
	public static void messageToBinary(String fileName) throws IOException {
		String binaryMessage = "";
		char[] messChar = getFile(fileName);

		for (int i = 0; i < messChar.length; i++){
			binaryMessage += Integer.toBinaryString(messChar[i]);
		}
		bitset =  new BitSet(binaryMessage.length());
		
//		System.out.println(bitset.size());
		for(int i = 0; i < binaryMessage.length(); i++) {
			if (binaryMessage.charAt(i) == '1') {
	            bitset.set(i);
	        } else {
	        }
		}
//		System.out.println(bitsetS.equals(binaryMessage));
		finalBit = bitset.length();
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("convertedMessage.txt"), "utf-8"))) {
			writer.write(binaryMessage);
			writer.flush();
		}
		
		
	}//end messageToBinary
	
	public static char[] getFile(String fileName) throws IOException {
			String everything;
			char[] input;
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
			  //  everything = everything.substring(0,(everything.length() - 1));
			     input=everything.toCharArray();
			     return input;
			} finally {
			    br.close();
			}
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
}
