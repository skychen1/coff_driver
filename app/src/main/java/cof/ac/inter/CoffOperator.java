package cof.ac.inter;

import org.winplus.serial.utils.SerialPort;

import java.io.File;
import java.io.IOException;
import java.util.List;


class CoffOperator {


    SerialPort mPort = null;
    private static CoffOperator mCoffOperator;
    String portPath = "/dev/ttyO4";
    Communicator mCommunicator = null;
    static String TAG = "CoffOperator";


    final int WAIT_TIME = 100;

    private CoffOperator() {
    }

    public static CoffOperator getInstance() {

        if (mCoffOperator == null) {

            mCoffOperator = new CoffOperator();
        }
        return mCoffOperator;
    }


    /**
     * @param dev_path is serial port path o
     * @throws throw an Exception when open port failed
     */
    public boolean initDevice(String dev_path) {

        portPath = dev_path;
        return initDevice();
    }

    protected synchronized boolean initDevice() {

        File device = new File(portPath);
        try {
            mPort = new SerialPort(device, 115200, 0);
            if (mPort != null) {

                mCommunicator = new Communicator(mPort);
            }
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            CHLog.d(TAG, "SecurityException occured when init device");
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            CHLog.d(TAG, "SecurityException occured when init device");
        }
        if (mPort != null && mCommunicator != null && !mPort.canWork()) {

            CHLog.d(TAG, "init device success");
            return true;
        }
        CHLog.d(TAG, "init device failed");
        return false;
    }

    protected synchronized void closeDevice() {

        if (mCommunicator != null) {

            mCommunicator.close();
        }
        mPort = null;
        mCommunicator = null;
    }


    public synchronized Result makeCoffee(List<ContainerConfig> mConfigList) {

        Result mResult = null;
        for (int i = 0; i < 3; i++) {

            mResult = doMkCoffee(mConfigList, WAIT_TIME * (i + 1));
            if (mResult.getCode() == Result.SUCCESS) {

                break;
            }
        }
        return mResult;
    }

    private Result doMkCoffee(List<ContainerConfig> mConfigList, long wait_time) {

        CHLog.d(TAG, "------makeCoffee-------");
        Result mResult = new Result();
        if (mConfigList == null || mConfigList.size() == 0) {

            mResult.setCode(Result.DATA_ERR);
            return mResult;
        }
        byte[] byteArr = getMkCofInstruction(mConfigList);
        CHLog.d(TAG, "makeCoffee send byte = " + DataSwitcher.bytes2Hex(byteArr));
        doOperation(byteArr, wait_time);
        byte[] rbyteArr = mCommunicator.read(8);
        CHLog.d(TAG, "makeCoffee receive byte = " + DataSwitcher.bytes2Hex(rbyteArr));
        mResult.return_bytes = DataSwitcher.bytes2Hex(rbyteArr);
        if (rbyteArr[0] != (byte) 0xfe) {

            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "the start of return byte is not fe";
            return mResult;
        }
        if (rbyteArr[1] != (byte) 0x05) {

            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "the fuction byte is not 05";
            if (byteArr[1] == (byte) 0x85) {

                mResult.err_byte = DataSwitcher.byte2Hex(byteArr[2]);
            }
            return mResult;
        }
        byte[] crcBytes = CRC.getCRC(rbyteArr, 0, rbyteArr.length - 3);
        if (rbyteArr[rbyteArr.length - 1] != crcBytes[0] || rbyteArr[rbyteArr.length - 2] != crcBytes[1]) {

            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "crc check of return bytes not pass";
            return mResult;
        }
        mResult.code = Result.SUCCESS;
        return mResult;

    }

    public Result systemPower(int fucByte) {

        CHLog.d(TAG, "------systemPower-------");
        Result mResult = new Result();
        byte[] byteArr = {(byte) 0xFE, 0x0E, 0x00, (byte) fucByte, 0x00, 0x00, 0x00, 0x00};
        byte[] crcBytes = CRC.getCRC(byteArr, 0, byteArr.length - 3);
        byteArr[6] = crcBytes[1];
        byteArr[7] = crcBytes[0];
        CHLog.d(TAG, "systemPower send byte = " + DataSwitcher.bytes2Hex(byteArr));

        doOperation(byteArr, WAIT_TIME);
        byte[] rbyteArr = mCommunicator.read(8);
        CHLog.d(TAG, "systemPower receive byte = " + DataSwitcher.bytes2Hex(rbyteArr));
        mResult.return_bytes = DataSwitcher.bytes2Hex(rbyteArr);
        if (rbyteArr[0] != (byte) 0xfe) {

            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "the start of return byte is not fe";
            return mResult;
        }
        if (rbyteArr[1] != (byte) 0x0E) {

            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "the fuction byte is not 0e";
            if (byteArr[1] == (byte) 0x85) {

                mResult.err_byte = DataSwitcher.byte2Hex(byteArr[2]);
            }
            return mResult;
        }

        if (rbyteArr[2] != (byte) 0x00) {

            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "the data addr of return byte is not 00 ";
            return mResult;
        }
        crcBytes = CRC.getCRC(rbyteArr, 0, rbyteArr.length - 3);
        if (rbyteArr[rbyteArr.length - 1] != crcBytes[0] || rbyteArr[rbyteArr.length - 2] != crcBytes[1]) {

            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "crc check of return bytes not pass";
            return mResult;
        }
        mResult.code = Result.SUCCESS;
        return mResult;

    }


