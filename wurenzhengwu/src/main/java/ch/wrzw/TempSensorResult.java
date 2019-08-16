package ch.wrzw;


public class TempSensorResult {
    int code = -1;
    int temp ;

    Result mResult;

    protected TempSensorResult() {

        mResult = new Result("TempSensorResult");
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getTemp() {
        return temp;
    }

    public void setTemp(int temp) {
        this.temp = temp;
    }

    public Result getResult() {
        return mResult;
    }

    public void setResult(Result mResult) {
        this.mResult = mResult;
    }
}
