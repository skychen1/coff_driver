package cof.ac.inter;


/**
 * @author chen ai jun
 */
public class MachineState {

    private byte[] states;
    int start = 6;
    Result mResult;

    MajorState majorState;
    long create_time;
    private static String TAG = "MachineState";


    protected MachineState() {

        mResult = new Result();
        create_time = System.currentTimeMillis();
        majorState = new MajorState();

    }

    protected void setStateByteArr(byte[] statebytes) {

        states = statebytes;
        if (states == null) {

            mResult.setCode(Result.RETURN_ERR);
            mResult.setErrDes("state bytes is null");
            CHLog.d(TAG, "state bytes is null");
        }
        mResult.return_bytes = DataSwitcher.bytes2Hex(statebytes);
        if (states == null || statebytes.length != 95) {

            mResult.setCode(Result.RETURN_ERR);
            mResult.setErrDes("state bytes  length is not 0x95");
            CHLog.d(TAG, "state bytes length is not 0x95");
            return;
        }
        if (!BytesChecker.hasReturn(statebytes)) {

            mResult.setCode(Result.RETURN_ERR);
            mResult.setErrDes("no return byte");
            CHLog.d(TAG, "no return byte");
            return;
        }
        if (statebytes[0] != (byte) 0xfe) {

            mResult.setCode(Result.RETURN_ERR);
            mResult.setErrDes("the start byte is not 0xfe");
            CHLog.d(TAG, "the start byte is not 0xfe");
            return;
        }
        if (statebytes[1] != (byte) 0x01) {

            mResult.setCode(Result.RETURN_ERR);
            mResult.setErrDes("the response fuction code is not 0x01");
            CHLog.d(TAG, "the response fuction code is not 0x01");
            return;
        }
        if (statebytes[5] != 0x57) {

            mResult.setCode(Result.RETURN_ERR);
            mResult.setErrDes("data length  is not 0x57");
            CHLog.d(TAG, "data length  is not 0x57");
            return;
        }

        byte[] crcBytes = CRC.getCRC(statebytes, 0, statebytes.length - 3);
        if (statebytes[statebytes.length - 1] != crcBytes[0] || statebytes[statebytes.length - 2] != crcBytes[1]) {

            mResult.code = Result.RETURN_ERR;
            mResult.errDes = "crc check of return bytes not pass";
            CHLog.d(TAG, "crc check of return bytes not pass");
            return;
        }
        mResult.code = Result.SUCCESS;
        CHLog.d(TAG, "all check pass");

        setMajorState();
    }


    private boolean isOn(byte curbyte, int NO) {

        if (NO < 0) {

            NO = 0;
        } else if (NO > 7) {

            NO = 7;
        }
        if (NO == 0) {

            if ((curbyte & (byte) 0x01) == (byte) 0x01) {

                return true;
            }
            return false;
        } else if (NO == 1) {

            if ((curbyte & (byte) 0x02) == (byte) 0x02) {

                return true;
            }
            return false;
        } else if (NO == 2) {

            if ((curbyte & (byte) 0x04) == (byte) 0x04) {

                return true;
            }
            return false;
        } else if (NO == 3) {

            if ((curbyte & (byte) 0x08) == (byte) 0x08) {

                return true;
            }
            return false;
        } else if (NO == 4) {

            if ((curbyte & (byte) 0x10) == (byte) 0x10) {

                return true;
            }
            return false;
        } else if (NO == 5) {

            if ((curbyte & (byte) 0x20) == (byte) 0x20) {

                return true;
            }
            return false;
        } else if (NO == 6) {

            if ((curbyte & (byte) 0x40) == (byte) 0x40) {

                return true;
            }
            return false;
        } else {

            if ((curbyte & (byte) 0x80) == (byte) 0x80) {

                return true;
            }
            return false;
        }

    }


    public Result getResult() {

        return mResult;
    }

    /**
     * to get the first set motors working state,when on return "true"
     *
     * @param NO is the serial number of a motor
     */
    public boolean isStirOn(int NO) {

        byte curbyte = states[start];
        return isOn(curbyte, NO);
    }

