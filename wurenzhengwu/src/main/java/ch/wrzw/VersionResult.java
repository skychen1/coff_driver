package ch.wrzw;


public class VersionResult {
    int code = -1;
    byte mainVersion = 0;
    byte nextVersion = 0;

    String version = "";

    Result mResult;

    protected VersionResult() {

        mResult = new Result("VersionResult");
    }

    public int getCode() {
        return code;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getMainVersion() {
        return DataSwitcher.byteToInt(mainVersion);
    }

    public void setMainVersion(byte mainVersion) {
        this.mainVersion = mainVersion;
    }

    public int getNextVersion() {
        return DataSwitcher.byteToInt(nextVersion);
    }

    public void setNextVersion(byte nextVersion) {
        this.nextVersion = nextVersion;
    }

    public Result getResult() {
        return mResult;
    }

    public void setResult(Result mResult) {
        this.mResult = mResult;
    }

}
