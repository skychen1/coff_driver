package ch.wrzw;

import java.util.concurrent.TimeUnit;

public class Waiter {

	public static  void doWait(long millSecond) {
		
		try {
			
			TimeUnit.MILLISECONDS.sleep(millSecond);
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