    public Result onTimePower(int fucByte, int h, int m) {

        if (h < 0) {

            h = 0;
        } else if (h >= 24) {

            h = 23;
        }

        if (m < 0) {

            m = 0;
        } else if (m >= 60) {

            m = 59;
        }
        CHLog.d(TAG, "------onTimePower-------");
        Result mResult = new Result();
        byte[] byteArr = {(byte) 0xFE, 0x0E, 0x01, (byte) fucByte, (byte) h, (byte) m, 0x00, 0x00};
        byte[] crcBytes = CRC.getCRC(byteArr, 0, byteArr.length - 3);
        byteArr[6] = crcBytes[1];
        byteArr[7] = crcBytes[0];
        CHLog.d(TAG, "onTimePower send byte = " + DataSwitcher.bytes2Hex(byteArr));

        doOperation(byteArr, WAIT_TIME);
        byte[] rbyteArr = mCommunicator.read(8);
        CHLog.d(TAG, "onTimePower receive byte = " + DataSwitcher.bytes2Hex(rbyteArr));
        mResult.return_bytes = DataSwitcher.bytes2Hex(rbyteArr);
        if (rbyteArr[0] != (byte) 0xfe) {

            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "the start of return byte is not fe";
            return mResult;
        }
        if (rbyteArr[1] != (byte) 0x0E) {

            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "the fuction byte is not 0e";
            if (byteArr[1] == (byte) 0x85) {

                mResult.err_byte = DataSwitcher.byte2Hex(byteArr[2]);
            }
            return mResult;
        }

        if (rbyteArr[2] != (byte) 0x01) {

            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "the data addr of return byte is not 01 ";
            return mResult;
        }
        crcBytes = CRC.getCRC(rbyteArr, 0, rbyteArr.length - 3);
        if (rbyteArr[rbyteArr.length - 1] != crcBytes[0] || rbyteArr[rbyteArr.length - 2] != crcBytes[1]) {

            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "crc check of return bytes not pass";
            return mResult;
        }
        mResult.code = Result.SUCCESS;
        return mResult;

    }

    public Result operatorMotor(DebugAction debugAction, boolean open) {

        if (open) {
            return Debug(debugAction, 127, 0);
        } else {
            return Debug(debugAction, 13, 0);
        }
    }


    public synchronized MachineState getMachineState() {

        CHLog.d(TAG, "----getMachineState---");
        byte[] byteArr = {(byte) 0xFE, 0x01, 0x00, 0x00, 0x00, (byte)0x57, (byte) 0xFB, (byte) 0x69};
        /*byte[] crcBytes = CRC.getCRC(byteArr, 0, byteArr.length - 3);
        byteArr[6] = crcBytes[1];
        byteArr[7] = crcBytes[0];*/
        CHLog.d(TAG, "send bytes = " + DataSwitcher.bytes2Hex(byteArr));
        doOperation(byteArr, WAIT_TIME);
        byte[] rbyteArr = mCommunicator.read(95);
        CHLog.d(TAG, "receive bytes = " + DataSwitcher.bytes2Hex(rbyteArr));
        MachineState mState = new MachineState();
        mState.setStateByteArr(rbyteArr);
        return mState;
    }

    public synchronized byte[] getMachineStateByByte() {

        CHLog.d(TAG, "----getMachineState---");
        byte[] byteArr = {(byte) 0xFE, 0x01, 0x00, 0x00, 0x00, (byte)0x57, (byte) 0xFB, (byte) 0x69};
        CHLog.d(TAG, "send bytes = " + DataSwitcher.bytes2Hex(byteArr));
        doOperation(byteArr, WAIT_TIME);
        byte[] rbyteArr = mCommunicator.read(95);
        CHLog.d(TAG, "receive bytes = " + DataSwitcher.bytes2Hex(rbyteArr));

        return rbyteArr;
    }

