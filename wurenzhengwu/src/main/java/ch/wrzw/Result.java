package ch.wrzw;

import java.util.Random;

public class Result {

	int code = -1;
	public static final int SUCCESS = 1;
	public static final int RETURN_ERR = 2;
	public static final int DATA_ERR = 3;
	
	
	String errDes = null;
	String return_bytes = null;
	String send_bytes = null;
	String id = null;
	
	String result_name = null;
	
	static String TAG = "Result";
	
	protected Result(String result_name) {
		
		id = System.currentTimeMillis()+"-"+new Random().nextInt(1000);
		this.result_name = result_name;
	}
	
	
	
	public String getId() {
		
		return id;
	}
	
	public int getCode() {
		
		return code;
	}
	protected void setCode(int code) {
		
		this.code = code;
	}
	
	public boolean success(){
		
		if(code == SUCCESS) {
			
			return true;
		}
		return false;
	}
	
	public String getErrDes() {
		return errDes;
	}
	
	protected void setErrDes(String errDes) {
		
		this.errDes = errDes;
	}
	
	public String getReturn_bytes() {
		
		return return_bytes;
	}
	
	protected void setReturn_bytes(String return_byte) {
		
		this.return_bytes = return_byte;
	}
	
	protected void setSend_bytes(String send_bytes) {
		
		this.send_bytes = send_bytes;
	}
	
	
	public String getSend_bytes() {
		
		return send_bytes;
	}
	
	public boolean isSuccess() {
		
		JLog.d(TAG, result_name+"\nsend_bytes = "+send_bytes+"\nreturn_bytes = "+return_bytes);
		if(code == SUCCESS) {
			
			return true;
		}
		return false;
	}
	
	
	protected void init() {
		
		code = -1;
		errDes = null;
		return_bytes = null;
		send_bytes = null;
	}
	
}
