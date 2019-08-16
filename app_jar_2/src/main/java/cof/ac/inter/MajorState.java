package cof.ac.inter;



public class MajorState {
	
	final int NO_VALUE = -1;
	
	private int stateCode = -1;
	private int lowErrCode = -1;
	private int highErrCode = -1;
	
	private byte state_byte;
	private byte lowErr_byte;
	private byte highErr_byte;
	
	static String TAG =  "MajorState";
	
	private StateEnum curState;
	
	protected MajorState(){};

	public byte getLowErr_byte() {
		return lowErr_byte;
	}

	protected void setLowErr_byte(byte lowErr_byte) {
		
		this.lowErr_byte = lowErr_byte;
		lowErrCode = DataSwitcher.byteToInt(lowErr_byte);
	}

	public byte getHighErr_byte() {
		
		return highErr_byte;
	}

	protected void setHighErr_byte(byte highErr_byte) {
		
		this.highErr_byte = highErr_byte;
		highErrCode = DataSwitcher.byteToInt(highErr_byte);
	}
	
	
	public byte getState_byte() {
		return state_byte;
	}

	protected void setState_byte(byte state_byte) {
		this.state_byte = state_byte;
		stateCode = DataSwitcher.byteToInt(state_byte);
		if(state_byte == 0x00) {
			
			curState = StateEnum.IDLE;
		}else if(state_byte == 0x01) {
			
			curState = StateEnum.DOOR_OPNE;
		}
		else if(state_byte == 0x02) {
			
			curState = StateEnum.TESTING;
		}else if(state_byte == 0x03) {
			
			curState = StateEnum.CLEANING;
		}else if(state_byte == 0x04) {
			
			curState = StateEnum.STERILIZING;
		}else if(state_byte == 0x05) {
			
			curState = StateEnum.DOWN_CUP;
		}else if(state_byte == 0x06) {
			
			curState = StateEnum.MAKING;
		}else if(state_byte == 0x07) {
			
			curState = StateEnum.DOWN_POWER;
		}else if(state_byte == 0x08) {
			
			curState = StateEnum.FINISH;
		}else if(state_byte == 0x09) {
			
			curState = StateEnum.WARNING;
		}else if(state_byte == 0x0a) {
			
			curState = StateEnum.HAS_ERR;
		}else if(state_byte == 0x0c) {
			
			curState = StateEnum.HEAT_POT;
		}else if(state_byte == 0x0d) {
			
			curState = StateEnum.INIT;
		}else {
			
			curState = StateEnum.UNKNOW_STATE;
		}
		CHLog.d(TAG, "stateCode = "+stateCode);
	}

	public int getStateCode() {
		
		return stateCode;
	}
	
	public int getLowErrCode() {
		return lowErrCode;
	}
	
	public int getHighErrCode() {
		return highErrCode;
	}

	public StateEnum getCurStateEnum() {
		
		return curState;
	}
	
	

	
}
