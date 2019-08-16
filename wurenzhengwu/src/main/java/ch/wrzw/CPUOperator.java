package ch.wrzw;

import org.winplus.serial.utils.SerialPort;

import java.io.File;
import java.io.IOException;


class CPUOperator {

    static CPUOperator mOperator;
    static SerialPort mPort;
    static Communicator mCommunicator;
    static String TAG = "CPUOperator";
    final int LOOPCOUNT = 3;
    final int TIME = 100;
    int NUM = 50;
    private String portPath = "/dev/ttyXRUSB2";


    private CPUOperator() {
    }


    protected static CPUOperator getCPUOperator() {

        if (mOperator == null) {

            mOperator = new CPUOperator();
        }

        return mOperator;
    }

    protected synchronized boolean initDevice() {

        return initDevice(portPath);
    }

    protected synchronized boolean initDevice(String port_path) {

        File device = new File(port_path);
        try {
            mPort = new SerialPort(device, 9600, 0);
            if (mPort != null) {

                mCommunicator = new Communicator(mPort);
            }
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            JLog.d(TAG, "SecurityException occured when init device");
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            JLog.d(TAG, "SecurityException occured when init device");
        }
        if (mPort != null && mCommunicator != null) {

            JLog.d(TAG, "init device success");
            return true;
        }
        JLog.d(TAG, "init device failed");
        return false;
    }


    protected synchronized void closeDevice() {

        if (mCommunicator != null) {

            mCommunicator.close();
        }
        mPort = null;
        mCommunicator = null;
    }


    /*
     *open door
     */
    protected synchronized Result openDoor() {

        JLog.d(TAG, "---------openDoor--------");
        byte[] byteArr = {0x03, 0x03, 0x00, (byte) 0xF1, 0X31, (byte) 0XE4};
        return debug(byteArr, "openDoor");
    }


    /*
     * close door
     */
    protected synchronized Result closeDoor() {

        JLog.d(TAG, "---------closeDoor--------");
        byte[] byteArr = {0x03, 0x03, (byte) 0x01, (byte) 0xF1, 0x30, (byte) 0X74};
        return debug(byteArr, "closeDoor");
    }


    /*
* temp-sensor state
* No:1-2
*/
    protected synchronized TempSensorResult getTempSensorResult(int No) {

        JLog.d(TAG, "---------getTempSensorResult--------");
        byte[] crc = CRC.getTempSensorCRC(No);
        byte[] byteArr = {0x03, 0x07, (byte) No, (byte) 0xF1, crc[0], crc[1]};
        byte[] rbyteArr = null;
        byte[] newByteArr = null;
        NUM = byteArr.length;
        TempSensorResult tempSensorResult = new TempSensorResult();
        Result mResult = tempSensorResult.getResult();
        if (No > 2 || No < 1) {
            JLog.d(TAG, "error--temp sensor address must be 1~2");
            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "error--temp sensor address must be 1~2";
            tempSensorResult.setCode(Result.RETURN_ERR);
            tempSensorResult.setResult(mResult);
            return tempSensorResult;
        }
        doOperation(byteArr, TIME);
        rbyteArr = readBytes(NUM);
        mResult.return_bytes = DataSwitcher.bytes2Hex(rbyteArr);
        if (!DataChecker.hasData(rbyteArr)) {

            JLog.d(TAG, "error--getTempSensorResult--no_return");
            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "no data return !";
            tempSensorResult.setCode(Result.RETURN_ERR);
            tempSensorResult.setResult(mResult);
            return tempSensorResult;
        }
        newByteArr = rbyteArr;
        if (newByteArr == null) {

            JLog.d(TAG, "error--getTempSensorResult--checkBytes not pass");
            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "data not pass checking after dealt";
            tempSensorResult.setCode(Result.RETURN_ERR);
            tempSensorResult.setResult(mResult);
            return tempSensorResult;
        }

        if (newByteArr.length != 6) {
            JLog.d(TAG, "error--getTempSensorResult--checkBytes not pass");
            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "data length not 6";
            tempSensorResult.setCode(Result.RETURN_ERR);
            tempSensorResult.setResult(mResult);
            return tempSensorResult;
        }

        if (byteArr[0] != newByteArr[0]
                || byteArr[1] != newByteArr[1]
                || byteArr[2] != newByteArr[2]) {
            JLog.d(TAG, "error--getTempSensorResult--checkBytes not pass");
            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "data error";
            tempSensorResult.setCode(Result.RETURN_ERR);
            tempSensorResult.setResult(mResult);
            return tempSensorResult;
        }

        byte[] crcBytes = CRC.getCRC(rbyteArr, 0, rbyteArr.length - 3);

        if (rbyteArr[rbyteArr.length - 1] != crcBytes[1] || rbyteArr[rbyteArr.length - 2] != crcBytes[0]) {

            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "crc check of return bytes not pass";
            tempSensorResult.setCode(Result.RETURN_ERR);
            tempSensorResult.setResult(mResult);
            return tempSensorResult;
        }
        mResult.code = Result.SUCCESS;
        tempSensorResult.setCode(Result.SUCCESS);
        tempSensorResult.setTemp(DataSwitcher.byteToInt(newByteArr[3]));
        tempSensorResult.setResult(mResult);
        return tempSensorResult;
    }

