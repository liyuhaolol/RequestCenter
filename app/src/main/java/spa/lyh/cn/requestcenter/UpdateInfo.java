package spa.lyh.cn.requestcenter;

/**
 * Created by zhaolb on 2018/1/10.
 */

public class UpdateInfo{
    public final static int NORMAL = 1;
    public final static int FORCE = 2;
    String appLink;
    String appDescription;
    int appVersionCode;
    String appVersion;
    int forceUpdate;

    public String getAppDescription() {
        return appDescription;
    }

    public void setAppDescription(String appDescription) {
        this.appDescription = appDescription;
    }

    public int getAppVersionCode() {
        return appVersionCode;
    }

    public void setAppVersionCode(int appVersionCode) {
        this.appVersionCode = appVersionCode;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public int getForceUpdate() {
        return forceUpdate;
    }

    public void setForceUpdate(int forceUpdate) {
        this.forceUpdate = forceUpdate;
    }

    public String getAppLink() {
        return appLink;
    }

    public void setAppLink(String appLink) {
        this.appLink = appLink;
    }

    @Override
    public String toString() {
        return "UpdateInfo{" +
                "appLink='" + appLink + '\'' +
                ", appDescription='" + appDescription + '\'' +
                ", appVersionCode=" + appVersionCode +
                ", appVersion='" + appVersion + '\'' +
                ", forceUpdate=" + forceUpdate +
                '}';
    }
}
