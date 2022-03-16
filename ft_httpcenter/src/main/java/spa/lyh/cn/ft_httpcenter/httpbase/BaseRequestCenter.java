package spa.lyh.cn.ft_httpcenter.httpbase;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.alibaba.fastjson.TypeReference;

import java.lang.reflect.Constructor;
import java.util.List;

import okhttp3.Call;
import okhttp3.Headers;
import spa.lyh.cn.lib_https.HttpClient;
import spa.lyh.cn.lib_https.MultiRequestCenter;
import spa.lyh.cn.lib_https.listener.DisposeDataHandle;
import spa.lyh.cn.lib_https.listener.DisposeDataListener;
import spa.lyh.cn.lib_https.listener.DisposeDownloadListener;
import spa.lyh.cn.lib_https.listener.DisposeHeadListener;
import spa.lyh.cn.lib_https.listener.RequestResultListener;
import spa.lyh.cn.lib_https.listener.UploadProgressListener;
import spa.lyh.cn.lib_https.model.FilePart;
import spa.lyh.cn.lib_https.multirequest.MultiCall;
import spa.lyh.cn.lib_https.request.CommonRequest;
import spa.lyh.cn.lib_https.request.HeaderParams;
import spa.lyh.cn.lib_https.request.RequestParams;

public class BaseRequestCenter {

