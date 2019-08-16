package ch.wrzw;


public class TempSensorStateResult {
    int code = -1;
    boolean isUpsOk;
    boolean isBoxSensorOk;
    byte byteCode;

    Result mResult;

    protected TempSensorStateResult() {

        mResult = new Result("TempSensorStateResult");
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setByteCode(byte byteCode) {
        this.byteCode = byteCode;
        if (byteCode == (byte) 0xf0) {
            setUpsOk(true);
            setBoxSensorOk(true);
        } else if (byteCode == (byte) 0xf1) {
            setUpsOk(false);
            setBoxSensorOk(true);
        } else if (byteCode == (byte) 0xf2) {
            setUpsOk(true);
            setBoxSensorOk(false);
        } else if (byteCode == (byte) 0xff) {
            setUpsOk(false);
            setBoxSensorOk(false);
        }
    }

    public boolean isUpsOk() {
        return isUpsOk;
    }

    public void setUpsOk(boolean upsOk) {
        isUpsOk = upsOk;
    }

    public boolean isBoxSensorOk() {
        return isBoxSensorOk;
    }

    public void setBoxSensorOk(boolean boxSensorOk) {
        isBoxSensorOk = boxSensorOk;
    }

    public Result getResult() {
        return mResult;
    }

    public void setResult(Result mResult) {
        this.mResult = mResult;
    }
}
