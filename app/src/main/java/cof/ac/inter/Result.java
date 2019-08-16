package cof.ac.inter;

import java.util.Random;

public class Result {

	int code = -1;
	public static final int SUCCESS = 1;
	public static final int RETURN_ERR = 2;
	public static final int DATA_ERR = 3;

	String err_byte = null;
	String errDes = null;
	String return_bytes = null;
	String id = null;
	
	protected Result() {
		
		id = System.currentTimeMillis()+"-"+new Random().nextInt(1000);
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
		
		if(code == 1) {
			
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

	public String getErr_byte() {

		return err_byte;
	}

	protected void init() {
		
		code = -1;
		errDes = null;
		return_bytes = null;
	}
	
}