    /**
     * when on return "true"
     */
    public boolean isAirPumpOn() {

        byte curbyte = states[start];
        if ((curbyte & 0x02) == 0x02) {

            return true;
        }
        return false;
    }

    /**
     * to get container motor working state,when on return "true"
     *
     * @param NO is the serial number of the container motor
     */
    public boolean isContainerOn(int NO) {

        byte curbyte = states[start + 1];
        return isOn(curbyte, NO);
    }

    /**
     * to get the third set motors working state,when on return "true"
     *
     * @param NO is the serial number of a motor
     */
    public boolean isThirdSetMotorOn(int NO) {

        byte curbyte = states[start + 2];
        return isOn(curbyte, NO);
    }


    public boolean isGearPumpOn() {

        byte curbyte = states[start + 2];
        if ((curbyte & 0x40) == 0x40) {

            return true;
        }
        return false;
    }

    /**
     * to get the forth set taps working state,when on return "true"
     *
     * @param NO is the serial number of a motor
     */
    public boolean isForthSetMotorOn(int NO) {

        byte curbyte = states[start + 3];
        return isOn(curbyte, NO);
    }


    /**
     * to get the first set taps working state,when signal detected return "true"
     *
     * @param NO is the serial number of a sensor
     */
    public boolean isFirstSetTapOn(int NO) {

        byte curbyte = states[start + 4];
        return isOn(curbyte, NO);
    }

    /**
     * to get the first set sensors working state,when signal detected return "true"
     *
     * @param NO is the serial number of a sensor
     */
    public boolean isSecondSetTapOn(int NO) {

        byte curbyte = states[start + 5];
        return isOn(curbyte, NO);
    }


    /**
     * to get the first set sensors working state,when signal detected return "true"
     *
     * @param NO is the serial number of a sensor
     */
    public boolean isFirstSetSensorOn(int NO) {

        byte curbyte = states[start + 6];
        return isOn(curbyte, NO);
    }

    /**
     * to get the second set sensors working state,when signal detected return "true"
     *
     * @param NO is the serial number of a sensor
     */
    public boolean isSecondSetSensorOn(int NO) {

        byte curbyte = states[start + 7];
        return isOn(curbyte, NO);
    }

    /**
     * to get the third set sensors working state,when signal detected return "true"
     *
     * @param NO is the serial number of a sensor
     */
    public boolean isThirdSetSensorOn(int NO) {

        byte curbyte = states[start + 8];
        return isOn(curbyte, NO);
    }

    public boolean isForthSetSensorOn(int NO) {

        byte curbyte = states[start + 82];
        return isOn(curbyte, NO);
    }

    public boolean isFifthSetSensorOn(int NO) {

        byte curbyte = states[start + 83];
        return isOn(curbyte, NO);
    }

    public boolean is54ByteSensorOn(int NO) {

        byte curbyte = states[start + 84];
        return isOn(curbyte, NO);
    }

    public boolean isWasteLiquidInRightPlace() {
        return isFifthSetSensorOn(3);
    }

    /**
     * get the temperature of the pot 1
     */
    public float getPotTemp() {

        byte curByte1 = states[start + 10];
        byte curByte2 = states[start + 11];
        int d1 = DataSwitcher.byteToInt(curByte1);
        int d2 = DataSwitcher.byteToInt(curByte2);
        float temp = ((float) (d2 * 256 + d1)) / 10;
        return temp;
    }

    /**
     * get the temperature of the pot 2
     */
    public float getPot2Temp() {

        byte curByte1 = states[start + 12];
        byte curByte2 = states[start + 13];
        int d1 = DataSwitcher.byteToInt(curByte1);
        int d2 = DataSwitcher.byteToInt(curByte2);
        float temp = ((float) (d2 * 256 + d1)) / 10;
        return temp;
    }


    public int getPotPressure() {

        byte curByte1 = states[start + 16];
        byte curByte2 = states[start + 17];
        int d1 = DataSwitcher.byteToInt(curByte1);
        int d2 = DataSwitcher.byteToInt(curByte2);
        return d1 + d2 * 256;
    }

