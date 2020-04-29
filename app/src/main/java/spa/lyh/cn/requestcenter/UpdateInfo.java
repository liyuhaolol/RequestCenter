package spa.lyh.cn.requestcenter;

import java.io.Serializable;

/**
 * Created by zhaolb on 2018/1/10.
 */

public class UpdateInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private String version;
    private String versionName;
    private String versionInfo;
    private int versionType;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVersionInfo() {
        return versionInfo;
    }

    public void setVersionInfo(String versionInfo) {
        this.versionInfo = versionInfo;
    }

    public int getVersionType() {
        return versionType;
    }

    public void setVersionType(int versionType) {
        this.versionType = versionType;
    }
}
