package cof.ac.inter;

import android.util.Log;

public class CHLog {

	private static String mTAG = "Coff";
	private static boolean doPrint = true;
	
	public static void d(String TAG,String msg) {
		
		if(doPrint) {
			if(TAG != null && msg != null) {
				
				Log.d(mTAG, TAG+"--"+msg);
				
			}
		}
	}
	
	public static void setDoPrint(boolean print) {
		
		doPrint = print;
	}
	
	public static void w(String TAG,String msg) {
		
		if(TAG != null && msg != null) {
			
			Log.d(mTAG, TAG+"--"+msg);
			
		}
	}
	
	
}
