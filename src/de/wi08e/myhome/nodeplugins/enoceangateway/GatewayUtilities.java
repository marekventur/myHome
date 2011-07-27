/**
 * 
 */
package de.wi08e.myhome.nodeplugins.enoceangateway;

/**
 * @author Marek
 *
 */
public class GatewayUtilities {
	public static int[] fromString(String in) {
		if (in.length() % 2 != 0) throw new IllegalArgumentException("Length should be a multiple of 2");
		int[] result = new int[in.length() / 2];
		for (int i=0; i< result.length; i++) {
			result[i] = Integer.decode("0X"+in.charAt(i*2)+in.charAt(i*2+1));
		
		}
		return result;
	}
	
	public static String charToBit(char in) {
		byte b = Byte.parseByte(""+in, 16);
		String res = "";
		int mask = 0x80;
		mask >>= 1;
		mask >>= 1;
		mask >>= 1;
		mask >>= 1;
        while (mask > 0) {
            if ((mask & b) != 0) {
                res += '1';
            } else {
            	res += '0';
            }
            mask >>= 1;
        }
        return res;
	}
	
	public static String fromByte(int in) {
		if (in > 255) throw new IllegalArgumentException("Can't convert to Byte");
		if (in > 15)
			return Integer.toHexString(in).toUpperCase();
		return "0"+Integer.toHexString(in).toUpperCase();
	}
			
	public static String constructTelegram(int org, String data, String id) {	
		return constructTelegram("6B"+fromByte(org)+data+id+"00");	
	}
	
	public static String constructTelegram(int org, String data, String id, String status) {	
		return constructTelegram("6B"+fromByte(org)+data+id+status);	
	}
	
	public static String constructTelegram(String content) {
		while(content.length() < 22) content += "0";
		return "A55A"+ addCheckSum(content);	
	}
	
	
	public static String addCheckSum(String input) {
		int[] values = fromString(input);
		if (values.length != 11) throw new IllegalArgumentException("input should be 11 bytes long");
		int checkSum = 0;
		for (int i=0; i<11; i++) checkSum += values[i];
		return input+fromByte(checkSum % 256);
	}
	
	public static String unpackTelegram(char[] input) {
		if (input.length != 28) throw new IllegalArgumentException("input should be 14 bytes long");
		String result = "";
		for (int i=4; i< 26; i++) {
			result += input[i];
		}	
		return result;
	}
	
	public static String unpackTelegramReadable(char[] input) {
		if (input.length != 28) throw new IllegalArgumentException("input should be 14 bytes long");
		String result = "";
		for (int i=4; i< 26; i++) {
			result += input[i];
			if (i % 2 == 1)	result += " ";
		}	
		return result;
	}
	
}
