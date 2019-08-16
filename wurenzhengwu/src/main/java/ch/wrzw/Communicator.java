package ch.wrzw;

import org.winplus.serial.utils.SerialPort;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

class Communicator {

	SerialPort mPort;
	FileOutputStream mFileOutputStream;
	FileInputStream mFileInputStream;
	static String TAG = "Communicator";
	
	protected Communicator(SerialPort serialPort) {
		
		mPort = serialPort;
	}
	
	public void write(byte[] bytearr){
		
		if(!mPort.canWork()){
			
			mPort.opeanSerial();
			mFileOutputStream =  mPort.getOutputStream();		
		}
			
		if(mFileOutputStream == null) {
			
			mFileOutputStream =  mPort.getOutputStream();	
		}
		try {
			if(mFileOutputStream != null){
				mFileOutputStream.write(bytearr);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public byte[] read() {
		
		byte[] buffer = {0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
		if(!mPort.canWork()){
			
			mPort.opeanSerial();
			mFileInputStream =  mPort.getInputStream();		
		}
			
		if(mFileInputStream == null) {
			
			mFileInputStream =  mPort.getInputStream();
		}
		try {
			if(mFileInputStream != null){
				mFileInputStream.read(buffer);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return buffer;
	}
	
	
	public byte[] read(int num) {
		
		byte[] byteArr = new byte[num];
		if(!mPort.canWork()){
			
			mPort.opeanSerial();
			mFileInputStream =  mPort.getInputStream();		
		}
			
		if(mFileInputStream == null) {
			
			mFileInputStream =  mPort.getInputStream();		
		}
		try {
			if(mFileInputStream != null){
				
				mFileInputStream.read(byteArr);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return byteArr;
	}
	
	
	
	public void close() {
		
		if(mPort.canWork()) {	
			
			mPort.closeSerial();
			mFileOutputStream = null;
			mFileInputStream = null;
		}
	}
}
