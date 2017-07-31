import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.BitSet;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class MessageHelper {
	private static BitSet bitset = null;
	private static int currentBit = 0;
	public static void messageToBinary(String fileName) throws IOException {
		String binaryMessage = "";
		char[] messChar = getFile(fileName);

		for (int i = 0; i < messChar.length; i++){
			binaryMessage += Integer.toBinaryString(messChar[i]);
		}
		String bitsetS = "";
		bitset =  new BitSet(binaryMessage.length());
		
		System.out.println(bitset.size());
		for(int i = 0; i < binaryMessage.length(); i++) {
			
			if (binaryMessage.charAt(i) == '1') {
	            bitset.set(i);
	            bitsetS += '1';
	        } else {
	        	bitsetS += '0';
	        }
		}
		System.out.println(bitsetS.equals(binaryMessage));
		
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream("convertedMessage.txt"), "utf-8"))) {
			writer.write(binaryMessage);
			writer.newLine();
			writer.newLine();
			writer.newLine();
			writer.newLine();
			writer.write("\n"+bitsetS);
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
	
	public static String getSecretBits(int numberToRead) {
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
		System.out.println(currentBit);
		return secretBits;
	}
	
	public static void main(String[] args) {
		try {
			messageToBinary("test.txt");
			System.out.println(getSecretBits(5));
			System.out.println(getSecretBits(3));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
