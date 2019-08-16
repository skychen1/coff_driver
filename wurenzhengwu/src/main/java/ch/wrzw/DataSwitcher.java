package ch.wrzw;

public class DataSwitcher {

	public static byte[] intToBytes(int i) {

        byte[] b = new byte[4];  
        b[0] = (byte) (i >>> 24);  
        b[1] = (byte) (i >>> 16);  
        b[2] = (byte) (i >>> 8);  
        b[3] = (byte) i;  
        return b;  
    }
	
	public static int bytesToInt(byte[] b) {  
        int i = (b[0] << 24) & 0xFF000000;  
        i |= (b[1] << 16) & 0xFF0000;  
        i |= (b[2] << 8) & 0xFF00;  
        i |= b[3] & 0xFF;  
        return i;  
    }
	
	public static int byteToInt(byte data) { 
		
		byte[] b = {0x00,0x00,0x00,data};
        return bytesToInt(b);
    }  
				
	public static byte[] charToBytes(char c) {  
		 
        byte[] b = new byte[2];  
        b[0] = (byte) (c >>> 8);  
        b[1] = (byte) c;  
        return b;  
	}
	
	public static String byte2Hex(byte b) {  
		
		  String hex = Integer.toHexString(b & 0xFF); 
		  if (hex.length() == 1) { 
		       hex = '0' + hex; 
		  } 
		  return hex;
	}
	
	public static String bytes2Hex(byte[] b) {

		if(b == null) {

			return "";
		}
		String hex = "";
		for(int i = 0; i < b.length; i++) {
			
			hex = hex +byte2Hex(b[i])+" ";
		}
		return hex;		
	}
	
	
	public static String bytes2Hex_noSpace(byte[] b) {

		if(b == null) {

			return "";
		}
		String hex = "";
		for(int i = 0; i < b.length; i++) {
			
			hex = hex +byte2Hex(b[i]);
		}
		return hex;		
	}
	
	public static String byte2Hex_noSpace(byte[] b, int from, int to) {

		if(b == null) {

			return "";
		}
		byte[] mbytes = new byte[to - from + 1];
		for(int i = from; i <= to; i++ ) {
			
			mbytes[i - from] = b[i];
		}
		return bytes2Hex_noSpace(mbytes);
	}
	
	public static String byte2Hex(byte[] b, int from, int to) {

		if(b == null) {

			return "";
		}
		byte[] mbytes = new byte[to - from + 1];
		for(int i = from; i <= to; i++ ) {
			
			mbytes[i - from] = b[i];
		}
		return bytes2Hex(mbytes);
	}
	
	public static void main(String[] args) {
		
		
		byte t = 0x65;
		System.out.println(byte2Hex(t));
		
	}
}