    /*
  * temp sensor state
  */
    protected synchronized TempSensorStateResult getTempSensorState() {

        JLog.d(TAG, "---------getTempSensorState--------");
        byte[] byteArr = {0x03, 0x05, (byte) 0x01, (byte) 0x00, (byte) 0x11, (byte) 0XF1};
        byte[] rbyteArr = null;
        byte[] newByteArr = null;
        NUM = byteArr.length;
        TempSensorStateResult tempSensorStateResult = new TempSensorStateResult();
        Result mResult = tempSensorStateResult.getResult();
        doOperation(byteArr, TIME);
        rbyteArr = readBytes(NUM);
        mResult.return_bytes = DataSwitcher.bytes2Hex(rbyteArr);
        if (!DataChecker.hasData(rbyteArr)) {

            JLog.d(TAG, "error--getTempSensorState--no_return");
            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "no data return !";
            tempSensorStateResult.setCode(Result.RETURN_ERR);
            tempSensorStateResult.setResult(mResult);
            return tempSensorStateResult;
        }
        newByteArr = rbyteArr;
        if (newByteArr == null) {

            JLog.d(TAG, "error--getTempSensorState--checkBytes not pass");
            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "data not pass checking after dealt";
            tempSensorStateResult.setCode(Result.RETURN_ERR);
            tempSensorStateResult.setResult(mResult);
            return tempSensorStateResult;
        }

        if (newByteArr.length != 6) {
            JLog.d(TAG, "error--getTempSensorState--checkBytes not pass");
            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "data length not 6";
            tempSensorStateResult.setCode(Result.RETURN_ERR);
            tempSensorStateResult.setResult(mResult);
            return tempSensorStateResult;
        }

        byte[] crcBytes = CRC.getCRC(rbyteArr, 0, rbyteArr.length - 3);

        if (rbyteArr[rbyteArr.length - 1] != crcBytes[1] || rbyteArr[rbyteArr.length - 2] != crcBytes[0]) {

            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "crc check of return bytes not pass";
            tempSensorStateResult.setCode(Result.RETURN_ERR);
            tempSensorStateResult.setResult(mResult);
            return tempSensorStateResult;
        }
        mResult.code = Result.SUCCESS;
        tempSensorStateResult.setCode(Result.SUCCESS);
        tempSensorStateResult.setByteCode(newByteArr[3]);
        tempSensorStateResult.setResult(mResult);
        return tempSensorStateResult;
    }


    /*
* door state
*/
    protected synchronized DoorStateResult getDoorState() {

        JLog.d(TAG, "---------getDoorState--------");
        byte[] byteArr = {0x03, 0x01, (byte) 0x00, (byte) 0xF1, (byte) 0x90, (byte) 0X24};
        byte[] rbyteArr = null;
        byte[] newByteArr = null;
        NUM = byteArr.length;
        DoorStateResult doorStateResult = new DoorStateResult();
        Result mResult = doorStateResult.getResult();
        doOperation(byteArr, TIME);
        rbyteArr = readBytes(NUM);
        mResult.return_bytes = DataSwitcher.bytes2Hex(rbyteArr);
        if (!DataChecker.hasData(rbyteArr)) {

            JLog.d(TAG, "error--getDoorState--no_return");
            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "no data return !";
            doorStateResult.setCode(Result.RETURN_ERR);
            doorStateResult.setResult(mResult);
            return doorStateResult;
        }
        newByteArr = rbyteArr;
        if (newByteArr == null) {

            JLog.d(TAG, "error--getDoorState--checkBytes not pass");
            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "data not pass checking after dealt";
            doorStateResult.setCode(Result.RETURN_ERR);
            doorStateResult.setResult(mResult);
            return doorStateResult;
        }

        if (newByteArr.length != 6) {
            JLog.d(TAG, "error--getDoorState--checkBytes not pass");
            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "data length not 6";
            doorStateResult.setCode(Result.RETURN_ERR);
            doorStateResult.setResult(mResult);
            return doorStateResult;
        }

        byte[] crcBytes = CRC.getCRC(rbyteArr, 0, rbyteArr.length - 3);

        if (rbyteArr[rbyteArr.length - 1] != crcBytes[1] || rbyteArr[rbyteArr.length - 2] != crcBytes[0]) {

            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "crc check of return bytes not pass";
            doorStateResult.setCode(Result.RETURN_ERR);
            doorStateResult.setResult(mResult);
            return doorStateResult;
        }
        mResult.code = Result.SUCCESS;
        doorStateResult.setCode(Result.SUCCESS);
        doorStateResult.setDoorState(DoorState.NONE);
        if (newByteArr[2] == (byte) 0xf0) {

            doorStateResult.setDoorState(DoorState.DOOR_OPENED);
        } else if (newByteArr[2] == (byte) 0xff) {
            doorStateResult.setDoorState(DoorState.DOOR_CLOSED);

        } else if (newByteArr[2] == (byte) 0xf5) {
            doorStateResult.setDoorState(DoorState.DOOR_OPENING);
        }
        doorStateResult.setResult(mResult);
        return doorStateResult;
    }


