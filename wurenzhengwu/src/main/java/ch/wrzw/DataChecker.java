package ch.wrzw;

import java.util.ArrayList;

class DataChecker {

	private  static byte[] doFilter( byte[] byteArr) {
		
		int i = 0;
		ArrayList<Byte> mList = new ArrayList<Byte>();
		while( i < byteArr.length) {
			
			if( byteArr[i] == 0x10) {
				
				
				if(i+1 < byteArr.length && (byteArr[i+1]==0x10 || byteArr[i+1]==0x02 || byteArr[i+1]==0x03)) {
					
					mList.add(byteArr[i+1]);
					i = i + 2;
					
				}else {
					
					i++;
				}
			}else {
				mList.add(byteArr[i]);
				i++;
			}
		}
		
		byte[] bArr = new byte[mList.size()];
		for(int j = 0 ; j < mList.size(); j++) {
			
			bArr[j] = mList.get(j).byteValue();
		}
		return bArr;
		
	}
	
	
	private static int trim( byte[] byteArr) {
		
		int length = byteArr.length;
		for(int i = byteArr.length-1; i <  byteArr.length; i-- ) {
			
			if( byteArr[i] == 0x00) {
				
				length--;
			}else {
				
				if(byteArr[i] != 0x03) {
					
					return -1;
				}
				break;
			}
		}
		return length;
	}
	
	
	
	
	protected static byte[] getFilterBytes( byte[] byteArr) {


		if( byteArr[0] != 0x01) {
			
			return null;
		}
		int length = trim(byteArr);
		if(length == -1) {
			
			return null;
		}
		byte[] nBytes = new byte[length];
		for(int i = 0; i < length; i++) {
			
			nBytes[i] = byteArr[i];
		}
		byte[] rbyteArr = doFilter(nBytes);
		return rbyteArr;
	}




	protected static boolean hasData(byte[]byteArr) {

		if (byteArr==null){
			return false;
		}

		for(int i = 0; i < byteArr.length; i++){
			
			if( byteArr[i] != 0x00) {
				
				return true;
			}
		}
		return false;
	}
	
	
	protected static byte[] getValidBytes(byte[] byteArr) {
		
		if(byteArr == null || byteArr.length < 4){
			
			return byteArr;
		}
		int count = 0;
		for(int i = 1; i < byteArr.length-1;i++) {
			
			if(byteArr[i] ==0x10 || byteArr[i] == 0x02 || byteArr[i] == 0x03) {
				
				count++;
			}
		}
		if(count == 0) {
			
			return byteArr;
		}
		else {
		
			byte[] mbyteArr = new byte[byteArr.length+count];
			mbyteArr[0] = byteArr[0];
			mbyteArr[mbyteArr.length-1] = byteArr[byteArr.length -1];
			int index = 1;
			for(int i = 1; i < byteArr.length-1; i++) {
				
				if(byteArr[i]== 0x02 || byteArr[i] == 0x03 || byteArr[i] == 0x10){
					
					mbyteArr[index] = 0x10;
					mbyteArr[index+1] = byteArr[i];
					index = index +2;
				}else {
					
					mbyteArr[index] = byteArr[i];
					index++;
				}
			}
			return mbyteArr;
		}
	}
	
	
}
