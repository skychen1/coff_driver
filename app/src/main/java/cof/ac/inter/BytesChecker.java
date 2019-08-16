package cof.ac.inter;

class BytesChecker {

	static boolean hasReturn(byte[]byteArr) {
		
		for(int i = 0; i < byteArr.length; i++){
			
			if( byteArr[i] != 0x00) {
				
				return true;
			}
		}
		return false;
	}
	
	
	static byte[] getValidBytes(byte[] byteArr) {
		
		int count = 0;
		if(byteArr == null ||byteArr.length < 3 ) {
			
			return null;
		}
		for(int i = 1; i < byteArr.length-1; i++) {
			
			if(byteArr[i]==0x02 || byteArr[i] == 0x03 || byteArr[i] == 0x10) {
				
				count++;
			}
		}
		if(count == 0) {
			
			return byteArr;
		}
		
		byte[] newByteArr = new byte[byteArr.length+count];
		newByteArr[0] = byteArr[0];
		newByteArr[newByteArr.length-1] = byteArr[byteArr.length-1];
		int index = 1;
		for(int i = 1; i < byteArr.length-1; i++) {
			
			if(byteArr[i]==0x02 || byteArr[i] == 0x03 || byteArr[i] == 0x10) {
				
				newByteArr[index] = 0x10;
				newByteArr[index+1] = byteArr[i];
				index = index + 2;
				
			}else {
				
				newByteArr[index] = byteArr[i];
				index++;
			}
		}
		return newByteArr;
		
	}
}
