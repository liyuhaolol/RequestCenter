package spa.lyh.cn.requestcenter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;

import com.alibaba.fastjson.TypeReference;

import okhttp3.Call;
import spa.lyh.cn.ft_httpcenter.httpbase.BaseRequestCenter;
import spa.lyh.cn.ft_httpcenter.model.JsonFromServer;
import spa.lyh.cn.lib_https.listener.DisposeDataListener;
import spa.lyh.cn.lib_https.request.HeaderParams;
import spa.lyh.cn.lib_https.request.RequestParams;

public class MyCenter extends BaseRequestCenter {

    /**
     * 检查更新
     *
     * @param context
     * @param listener
     */
    public static Call getNewVersion(Context context, DisposeDataListener listener) {
        RequestParams bodyParams = new RequestParams();
        bodyParams.put("versionType", "1");
        bodyParams.put("channelType", "XiaoMi");
        TypeReference typeReference = new TypeReference<JsonFromServer<UpdateInfo>>() {
        };
        return postRequest(context, "http://app.jrlamei.com/jrlmCMS/forApp/getChannelNewVersion.jspx", bodyParams, null, typeReference,generateDialog(context,""), false,listener);
    }


}
