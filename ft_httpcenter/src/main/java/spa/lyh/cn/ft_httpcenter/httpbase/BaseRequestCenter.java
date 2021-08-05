package spa.lyh.cn.ft_httpcenter.httpbase;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.alibaba.fastjson.TypeReference;

import java.lang.reflect.Constructor;
import java.util.List;

import okhttp3.Call;
import okhttp3.Headers;
import spa.lyh.cn.lib_https.HttpClient;
import spa.lyh.cn.lib_https.listener.DisposeDataHandle;
import spa.lyh.cn.lib_https.listener.DisposeDataListener;
import spa.lyh.cn.lib_https.listener.DisposeDownloadListener;
import spa.lyh.cn.lib_https.listener.DisposeHeadListener;
import spa.lyh.cn.lib_https.listener.UploadProgressListener;
import spa.lyh.cn.lib_https.model.FilePart;
import spa.lyh.cn.lib_https.request.CommonRequest;
import spa.lyh.cn.lib_https.request.HeaderParams;
import spa.lyh.cn.lib_https.request.RequestParams;

public class BaseRequestCenter {

    //可以控制是否显示loadingDialog
    protected static Call postRequest(final Activity activity, String url, RequestParams params, HeaderParams headers, TypeReference<?> typeReference, final Dialog loadingDialog, final DisposeDataListener listener) {

        if (loadingDialog != null){
            loadingDialog.setCanceledOnTouchOutside(false);
            if (!loadingDialog.isShowing()){
                loadingDialog.show();
            }
        }
        //创建网络请求
        Call call = HttpClient.getInstance(activity).sendResquest(CommonRequest.
                createPostRequest(url, params, headers, isApkInDebug(activity)), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Headers headerData,Object bodyData) {
                if (!activity.isFinishing()){
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    boolean sendToListener = true;
                    if (HttpClient.getInstance(activity).getHttpFilter() != null){
                        sendToListener = HttpClient.getInstance(activity).getHttpFilter().dataFilter(activity,url,headerData,bodyData);
                    }

                    if (listener != null && sendToListener){
                        listener.onSuccess(headerData,bodyData);
                    }
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                if (!activity.isFinishing()) {
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    if (listener != null){
                        try {
                            listener.onFailure(reasonObj);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, typeReference, isApkInDebug(activity)));

        return call;
    }

    protected static Call postServiceRequest(Context context, String url, RequestParams params, HeaderParams headers, TypeReference<?> typeReference, final DisposeDataListener listener) {
        //创建网络请求
        Call call = HttpClient.getInstance(context).sendResquest(CommonRequest.
                createPostRequest(url, params, headers, isApkInDebug(context)), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Headers headerData,Object bodyData) {
                boolean sendToListener = true;
                if (HttpClient.getInstance(context).getHttpFilter() != null){
                    sendToListener = HttpClient.getInstance(context).getHttpFilter().dataFilter(context,url,headerData,bodyData);
                }
                if (listener != null && sendToListener){
                    listener.onSuccess(headerData,bodyData);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                if (listener != null){
                    try {
                        listener.onFailure(reasonObj);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }, typeReference, isApkInDebug(context)));

        return call;
    }

    protected static Call postFileRequest(final Activity activity, String url, RequestParams params, List<FilePart> fileList, HeaderParams headers, TypeReference<?> typeReference, final Dialog loadingDialog, final DisposeDataListener listener, UploadProgressListener uploadListener) {
        if (loadingDialog != null){
            loadingDialog.setCanceledOnTouchOutside(false);
            if (!loadingDialog.isShowing()){
                loadingDialog.show();
            }
        }

        Call call = HttpClient.getInstance(activity).sendResquest(CommonRequest.createUploadRequest(url,params,fileList,headers,isApkInDebug(activity),uploadListener),
                new DisposeDataHandle(new DisposeDataListener() {
                    @Override
                    public void onSuccess(Headers headerData,Object bodyData) {
                        if (!activity.isFinishing()){
                            if (loadingDialog != null && loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                            }
                            boolean sendToListener = true;
                            if (HttpClient.getInstance(activity).getHttpFilter() != null){
                                sendToListener = HttpClient.getInstance(activity).getHttpFilter().dataFilter(activity,url,headerData,bodyData);
                            }
                            if (listener != null && sendToListener){
                                listener.onSuccess(headerData,bodyData);
                            }
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
                        if (!activity.isFinishing()) {
                            if (loadingDialog != null && loadingDialog.isShowing()) {
                                loadingDialog.dismiss();
                            }
                            if (listener != null){
                                try{
                                    listener.onFailure(reasonObj);
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                },
                        typeReference,
                        isApkInDebug(activity)));

        return call;
    }

    //可以控制是否显示loadingDialog
    protected static Call getRequest(final Activity activity, String url, RequestParams params, HeaderParams headers, TypeReference<?> typeReference, final Dialog loadingDialog, final DisposeDataListener listener) {
        if (loadingDialog != null){
            loadingDialog.setCanceledOnTouchOutside(false);
            if (!loadingDialog.isShowing()){
                loadingDialog.show();
            }
        }
        //创建网络请求
        Call call = HttpClient.getInstance(activity).sendResquest(CommonRequest.
                createGetRequest(url, params, headers, isApkInDebug(activity)), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Headers headerData,Object bodyData) {
                if (!activity.isFinishing()){
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    boolean sendToListener = true;
                    if (HttpClient.getInstance(activity).getHttpFilter() != null){
                        sendToListener = HttpClient.getInstance(activity).getHttpFilter().dataFilter(activity,url,headerData,bodyData);
                    }
                    if (listener != null && sendToListener){
                        listener.onSuccess(headerData,bodyData);
                    }
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                if (!activity.isFinishing()){
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    try {
                        if (listener != null){
                            listener.onFailure(reasonObj);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }, typeReference, isApkInDebug(activity)));

        return call;
    }

    protected static Call getServiceRequest(Context context, String url, RequestParams params, HeaderParams headers, TypeReference<?> typeReference, final DisposeDataListener listener) {
        //创建网络请求
        Call call = HttpClient.getInstance(context).sendResquest(CommonRequest.
                createGetRequest(url, params, headers, isApkInDebug(context)), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Headers headerData,Object bodyData) {
                boolean sendToListener = true;
                if (HttpClient.getInstance(context).getHttpFilter() != null){
                    sendToListener = HttpClient.getInstance(context).getHttpFilter().dataFilter(context,url,headerData,bodyData);
                }
                if (listener != null && sendToListener){
                    listener.onSuccess(headerData,bodyData);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                if (listener != null){
                   try{
                       listener.onFailure(reasonObj);
                   }catch (Exception e){
                       e.printStackTrace();
                   }
                }
            }
        }, typeReference, isApkInDebug(context)));

        return call;
    }

    protected static Call putRequest(final Activity activity, String url, RequestParams params, HeaderParams headers, TypeReference<?> typeReference, final Dialog loadingDialog, final DisposeDataListener listener) {

        if (loadingDialog != null){
            loadingDialog.setCanceledOnTouchOutside(false);
            if (!loadingDialog.isShowing()){
                loadingDialog.show();
            }
        }
        //创建网络请求
        Call call = HttpClient.getInstance(activity).sendResquest(CommonRequest.
                createputRequest(url, params, headers, isApkInDebug(activity)), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Headers headerData,Object bodyData) {
                if (!activity.isFinishing()){
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    boolean sendToListener = true;
                    if (HttpClient.getInstance(activity).getHttpFilter() != null){
                        sendToListener = HttpClient.getInstance(activity).getHttpFilter().dataFilter(activity,url,headerData,bodyData);
                    }
                    if (listener != null && sendToListener){
                        listener.onSuccess(headerData,bodyData);
                    }
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                if (!activity.isFinishing()) {
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    if (listener != null){
                        try {
                            listener.onFailure(reasonObj);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, typeReference, isApkInDebug(activity)));

        return call;
    }

    protected static Call putServiceRequest(Context context, String url, RequestParams params, HeaderParams headers, TypeReference<?> typeReference, final DisposeDataListener listener) {
        //创建网络请求
        Call call = HttpClient.getInstance(context).sendResquest(CommonRequest.
                createputRequest(url, params, headers, isApkInDebug(context)), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Headers headerData,Object bodyData) {
                boolean sendToListener = true;
                if (HttpClient.getInstance(context).getHttpFilter() != null){
                    sendToListener = HttpClient.getInstance(context).getHttpFilter().dataFilter(context,url,headerData,bodyData);
                }
                if (listener != null && sendToListener){
                    listener.onSuccess(headerData,bodyData);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                if (listener != null){
                    try {
                        listener.onFailure(reasonObj);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }, typeReference, isApkInDebug(context)));

        return call;
    }

    protected static Call deleteRequest(final Activity activity, String url, RequestParams params, HeaderParams headers, TypeReference<?> typeReference, final Dialog loadingDialog, final DisposeDataListener listener) {

        if (loadingDialog != null){
            loadingDialog.setCanceledOnTouchOutside(false);
            if (!loadingDialog.isShowing()){
                loadingDialog.show();
            }
        }
        //创建网络请求
        Call call = HttpClient.getInstance(activity).sendResquest(CommonRequest.
                createDeleteRequest(url, params, headers, isApkInDebug(activity)), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Headers headerData,Object bodyData) {
                if (!activity.isFinishing()){
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    boolean sendToListener = true;
                    if (HttpClient.getInstance(activity).getHttpFilter() != null){
                        sendToListener = HttpClient.getInstance(activity).getHttpFilter().dataFilter(activity,url,headerData,bodyData);
                    }
                    if (listener != null && sendToListener){
                        listener.onSuccess(headerData,bodyData);
                    }
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                if (!activity.isFinishing()) {
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    if (listener != null){
                        try {
                            listener.onFailure(reasonObj);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, typeReference, isApkInDebug(activity)));

        return call;
    }

    protected static Call deleteServiceRequest(Context context, String url, RequestParams params, HeaderParams headers, TypeReference<?> typeReference, final DisposeDataListener listener) {
        //创建网络请求
        Call call = HttpClient.getInstance(context).sendResquest(CommonRequest.
                createDeleteRequest(url, params, headers, isApkInDebug(context)), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Headers headerData,Object bodyData) {
                boolean sendToListener = true;
                if (HttpClient.getInstance(context).getHttpFilter() != null){
                    sendToListener = HttpClient.getInstance(context).getHttpFilter().dataFilter(context,url,headerData,bodyData);
                }
                if (listener != null && sendToListener){
                    listener.onSuccess(headerData,bodyData);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
                if (listener != null){
                    try {
                        listener.onFailure(reasonObj);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        }, typeReference, isApkInDebug(context)));

        return call;
    }

    protected static Call headRequest(Context context, String url, HeaderParams headers, final DisposeHeadListener listener) {
        //创建网络请求
        Call call = HttpClient.getInstance(context).headResquest(CommonRequest.createHeadRequest(url,headers,isApkInDebug(context)),
                new DisposeDataHandle(new DisposeHeadListener() {
                    @Override
                    public void onSuccess(Headers headerData) {
                        if (listener != null){
                            listener.onSuccess(headerData);
                        }
                    }

                    @Override
                    public void onFailure(Object error) {
                        if (listener != null){
                            try {
                                listener.onFailure(error);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }
                }, isApkInDebug(context)));

        return call;
    }

    protected static Call downloadFile(Context context,String url, String path, int ioMethod, DisposeDownloadListener listener) {
        return HttpClient.getInstance(context).downloadFile(context,CommonRequest.createGetRequest(url, null, null, isApkInDebug(context)),
                new DisposeDataHandle(listener, path, isApkInDebug(context)),ioMethod);
    }

    /**
     * 反射生成对应的
     * @param activity
     * @param className
     * @return
     */
    public static Dialog generateDialog(Activity activity, String className){
        Dialog dialog;
        try{
            Class clazz = Class.forName(className);
            Constructor constructor = clazz.getConstructor(Context.class);
            dialog = (Dialog) constructor.newInstance(activity);
        }catch (Exception e){
            e.printStackTrace();
            dialog = new Dialog(activity);
        }
        return dialog;
    }

    /**
     * 判断当前应用是否是debug状态
     */
    private static boolean isApkInDebug(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
            return false;
        }
    }
}
