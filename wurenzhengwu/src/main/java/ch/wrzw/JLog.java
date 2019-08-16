package ch.wrzw;

import android.util.Log;

class JLog {

	private static String mTAG = "juice";
	
	protected static void d(String TAG,String msg){
		
		if(TAG != null && msg != null && CPUMsger.doPrintLog) {
			
			Log.d(mTAG, TAG+"--"+msg);
		}
	}


	protected static void w(String TAG,String msg){

		if(TAG != null && msg != null) {

			Log.d(mTAG, TAG+"--"+msg);
		}
	}
	
}
