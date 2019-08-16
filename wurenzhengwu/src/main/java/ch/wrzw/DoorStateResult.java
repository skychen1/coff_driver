package ch.wrzw;


public class DoorStateResult {
    int code = -1;
    DoorState doorState;

    Result mResult;

    protected DoorStateResult() {

        mResult = new Result("DoorStateResult");
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DoorState getDoorState() {
        return doorState;
    }

    public void setDoorState(DoorState doorState) {
        this.doorState = doorState;
    }

    public Result getResult() {
        return mResult;
    }

    public void setResult(Result mResult) {
        this.mResult = mResult;
    }
}