    /**
     * Post请求
     * @param context 上下文,如果使用了generateDialog()则这里必须传Activity
     * @param url 请求url
     * @param params body的键值对
     * @param headers header的键值对
     * @param typeReference 返回泛型
     * @param loadingDialog 网络请求中的加载dialog,传null则没有loading
     * @param useHttpFilter 这次请求是否经过HttpFilter过滤
     * @param listener 请求回调
     * @return 这次请求本身
     */
    protected static Call postRequest(final Context context, String url, RequestParams params, HeaderParams headers, TypeReference<?> typeReference, final Dialog loadingDialog,boolean useHttpFilter, final DisposeDataListener listener) {

        if (loadingDialog != null){
            loadingDialog.setCanceledOnTouchOutside(false);
            if (!loadingDialog.isShowing()){
                loadingDialog.show();
            }
        }
        //创建网络请求
        Call call = HttpClient.getInstance(context).sendResquest(CommonRequest.
                createPostRequest(url, params, headers, isApkInDebug(context)), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Headers headerData,Object bodyData) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                boolean sendToListener = true;
                if (HttpClient.getInstance(context).getHttpFilter() != null && useHttpFilter){
                    sendToListener = HttpClient.getInstance(context).getHttpFilter().dataFilter(context,url,headerData,bodyData);
                }

                if (listener != null && sendToListener){
                    listener.onSuccess(headerData,bodyData);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
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
        }, typeReference, isApkInDebug(context)));

        return call;
    }

    /**
     * Post发送文件请求
     * @param context 上下文,如果使用了generateDialog()则这里必须传Activity
     * @param url 请求url
     * @param params body的键值对
     * @param fileList 上传文件list
     * @param headers header的键值对
     * @param typeReference 返回泛型
     * @param loadingDialog 网络请求中的加载dialog,传null则没有loading
     * @param useHttpFilter 这次请求是否经过HttpFilter过滤
     * @param listener 请求回调
     * @param uploadListener 上传进度回调
     * @return 这次请求本身
     */
    protected static Call postFileRequest(final Context context, String url, RequestParams params, List<FilePart> fileList, HeaderParams headers, TypeReference<?> typeReference, final Dialog loadingDialog,boolean useHttpFilter, final DisposeDataListener listener, UploadProgressListener uploadListener) {
        if (loadingDialog != null){
            loadingDialog.setCanceledOnTouchOutside(false);
            if (!loadingDialog.isShowing()){
                loadingDialog.show();
            }
        }

        Call call = HttpClient.getInstance(context).sendResquest(CommonRequest.createUploadRequest(url,params,fileList,headers,isApkInDebug(context),uploadListener),
                new DisposeDataHandle(new DisposeDataListener() {
                    @Override
                    public void onSuccess(Headers headerData,Object bodyData) {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                        }
                        boolean sendToListener = true;
                        if (HttpClient.getInstance(context).getHttpFilter() != null && useHttpFilter){
                            sendToListener = HttpClient.getInstance(context).getHttpFilter().dataFilter(context,url,headerData,bodyData);
                        }
                        if (listener != null && sendToListener){
                            listener.onSuccess(headerData,bodyData);
                        }
                    }

                    @Override
                    public void onFailure(Object reasonObj) {
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
                },
                        typeReference,
                        isApkInDebug(context)));

        return call;
    }

    /**
     * 创建Post请求，但不发起请求
     * @param context 上下文,如果使用了generateDialog()则这里必须传Activity
     * @param url 请求url
     * @param params body的键值对
     * @param headers header的键值对
     * @return 这次请求本身
     */
    protected static Call createPostRequest(Context context, String url, RequestParams params, HeaderParams headers){
        return HttpClient.getInstance(context).createRequest(CommonRequest.createPostRequest(url,params,headers,isApkInDebug(context)));
    }

    /**
     * Get请求
     * @param context 上下文,如果使用了generateDialog()则这里必须传Activity
     * @param url 请求url
     * @param params body的键值对
     * @param headers header的键值对
     * @param typeReference 返回泛型
     * @param loadingDialog 网络请求中的加载dialog,传null则没有loading
     * @param useHttpFilter 这次请求是否经过HttpFilter过滤
     * @param listener 请求回调
     * @return 这次请求本身
     */
    protected static Call getRequest(final Context context, String url, RequestParams params, HeaderParams headers, TypeReference<?> typeReference, final Dialog loadingDialog,boolean useHttpFilter, final DisposeDataListener listener) {
        if (loadingDialog != null){
            loadingDialog.setCanceledOnTouchOutside(false);
            if (!loadingDialog.isShowing()){
                loadingDialog.show();
            }
        }
        //创建网络请求
        Call call = HttpClient.getInstance(context).sendResquest(CommonRequest.
                createGetRequest(url, params, headers, isApkInDebug(context)), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Headers headerData,Object bodyData) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                boolean sendToListener = true;
                if (HttpClient.getInstance(context).getHttpFilter() != null && useHttpFilter){
                    sendToListener = HttpClient.getInstance(context).getHttpFilter().dataFilter(context,url,headerData,bodyData);
                }
                if (listener != null && sendToListener){
                    listener.onSuccess(headerData,bodyData);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
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
        }, typeReference, isApkInDebug(context)));

        return call;
    }

    /**
     * 创建Get请求，但不发起请求
     * @param context 上下文,如果使用了generateDialog()则这里必须传Activity
     * @param url 请求url
     * @param params body的键值对
     * @param headers header的键值对
     * @return 这次请求本身
     */
    protected static Call createGetRequest(Context context, String url, RequestParams params, HeaderParams headers){
        return HttpClient.getInstance(context).createRequest(CommonRequest.createGetRequest(url,params,headers,isApkInDebug(context)));
    }

    /**
     * Put请求
     * @param context 上下文,如果使用了generateDialog()则这里必须传Activity
     * @param url 请求url
     * @param params body的键值对
     * @param headers header的键值对
     * @param typeReference 返回泛型
     * @param loadingDialog 网络请求中的加载dialog,传null则没有loading
     * @param useHttpFilter 这次请求是否经过HttpFilter过滤
     * @param listener 请求回调
     * @return 这次请求本身
     */
    protected static Call putRequest(final Context context, String url, RequestParams params, HeaderParams headers, TypeReference<?> typeReference, final Dialog loadingDialog,boolean useHttpFilter, final DisposeDataListener listener) {

        if (loadingDialog != null){
            loadingDialog.setCanceledOnTouchOutside(false);
            if (!loadingDialog.isShowing()){
                loadingDialog.show();
            }
        }
        //创建网络请求
        Call call = HttpClient.getInstance(context).sendResquest(CommonRequest.
                createputRequest(url, params, headers, isApkInDebug(context)), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Headers headerData,Object bodyData) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                boolean sendToListener = true;
                if (HttpClient.getInstance(context).getHttpFilter() != null && useHttpFilter){
                    sendToListener = HttpClient.getInstance(context).getHttpFilter().dataFilter(context,url,headerData,bodyData);
                }
                if (listener != null && sendToListener){
                    listener.onSuccess(headerData,bodyData);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
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
        }, typeReference, isApkInDebug(context)));

        return call;
    }

    /**
     * Delete请求
     * @param context 上下文,如果使用了generateDialog()则这里必须传Activity
     * @param url 请求url
     * @param params body的键值对
     * @param headers header的键值对
     * @param typeReference 返回泛型
     * @param loadingDialog 网络请求中的加载dialog,传null则没有loading
     * @param useHttpFilter 这次请求是否经过HttpFilter过滤
     * @param listener 请求回调
     * @return 这次请求本身
     */
    protected static Call deleteRequest(final Context context, String url, RequestParams params, HeaderParams headers, TypeReference<?> typeReference, final Dialog loadingDialog,boolean useHttpFilter, final DisposeDataListener listener) {

        if (loadingDialog != null){
            loadingDialog.setCanceledOnTouchOutside(false);
            if (!loadingDialog.isShowing()){
                loadingDialog.show();
            }
        }
        //创建网络请求
        Call call = HttpClient.getInstance(context).sendResquest(CommonRequest.
                createDeleteRequest(url, params, headers, isApkInDebug(context)), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(Headers headerData,Object bodyData) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                boolean sendToListener = true;
                if (HttpClient.getInstance(context).getHttpFilter() != null && useHttpFilter){
                    sendToListener = HttpClient.getInstance(context).getHttpFilter().dataFilter(context,url,headerData,bodyData);
                }
                if (listener != null && sendToListener){
                    listener.onSuccess(headerData,bodyData);
                }
            }

            @Override
            public void onFailure(Object reasonObj) {
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
        }, typeReference, isApkInDebug(context)));

        return call;
    }

    /**
     * Head请求
     * @param context 上下文
     * @param url 请求url
     * @param headers header的键值对
     * @param listener 请求回调
     * @return 这次请求本身
     */
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

    /**
     * 下载资源文件
     * @param context 上下文
     * @param url 请求url
     * @param path 下载的文件路径
     * @param ioMethod 文件写入方式，添加或者覆盖
     * @param listener 请求回调
     * @return 这次请求本身
     */
    protected static Call downloadFile(Context context,String url, String path, int ioMethod, DisposeDownloadListener listener) {
        return HttpClient.getInstance(context).downloadFile(context,CommonRequest.createGetRequest(url, null, null, isApkInDebug(context)),
                new DisposeDataHandle(listener, path, isApkInDebug(context)),ioMethod);
    }

    /**
     * 反射生成对应的dialog
     * @param context 上下文，如果不是activity则无法初始化dialog
     * @param className dialog的className用来反射
     * @return 返回dialog对象
     */
    public static Dialog generateDialog(Context context, String className){
        Dialog dialog = null;
        if (context instanceof Activity){
            try{
                Class clazz = Class.forName(className);
                Constructor constructor = clazz.getConstructor(Context.class);
                dialog = (Dialog) constructor.newInstance(context);
            }catch (Exception e){
                e.printStackTrace();
                if (isApkInDebug(context)){
                    Log.e("generateDialog","没能反射到对应dialog对象,初始化空白占位dialog");
                }
                dialog = new Dialog(context);
            }
        }else {
            if (isApkInDebug(context)){
                Log.e("generateDialog","无法初始化dialog,因为没有传入Activity");
            }
        }
        return dialog;
    }

    /**
     * 开始请求池任务
     * @param context 上下文
     * @param calls 请求的list
     * @param listener 结果回调
     */
    public static void startRequestPool(Context context, List<MultiCall> calls, RequestResultListener listener){
        MultiRequestCenter
                .get()
                .setDevMode(isApkInDebug(context))
                .addRequests(calls)
                .startTasks(listener);
    }

    /**
     * 开始请求池
     * @param context 上下文
     * @param listener 结果回调
     * @param calls 请求的数组
     */
    public static void startRequestPool(Context context, RequestResultListener listener, MultiCall... calls){
        MultiRequestCenter mrc = MultiRequestCenter
                .get()
                .setDevMode(isApkInDebug(context));
        for (int i = 0;i < calls.length;i++){
            mrc.addRequest(calls[i]);
        }
        mrc.startTasks(listener);
    }

    /**
     * 判断当前应用是否是debug状态
     * @param context 上下文
     * @return 是否是debug状态
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
