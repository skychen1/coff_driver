package ch.wrzw;


public class CPUMsger {
    private static CPUMsger mInstance;
    CPUOperator mCpuOperator;
    private static CPUMsger mCpuMsger = null;
    String JAR_VERSION = "20181113";
    boolean isBusy = false;

    static String TAG = "CPUMsger";

    static boolean doPrintLog = false;
    boolean init_success = false;

    public static CPUMsger getInstance() {

        if (mInstance == null) {

            mInstance = new CPUMsger();
        }
        return mInstance;
    }

    private CPUMsger() {

        mCpuOperator = CPUOperator.getCPUOperator();

    }

    public static CPUMsger getCPUMsger() {

        JLog.d(TAG, "get cpumsger has been");
        if (mCpuMsger == null) {

            JLog.d(TAG, "create a cpumsger");
            mCpuMsger = new CPUMsger();
        } else {

            JLog.d(TAG, "cpumsger has been created");
        }
        return mCpuMsger;
    }

    public boolean initDevice() {

        if (!init_success) {

            init_success = mCpuOperator.initDevice();
            JLog.d(TAG, "after init device init_success = " + init_success);
        }
        return init_success;
    }

    public synchronized boolean initDevice(String port_path) {

        if (!init_success) {

            init_success = mCpuOperator.initDevice(port_path);
            JLog.d(TAG, "after init device init_success = " + init_success);
        }
        return init_success;
    }


    public synchronized boolean closeDevice() {

        if (init_success) {

            init_success = false;
            mCpuOperator.closeDevice();
        }
        return true;

    }

    public boolean isAvailable() {

        JLog.d(TAG, "init_success = " + init_success);
        return init_success;
    }


    public void setDoPrintLog(boolean doPrint) {

        doPrintLog = doPrint;
    }

    public boolean getDoPrintLog() {

        return doPrintLog;
    }

    /*
    * open door
    * */
    public synchronized Result openDoor() {

        isBusy = true;
        Result mResult = mCpuOperator.openDoor();
        isBusy = false;
        return mResult;

    }

   /*
   * close door
   * */
    public synchronized Result closeDoor() {

        isBusy = true;
        Result mResult = mCpuOperator.closeDoor();
        isBusy = false;
        return mResult;
    }

    /*
* temp-sensor temp
* No:1-2
*/
    public synchronized TempSensorResult getTempSensorResult(int No) {

        isBusy = true;
        TempSensorResult tempSensorResult = mCpuOperator.getTempSensorResult(No);
        isBusy = false;
        return tempSensorResult;

    }
    //get crt version
    public VersionResult getCrtVersion() {

        isBusy = true;
        VersionResult mResult = mCpuOperator.getVersion();
        isBusy = false;

        return mResult;
    }

    /*
    * temp sensor state
    * */
    public TempSensorStateResult getTempSensorState() {

        isBusy = true;
        TempSensorStateResult mResult = mCpuOperator.getTempSensorState();
        isBusy = false;

        return mResult;
    }

    /*
    *get door state
    * */
    public DoorStateResult getDoorState() {
        isBusy = true;
        DoorStateResult mResult = mCpuOperator.getDoorState();
        isBusy = false;
        return mResult;
    }

    public String getJAR_VERSION() {

        return JAR_VERSION;
    }


}