    /**
     * get the pressure of the pot 2
     */
    public int getPot2Pressure() {

        byte curByte1 = states[start + 14];
        byte curByte2 = states[start + 15];
        int d1 = DataSwitcher.byteToInt(curByte1);
        int d2 = DataSwitcher.byteToInt(curByte2);
        return d1 + d2 * 256;
    }

    public String getVersion() {

        byte curByte1 = states[start + 68];
        byte curByte2 = states[start + 69];
        return DataSwitcher.byte2Hex(curByte1) + "_" + DataSwitcher.byte2Hex(curByte2);
    }

    public String getIceVersion() {

        byte curByte1 = states[start + 90];
        byte curByte2 = states[start + 91];
        return DataSwitcher.byte2Hex(curByte1) + "_" + DataSwitcher.byte2Hex(curByte2);
    }

    public void setMajorState() {

        byte state_byte = states[start + 0x14];
        byte lowErr_byte = states[start + 0x16];
        byte highErr_byte = states[start + 0x17];
        majorState.setHighErr_byte(highErr_byte);
        majorState.setLowErr_byte(lowErr_byte);
        majorState.setState_byte(state_byte);
        if (state_byte == 0x0a) {

            CHLog.d(TAG, "statebyte 0x0a occured = " + DataSwitcher.bytes2Hex(states));
        } else {
            CHLog.d(TAG, "statebyte = " + DataSwitcher.byte2Hex(state_byte));
        }
    }


    /**
     * check if the coffee bean is enough or not
     * if enough return true,else return false
     */
    public boolean isBeanEnough() {

        return isFirstSetSensorOn(0);

    }

    /**
     * check if the waste container full or not
     * if enough return true,else return false
     */
    public boolean isWasteContainerFull() {

        return isFirstSetSensorOn(6);
    }

    /**
     * check if the cup shelf at the right place or not
     * if at the right place return true,else return false
     */
    public boolean isCupShelfRightPlace() {

        return isSecondSetSensorOn(4);
    }

    /**
     * check if the front door at the right place or not
     * if at the right place return true,else return false 0-���� 1-�ر�
     */
    public boolean isFrontDoorOpen() {

        if (isSecondSetSensorOn(7)) {

            return false;
        }
        return true;
    }

    public boolean hasCupOnShelf() {

        return isSecondSetSensorOn(6);
    }

    public boolean isLittleDoorOpen() {

        if (isSecondSetSensorOn(0)) {

            return false;
        }
        return true;
    }

    public boolean isWaterEnough() {

        // MajorState mState = getMajorState();
        if (majorState.getHighErr_byte() == 0x72) {

            return false;
        }
        return true;
    }


    /**
     * check if cup left
     */
    public boolean hasCup() {

        return isSecondSetSensorOn(1);
    }

    public String getWarnCode() {

        return DataSwitcher.byte2Hex(states[start + 0x55]);
    }

    public String getWarnDesCode() {

        return DataSwitcher.byte2Hex(states[start + 0x56]);
    }

    public ErrType getCurErrType() {

        // MajorState curState = getMajorState();
        if (majorState.getState_byte() != 0x00 || majorState.getState_byte() != 0x09) {

            return ErrType.STATECODE_ERR;
        }
        if (!isBeanEnough()) {

            return ErrType.BEAN_ERR;
        }
        if (isWasteContainerFull()) {

            return ErrType.WASTEFULL_ERR;
        }
        if (!isCupShelfRightPlace()) {

            return ErrType.CUPSHELF_ERR;
        }
        if (isFrontDoorOpen()) {

            return ErrType.DOOROPNE_ERR;
        }
        if (!isWaterEnough()) {

            return ErrType.WATER_ERR;
        }
        return ErrType.NO_Err;
    }

    public long getCreateTime() {

        return create_time;
    }

    public byte[] getStateBytes() {

        return states;
    }

    public MajorState getMajorState() {
        return majorState;
    }
}
