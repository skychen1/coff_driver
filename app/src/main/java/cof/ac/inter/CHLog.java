package cof.ac.inter;

import android.util.Log;

class CHLog {

    private static String mTAG = "Coff";

    protected static void d(String TAG, String msg) {

        if (TAG != null && msg != null && CoffMsger.doPrintLog) {

            Log.d(mTAG, TAG + "--" + msg);
        }
    }


    protected static void w(String TAG, String msg) {

        if (TAG != null && msg != null) {

            Log.d(mTAG, TAG + "--" + msg);
        }
    }


}
