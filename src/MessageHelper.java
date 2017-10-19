import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.util.BitSet;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

public class MessageHelper {
	private static BitSet bitset = null;
	private int currentBit = 0;
	private int finalBit = 0;
	private static String algo;
	
	MessageHelper(String algo) {
		MessageHelper.algo = algo;
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
		this.finalBit = bitset.length()+1;
		//System.out.println(finalBit);
		String filename = (algo.equals("KF")) ? "KFtoEmbedBinary.txt" : "ARCtoEmbedBinary.txt";
		try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(filename), "utf-8"))) {
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
	        	sb.append("\n");
	        	line = br.readLine();
		    }
		     everything = sb.toString();
		     
		     return everything;
		} finally {
		    br.close();
		}
	}
	
//	public static String[] splitBitStream(String fileName) throws IOException {
//		String bitstream;
//		BufferedReader br = new BufferedReader(new FileReader(fileName));
//		try{
//			StringBuilder sb = new StringBuilder();
//			String line = br.readLine();
//			while(line != null) {
//				sb.append(line);
//				line = br.readLine();
//			}
//				bitstream = sb.toString();
//		} finally {
//			br.close();
//		}
//		String output[] = splitStringEvery(bitstream, 8);
//		return output;
//	} 
	
	public static String[] splitStringEvery(String s, int interval) {
	    int arrayLength = (int) Math.ceil(((s.length() / (double)interval)));
	    String[] result = new String[arrayLength];
	    String res = "";
	    int j = 0;
	    int lastIndex = result.length - 1;
	    for (int i = 0; i < lastIndex; i++) {
	        result[i] = s.substring(j, j + interval);
	        res += result[i];
	        j += interval;
	    } //Add the last bit
	    //if(s.substring(j).length() == interval)
	    //	result[lastIndex] = s.substring(j);
	    //System.out.println(res);
	    return result;
	}
	
	public static String binaryToASCII(String fileName, String algo, String bitstream) throws IOException {
		String[] binValues = splitStringEvery(bitstream, 8);
		String output = "";
		for(int i = 0; i < binValues.length - 1; i++) {
			int charCode = Integer.parseInt(binValues[i], 2);
			if(charCode != 0)
				output += (char)charCode;
		}
//		String filename = (algo.equals("KF")) ? "KFExtractedMessageASCII.txt" : "ARCExtractedMessageASCII.txt";
//		String path="/Users/MARK ANTONIO/Documents/GitHub/ModifiedPixelValueDifference/extracted_Messages/"+fileName+".txt";
		String path =System.getProperty("user.dir")+"/extracted_Messages/"+fileName+".txt";
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter
				(new FileOutputStream(path), "UTF-8"));
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
	
	public static void writeMessage(String embeddedSecretMessage, String algo) throws IOException{
		String fileName = (algo.equals("KF")) ? "KFExtractedMessageBinary.txt" : "ARCExtractedMessageBinary.txt";
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter
				(new FileOutputStream(fileName), "utf-8"));
		writer.write(embeddedSecretMessage);
		writer.flush();
		writer.close();
		System.out.println("extractedMessageBin");
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
