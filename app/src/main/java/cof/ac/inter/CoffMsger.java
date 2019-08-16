package cof.ac.inter;

import java.util.List;

public class CoffMsger {

    private static String TAG = "CoffMsger";
    static boolean doPrintLog = false;
    private MachineState state;
    private CoffOperator mOperator;
    private StateListener mStateListener;

    private boolean stopCheck = false;

    private static CoffMsger mInstance;

    private String JAR_VERSION = "20180827";

    private boolean isBusy = false;
    boolean init_success = false;
    private Thread mCheckThread = null;

    private CoffMsger() {

        mOperator = CoffOperator.getInstance();
        state = new MachineState();
    }

    public static CoffMsger getInstance() {

        if (mInstance == null) {

            mInstance = new CoffMsger();
        }
        return mInstance;
    }

    public String getJAR_VERSION() {

        return JAR_VERSION;
    }

    public void setStateListener(StateListener listener) {

        mStateListener = listener;
    }

    public boolean isAvailable() {

        CHLog.d(TAG, "init_success = " + init_success);
        return init_success;
    }

    public boolean initDevice() {

        if (!init_success) {

            init_success = mOperator.initDevice();
            CHLog.d(TAG, "after init device init_success = " + init_success);
        }
        return init_success;
    }

    public synchronized boolean initDevice(String port_path) {

        if (!init_success) {

            init_success = mOperator.initDevice(port_path);
            CHLog.d(TAG, "after init device init_success = " + init_success);
        }
        return init_success;
    }

    public synchronized Result mkCoffee(List<ContainerConfig> mConfigList) {

        isBusy = true;
        Result mResult = mOperator.makeCoffee(mConfigList);
        isBusy = false;
        return mResult;
    }

    public synchronized Result operatorMotor(DebugAction debugAction, boolean open) {
        isBusy = true;
        Result result = mOperator.operatorMotor(debugAction, open);
        isBusy = false;
        return result;
    }

    public synchronized Result systemPower(int fucByte) {

        isBusy = true;
        Result mResult = mOperator.systemPower(fucByte);
        isBusy = false;
        return mResult;
    }

    public synchronized Result onTimePower(int fucByte, int h, int m) {

        isBusy = true;
        Result mResult = mOperator.onTimePower(fucByte, h, m);
        isBusy = false;
        return mResult;
    }

    public synchronized Result Debug(DebugAction curAction, int v1, int v2) {

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

        if (mCheckThread != null) {

            doNotify();
        } else {

            checkState();
        }
    }

    public void stopCheckState() {

        stopCheck = true;
    }

    public synchronized boolean closeDevice() {

        if (init_success) {

            init_success = false;
            mOperator.closeDevice();
        }
        return true;

    }

    private void checkState() {

        if (mCheckThread == null) {

            stopCheck = false;
            Runnable mRun = new Runnable() {
                @Override
                public void run() {

                    while (!stopCheck) {

                        CHLog.d(TAG, "check state init_success = " + init_success);
                        if (!init_success) {

                            doWait();
                        }

                        if (!isBusy) {
                            state.setStateByteArr(mOperator.getMachineStateByByte());
//                            state = mOperator.getMachineState();
                            if (mStateListener != null) {

                                mStateListener.stateArrived(state);
                            }

                        }

                        Waiter.doWait(500);
                    }
                    mCheckThread = null;
                }
            };
            mCheckThread = new Thread(mRun);
            mCheckThread.start();
        }

    }


    private void doNotify() {

        synchronized (this) {

            notifyAll();

        }
    }

    private void doWait() {

        synchronized (this) {

            try {
                wait();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


}
