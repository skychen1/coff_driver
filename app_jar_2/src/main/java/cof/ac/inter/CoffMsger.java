package cof.ac.inter;

import java.util.List;

public class CoffMsger {

	
	private MachineState state;
	private CoffOperator mOperator;	
	private StateListener mStateListener;
	
	private Thread checkThread;	
	private boolean stopCheck = false;
	
	private static CoffMsger mInstance;
	
	private String version = "20180605";
	
	private boolean isBusy = false;
	
	private CoffMsger(){
		
		mOperator = CoffOperator.getInstance();
	}
	
	public static CoffMsger getInstance() {
		
		if(mInstance == null) {
			
			mInstance = new CoffMsger();
		}
		return mInstance;
	}
	
	
	public String getVersion() {
		
		return version;
	}
	
	public void setStateListener(StateListener listener) {
		
		mStateListener = listener;
	}
	
	public boolean init() {
	
		return mOperator.initDevice();
	}
	
	public boolean init(String dev_path) {
		
		return mOperator.initDevice(dev_path);
	}
	
	public synchronized Result mkCoffee(List<ContainerConfig> mConfigList) {
		
		isBusy = true;
		Result mResult = mOperator.makeCoffee(mConfigList);
		isBusy = false;
		return mResult;
	}
	
	public synchronized Result Debug(DebugAction curAction,int v1,int v2) {
		
		isBusy = true;
		Result mResult = mOperator.Debug(curAction, v1, v2);
		isBusy = false;
		return mResult;
	}
	
	public MachineState getCurState() {
		
		return mOperator.getMachineState();
	}
	
	public MachineState getLastMachineState() {
		
		return state;
	}
	
	
	public void startCheckState() {
		
		if(checkThread == null) {
			
			Runnable mRun = new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					
					while(true) {
						
						if(stopCheck) {
							
							doWait();
						}
						if(!isBusy) {
							
							state = mOperator.getMachineState();
							if(mStateListener != null) {
								
								mStateListener.stateArrived(state);
							}
						}
						Waiter.doWait(500);
					}

				}
			};
			checkThread = new Thread(mRun);
			checkThread.start();
		}else {
			
			doNotify();
		}
	}
	
	public void stopCheckState() {
		
		stopCheck = true;
	}
	
	private void doNotify(){
		
		synchronized (this) {
			
			notify();
		}
		
	}
	
	
	private void doWait() {
		
		try {
			
			synchronized (this) {
				
				wait();
			}
						
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	
}