    public synchronized Result Debug(DebugAction curAction, int v1, int v2) {

        CHLog.d(TAG, "debug " + curAction);
        if (curAction == null) {

            return null;
        }
        if (v1 < 0) {

            v1 = 0;
        } else if (v1 > 255) {

            v1 = 255;
        }

        if (v2 < 0) {

            v2 = 0;
        } else if (v1 > 255) {

            v2 = 255;
        }
        Result mResult = new Result();
        byte fucbyte = DebugCodeMatcher.getFuctionByte(curAction);
        byte[] byteArr = {(byte) 0xFE, 0x02, 0x00, fucbyte, (byte) v1, (byte) v2, 0x00, 0x00};
        byte[] crcBytes = CRC.getCRC(byteArr, 0, byteArr.length - 3);
        byteArr[6] = crcBytes[1];
        byteArr[7] = crcBytes[0];
        CHLog.d(TAG, "send bytes = " + DataSwitcher.bytes2Hex(byteArr));
        doOperation(byteArr, WAIT_TIME);
        byte[] rbyteArr = mCommunicator.read(8);
        mResult.return_bytes = DataSwitcher.bytes2Hex(byteArr);
        CHLog.d(TAG, "recieve bytes = " + DataSwitcher.bytes2Hex(byteArr));

        if (rbyteArr[0] != (byte) 0xfe) {

            mResult.setCode(Result.RETURN_ERR);
            mResult.setErrDes("start byte is not fe");
            return mResult;
        }
        if (rbyteArr[1] != 0x02) {

            mResult.setCode(Result.RETURN_ERR);
            mResult.setErrDes("fuction byte is not 0x02");
            if (byteArr[1] == (byte) 0x82) {

                mResult.err_byte = DataSwitcher.byte2Hex(byteArr[2]);
            }
            return mResult;
        }
        crcBytes = CRC.getCRC(rbyteArr, 0, rbyteArr.length - 3);
        if (rbyteArr[rbyteArr.length - 1] != crcBytes[0] || rbyteArr[rbyteArr.length - 2] != crcBytes[1]) {

            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "crc check of return bytes not pass";
            return mResult;
        }


        mResult.code = Result.SUCCESS;
        return mResult;
    }

    private byte[] getMkCofInstruction(List<ContainerConfig> mConfigList) {

        byte[] datas = new byte[mConfigList.size() * 8];
        for (int i = 0; i < mConfigList.size(); i++) {

            ContainerConfig mConfig = mConfigList.get(i);
            byte[] mbytes = DataSwitcher.intToBytes(mConfig.container_id);
            datas[i * 8 + 0] = mbytes[3];
            mbytes = DataSwitcher.intToBytes(mConfig.water_interval);
            datas[i * 8 + 1] = mbytes[3];
            mbytes = DataSwitcher.intToBytes(mConfig.water_capacity);
            datas[i * 8 + 2] = mbytes[3];
            datas[i * 8 + 3] = mbytes[2];
            mbytes = DataSwitcher.intToBytes(mConfig.material_time);
            datas[i * 8 + 4] = mbytes[3];
            if (datas[i * 8 + 0] == (byte) 0x00) {

                datas[i * 8 + 7] = 1;
            } else {

                mbytes = DataSwitcher.intToBytes(mConfig.rotate_speed);
                datas[i * 8 + 5] = mbytes[3];
                mbytes = DataSwitcher.intToBytes(mConfig.stir_speed);
                datas[i * 8 + 6] = mbytes[3];
                if (mConfig.water_type == WaterType.HOT_WATER) {

                    CHLog.d(TAG, "makeCoffee water type hot= " + mConfig.water_type);
                    datas[i * 8 + 7] = 1;
                } else {

                    CHLog.d(TAG, "makeCoffee water type code= " + mConfig.water_type);
                    datas[i * 8 + 7] = 0;
                }
            }

        }
        byte[] byteArr = new byte[8 + datas.length];
        byteArr[0] = (byte) 0xfe;
        byteArr[1] = (byte) 0x05;
        byteArr[2] = 0x00;
        byteArr[3] = (byte) mConfigList.size(); // ��ȷ��
        byteArr[4] = 0x00;
        byteArr[5] = (byte) datas.length; //��ȷ��
        for (int i = 0; i < datas.length; i++) {

            byteArr[6 + i] = datas[i];
        }
        byte[] crcBytes = CRC.getCRC(byteArr, 0, byteArr.length - 3);
        byteArr[byteArr.length - 2] = crcBytes[1];
        byteArr[byteArr.length - 1] = crcBytes[0];
        return byteArr;
    }

    private void clearBuffer() {

        byte[] rbyteArr = null;
        do {

            Waiter.doWait(50);
            rbyteArr = mCommunicator.read(100);


        } while (BytesChecker.hasReturn(rbyteArr));
    }


    private void doOperation(byte[] byteArr, long wait_time) {

        if (mCommunicator != null) {

            clearBuffer();
            mCommunicator.write(byteArr);
            Waiter.doWait(wait_time);
        } else {

            CHLog.d(TAG, "mCommunicator == null");
        }
    }


}
