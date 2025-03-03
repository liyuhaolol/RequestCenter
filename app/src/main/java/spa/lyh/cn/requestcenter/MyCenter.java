package spa.lyh.cn.requestcenter;

import android.content.Context;


import okhttp3.Call;
import spa.lyh.cn.ft_httpcenter.httpbase.BaseRequestCenter;
import spa.lyh.cn.lib_https.HttpClient;
import spa.lyh.cn.lib_https.listener.DisposeDataListener;
import spa.lyh.cn.lib_https.listener.DisposeDownloadListener;
import spa.lyh.cn.lib_https.listener.DisposeHeadListener;
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
        bodyParams.put("appType", 1);
        bodyParams.put("siteId", 924958456908492800L);
        return postRequest(context, "https://ums.offshoremedia.net/app/versionInfo", bodyParams, null,generateDialog(context,""),false,listener);
    }

    public static Call downloadFile(Context context,String url, String path, DisposeDownloadListener listener){
        return downloadFile(context,url,path, HttpClient.OVERWRITE_FIRST,listener);
    }

    public static Call headRequest(Context context, String url, final DisposeHeadListener listener){
        return headRequest(context,url,null,listener);
    }


}