    /*
    * get version
    * */

    protected synchronized VersionResult getVersion() {

        JLog.d(TAG, "---------getVersion--------");
        byte[] byteArr = {0x03, 0x00, 0x00, (byte) 0xF1, (byte) 0xC1, (byte) 0xE4};
        byte[] rbyteArr = null;
        byte[] newByteArr = null;
        NUM = 7;
        VersionResult versionResult = new VersionResult();
        Result mResult = versionResult.getResult();
        doOperation(byteArr, TIME);
        rbyteArr = readBytes(NUM);
        mResult.return_bytes = DataSwitcher.bytes2Hex(rbyteArr);
        if (!DataChecker.hasData(rbyteArr)) {

            JLog.d(TAG, "error--getVersion--no_return");
            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "no data return !";
            versionResult.setCode(Result.RETURN_ERR);
            versionResult.setResult(mResult);
            return versionResult;
        }
        newByteArr = rbyteArr;
        if (newByteArr == null) {

            JLog.d(TAG, "error--getVersion--checkBytes not pass");
            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "data not pass checking after dealt";
            versionResult.setCode(Result.RETURN_ERR);
            versionResult.setResult(mResult);
            return versionResult;
        }

        if (newByteArr.length != 7) {
            JLog.d(TAG, "error--getVersion--checkBytes not pass");
            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "data length not 6";
            versionResult.setCode(Result.RETURN_ERR);
            versionResult.setResult(mResult);
            return versionResult;
        }

        byte[] crcBytes = CRC.getCRC(rbyteArr, 0, rbyteArr.length - 3);
        if (rbyteArr[rbyteArr.length - 1] != crcBytes[1] || rbyteArr[rbyteArr.length - 2] != crcBytes[0]) {

            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "crc check of return bytes not pass";
            versionResult.setCode(Result.RETURN_ERR);
            versionResult.setResult(mResult);
            return versionResult;
        }

        mResult.code = Result.SUCCESS;
        versionResult.setCode(Result.SUCCESS);
        versionResult.setMainVersion(newByteArr[2]);
        versionResult.setNextVersion(newByteArr[3]);
        versionResult.setVersion(newByteArr[2] + "." + newByteArr[3]);
        versionResult.setResult(mResult);
        return versionResult;
    }

    private Result debug(byte[] byteArr, String debug_name) {

        JLog.d(TAG, "---------------debug------------------");
        byte[] rbyteArr = null;
        byte[] newByteArr = null;
        Result mResult = new Result(debug_name);
        doOperation(byteArr, TIME);
        NUM = byteArr.length;
        rbyteArr = readBytes(NUM);
        mResult.return_bytes = DataSwitcher.bytes2Hex(rbyteArr);
        if (!DataChecker.hasData(rbyteArr)) {

            JLog.d(TAG, "error--debug--no_return");
            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "no data return !";
        }
        newByteArr = rbyteArr;//DataChecker.getFilterBytes(rbyteArr);

        if (!checkBytes(byteArr, newByteArr)) {

            JLog.d(TAG, "error--debug--checkBytes not pass");
            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "data not pass checking after dealt";
            return mResult;
        }
        mResult.code = Result.SUCCESS;
        return mResult;
    }


    private byte[] readBytes(int n) {

        byte[] byteArr = null;
        if (mCommunicator != null) {

            Waiter.doWait(100);
            byteArr = mCommunicator.read(n);
        }
        return byteArr;
    }

    private void clearBuffer() {

        byte[] rbyteArr = null;
        do {

            Waiter.doWait(50);
            rbyteArr = readBytes(100);

        } while (BytesChecker.hasReturn(rbyteArr));
    }

    private void doOperation(byte[] byteArr, long wait_time) {

        if (mCommunicator != null) {

            clearBuffer();
            mCommunicator.write(byteArr);

            Waiter.doWait(wait_time);
        } else {

            JLog.d(TAG, "mCommunicator == null");
        }
    }

    private boolean checkBytes(byte[] sendByte, byte[] receiveByte) {

        if (receiveByte == null) {

            JLog.d(TAG, "error--checkBytes-filter fail");
            return false;
        } else if (sendByte.length != receiveByte.length) {

            JLog.d(TAG, "error--checkBytes-filter fail");
            return false;
        } else {

            for (int i = 0; i < sendByte.length; i++) {
                if (sendByte[i] != receiveByte[i]) {

                    JLog.d(TAG, "error--checkBytes-filter fail");

                    return false;
                }

            }
        }
        return true;
    }
}
