package spa.lyh.cn.ft_httpcenter.httpbase;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.util.Log;

import com.alibaba.fastjson2.JSONException;
import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.util.List;

import okhttp3.Call;
import okhttp3.Headers;
import spa.lyh.cn.lib_https.HttpClient;
import spa.lyh.cn.lib_https.MultiRequestCenter;
import spa.lyh.cn.lib_https.exception.OkHttpException;
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
import spa.lyh.cn.lib_https.response.base.CommonBase;

public class BaseRequestCenter {

    /**
     * Post请求
     * @param context 上下文,如果使用了generateDialog()则这里必须传Activity
     * @param url 请求url
     * @param params body的键值对
     * @param headers header的键值对
     * @param loadingDialog 网络请求中的加载dialog,传null则没有loading
     * @param useHttpFilter 这次请求是否经过HttpFilter过滤
     * @param listener 请求回调
     * @return 这次请求本身
     */
    protected static Call postRequest(final Context context, String url, RequestParams params, HeaderParams headers, final Dialog loadingDialog, boolean useHttpFilter, final DisposeDataListener listener) {

        if (loadingDialog != null){
            loadingDialog.setCanceledOnTouchOutside(false);
            if (!loadingDialog.isShowing()){
                showDialog(context,loadingDialog);
            }
        }
        //创建网络请求
        Call call = HttpClient.getInstance(context).sendResquest(CommonRequest.
                createPostRequest(url, params, headers, isApkInDebug(context)), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(@NotNull Headers headerData, @NotNull String stringBody) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    dismissDialog(context,loadingDialog);
                }
                boolean sendToListener = true;
                if (HttpClient.getInstance(context).getHttpFilter() != null && useHttpFilter){
                    sendToListener = HttpClient.getInstance(context).getHttpFilter().dataFilter(context,url,headerData,stringBody);
                }

                if (listener != null && sendToListener){
                    try{
                        listener.onSuccess(headerData,stringBody);
                    }catch (JSONException e){
                        e.printStackTrace();
                        listener.onFailure(new OkHttpException(OkHttpException.JSON_ERROR, CommonBase.JSON_CONVERT_ERROR,stringBody));
                    }catch (Exception e){
                        e.printStackTrace();
                        listener.onFailure(new OkHttpException(OkHttpException.OTHER_ERROR,CommonBase.EMPTY_MSG,stringBody));
                    }
                }
            }

            @Override
            public void onFailure(@NotNull OkHttpException error) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    dismissDialog(context,loadingDialog);
                }
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
     * Post发送文件请求
     * @param context 上下文,如果使用了generateDialog()则这里必须传Activity
     * @param url 请求url
     * @param params body的键值对
     * @param fileList 上传文件list
     * @param headers header的键值对
     * @param loadingDialog 网络请求中的加载dialog,传null则没有loading
     * @param useHttpFilter 这次请求是否经过HttpFilter过滤
     * @param listener 请求回调
     * @param uploadListener 上传进度回调
     * @return 这次请求本身
     */
    protected static Call postFileRequest(final Context context, String url, RequestParams params, List<FilePart> fileList, HeaderParams headers, final Dialog loadingDialog,boolean useHttpFilter, final DisposeDataListener listener, UploadProgressListener uploadListener) {
        if (loadingDialog != null){
            loadingDialog.setCanceledOnTouchOutside(false);
            if (!loadingDialog.isShowing()){
                showDialog(context,loadingDialog);
            }
        }

        Call call = HttpClient.getInstance(context).sendResquest(CommonRequest.createUploadRequest(url,params,fileList,headers,isApkInDebug(context),uploadListener),
                new DisposeDataHandle(new DisposeDataListener() {
                    @Override
                    public void onSuccess(@NotNull Headers headerData, @NotNull String stringBody) {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            dismissDialog(context,loadingDialog);
                        }
                        boolean sendToListener = true;
                        if (HttpClient.getInstance(context).getHttpFilter() != null && useHttpFilter){
                            sendToListener = HttpClient.getInstance(context).getHttpFilter().dataFilter(context,url,headerData,stringBody);
                        }
                        if (listener != null && sendToListener){
                            try{
                                listener.onSuccess(headerData,stringBody);
                            }catch (JSONException e){
                                e.printStackTrace();
                                listener.onFailure(new OkHttpException(OkHttpException.JSON_ERROR, CommonBase.JSON_CONVERT_ERROR,stringBody));
                            }catch (Exception e){
                                e.printStackTrace();
                                listener.onFailure(new OkHttpException(OkHttpException.OTHER_ERROR,CommonBase.EMPTY_MSG,stringBody));
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NotNull OkHttpException error) {
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            dismissDialog(context,loadingDialog);
                        }
                        if (listener != null){
                            try{
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
     * Post发送json请求
     * @param context 上下文,如果使用了generateDialog()则这里必须传Activity
     * @param url 请求url
     * @param json body的jsonString
     * @param headers header的键值对
     * @param loadingDialog 网络请求中的加载dialog,传null则没有loading
     * @param useHttpFilter 这次请求是否经过HttpFilter过滤
     * @param listener 请求回调
     * @return 这次请求本身
     */
    protected static Call postJsonRequest(final Context context, String url, String json, HeaderParams headers, final Dialog loadingDialog, boolean useHttpFilter, final DisposeDataListener listener) {

        if (loadingDialog != null){
            loadingDialog.setCanceledOnTouchOutside(false);
            if (!loadingDialog.isShowing()){
                showDialog(context,loadingDialog);
            }
        }
        //创建网络请求
        Call call = HttpClient.getInstance(context).sendResquest(CommonRequest.
                createPostJsonRequest(url, json, headers, isApkInDebug(context)), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(@NotNull Headers headerData, @NotNull String stringBody) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    dismissDialog(context,loadingDialog);
                }
                boolean sendToListener = true;
                if (HttpClient.getInstance(context).getHttpFilter() != null && useHttpFilter){
                    sendToListener = HttpClient.getInstance(context).getHttpFilter().dataFilter(context,url,headerData,stringBody);
                }

                if (listener != null && sendToListener){
                    try{
                        listener.onSuccess(headerData,stringBody);
                    }catch (JSONException e){
                        e.printStackTrace();
                        listener.onFailure(new OkHttpException(OkHttpException.JSON_ERROR, CommonBase.JSON_CONVERT_ERROR,stringBody));
                    }catch (Exception e){
                        e.printStackTrace();
                        listener.onFailure(new OkHttpException(OkHttpException.OTHER_ERROR,CommonBase.EMPTY_MSG,stringBody));
                    }
                }
            }

            @Override
            public void onFailure(@NotNull OkHttpException error) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    dismissDialog(context,loadingDialog);
                }
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

    protected static Call createJsonPostRequest(Context context, String url, String json, HeaderParams headers){
        return HttpClient.getInstance(context).createRequest(CommonRequest.createPostJsonRequest(url,json,headers,isApkInDebug(context)));
    }

    /**
     * Get请求
     * @param context 上下文,如果使用了generateDialog()则这里必须传Activity
     * @param url 请求url
     * @param params body的键值对
     * @param headers header的键值对
     * @param loadingDialog 网络请求中的加载dialog,传null则没有loading
     * @param useHttpFilter 这次请求是否经过HttpFilter过滤
     * @param listener 请求回调
     * @return 这次请求本身
     */
    protected static Call getRequest(final Context context, String url, RequestParams params, HeaderParams headers, final Dialog loadingDialog,boolean useHttpFilter, final DisposeDataListener listener) {
        if (loadingDialog != null){
            loadingDialog.setCanceledOnTouchOutside(false);
            if (!loadingDialog.isShowing()){
                showDialog(context,loadingDialog);
            }
        }
        //创建网络请求
        Call call = HttpClient.getInstance(context).sendResquest(CommonRequest.
                createGetRequest(url, params, headers, isApkInDebug(context)), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(@NotNull Headers headerData, @NotNull String stringBody) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    dismissDialog(context,loadingDialog);
                }
                boolean sendToListener = true;
                if (HttpClient.getInstance(context).getHttpFilter() != null && useHttpFilter){
                    sendToListener = HttpClient.getInstance(context).getHttpFilter().dataFilter(context,url,headerData,stringBody);
                }
                if (listener != null && sendToListener){
                    try{
                        listener.onSuccess(headerData,stringBody);
                    }catch (JSONException e){
                        e.printStackTrace();
                        listener.onFailure(new OkHttpException(OkHttpException.JSON_ERROR, CommonBase.JSON_CONVERT_ERROR,stringBody));
                    }catch (Exception e){
                        e.printStackTrace();
                        listener.onFailure(new OkHttpException(OkHttpException.OTHER_ERROR,CommonBase.EMPTY_MSG,stringBody));
                    }
                }
            }

            @Override
            public void onFailure(@NotNull OkHttpException error) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    dismissDialog(context,loadingDialog);
                }
                try {
                    if (listener != null){
                        listener.onFailure(error);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, isApkInDebug(context)));

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
     * @param loadingDialog 网络请求中的加载dialog,传null则没有loading
     * @param useHttpFilter 这次请求是否经过HttpFilter过滤
     * @param listener 请求回调
     * @return 这次请求本身
     */
    protected static Call putRequest(final Context context, String url, RequestParams params, HeaderParams headers, final Dialog loadingDialog,boolean useHttpFilter, final DisposeDataListener listener) {

        if (loadingDialog != null){
            loadingDialog.setCanceledOnTouchOutside(false);
            if (!loadingDialog.isShowing()){
                showDialog(context,loadingDialog);
            }
        }
        //创建网络请求
        Call call = HttpClient.getInstance(context).sendResquest(CommonRequest.
                createPutRequest(url, params, headers, isApkInDebug(context)), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(@NotNull Headers headerData, @NotNull String stringBody) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    dismissDialog(context,loadingDialog);
                }
                boolean sendToListener = true;
                if (HttpClient.getInstance(context).getHttpFilter() != null && useHttpFilter){
                    sendToListener = HttpClient.getInstance(context).getHttpFilter().dataFilter(context,url,headerData,stringBody);
                }
                if (listener != null && sendToListener){
                    try{
                        listener.onSuccess(headerData,stringBody);
                    }catch (JSONException e){
                        e.printStackTrace();
                        listener.onFailure(new OkHttpException(OkHttpException.JSON_ERROR, CommonBase.JSON_CONVERT_ERROR,stringBody));
                    }catch (Exception e){
                        e.printStackTrace();
                        listener.onFailure(new OkHttpException(OkHttpException.OTHER_ERROR,CommonBase.EMPTY_MSG,stringBody));
                    }
                }
            }

            @Override
            public void onFailure(@NotNull OkHttpException error) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    dismissDialog(context,loadingDialog);
                }
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
     * Put发送json请求
     * @param context 上下文,如果使用了generateDialog()则这里必须传Activity
     * @param url 请求url
     * @param json body的jsonString
     * @param headers header的键值对
     * @param loadingDialog 网络请求中的加载dialog,传null则没有loading
     * @param useHttpFilter 这次请求是否经过HttpFilter过滤
     * @param listener 请求回调
     * @return 这次请求本身
     */
    protected static Call putJsonRequest(final Context context, String url, String json, HeaderParams headers, final Dialog loadingDialog, boolean useHttpFilter, final DisposeDataListener listener) {

        if (loadingDialog != null){
            loadingDialog.setCanceledOnTouchOutside(false);
            if (!loadingDialog.isShowing()){
                showDialog(context,loadingDialog);
            }
        }
        //创建网络请求
        Call call = HttpClient.getInstance(context).sendResquest(CommonRequest.
                createPutJsonRequest(url, json, headers, isApkInDebug(context)), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(@NotNull Headers headerData, @NotNull String stringBody) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    dismissDialog(context,loadingDialog);
                }
                boolean sendToListener = true;
                if (HttpClient.getInstance(context).getHttpFilter() != null && useHttpFilter){
                    sendToListener = HttpClient.getInstance(context).getHttpFilter().dataFilter(context,url,headerData,stringBody);
                }

                if (listener != null && sendToListener){
                    try{
                        listener.onSuccess(headerData,stringBody);
                    }catch (JSONException e){
                        e.printStackTrace();
                        listener.onFailure(new OkHttpException(OkHttpException.JSON_ERROR, CommonBase.JSON_CONVERT_ERROR,stringBody));
                    }catch (Exception e){
                        e.printStackTrace();
                        listener.onFailure(new OkHttpException(OkHttpException.OTHER_ERROR,CommonBase.EMPTY_MSG,stringBody));
                    }
                }
            }

            @Override
            public void onFailure(@NotNull OkHttpException error) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    dismissDialog(context,loadingDialog);
                }
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

    protected static Call createPutRequest(Context context, String url, RequestParams params, HeaderParams headers){
        return HttpClient.getInstance(context).createRequest(CommonRequest.createPutRequest(url,params,headers,isApkInDebug(context)));
    }

    protected static Call createJsonPutRequest(Context context, String url, String json, HeaderParams headers){
        return HttpClient.getInstance(context).createRequest(CommonRequest.createPutJsonRequest(url,json,headers,isApkInDebug(context)));
    }


    /**
     * Delete请求
     * @param context 上下文,如果使用了generateDialog()则这里必须传Activity
     * @param url 请求url
     * @param params body的键值对
     * @param headers header的键值对
     * @param loadingDialog 网络请求中的加载dialog,传null则没有loading
     * @param useHttpFilter 这次请求是否经过HttpFilter过滤
     * @param listener 请求回调
     * @return 这次请求本身
     */
    protected static Call deleteRequest(final Context context, String url, RequestParams params, HeaderParams headers, final Dialog loadingDialog,boolean useHttpFilter, final DisposeDataListener listener) {

        if (loadingDialog != null){
            loadingDialog.setCanceledOnTouchOutside(false);
            if (!loadingDialog.isShowing()){
                showDialog(context,loadingDialog);
            }
        }
        //创建网络请求
        Call call = HttpClient.getInstance(context).sendResquest(CommonRequest.
                createDeleteRequest(url, params, headers, isApkInDebug(context)), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(@NotNull Headers headerData, @NotNull String stringBody) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    dismissDialog(context,loadingDialog);
                }
                boolean sendToListener = true;
                if (HttpClient.getInstance(context).getHttpFilter() != null && useHttpFilter){
                    sendToListener = HttpClient.getInstance(context).getHttpFilter().dataFilter(context,url,headerData,stringBody);
                }
                if (listener != null && sendToListener){
                    try{
                        listener.onSuccess(headerData,stringBody);
                    }catch (JSONException e){
                        e.printStackTrace();
                        listener.onFailure(new OkHttpException(OkHttpException.JSON_ERROR, CommonBase.JSON_CONVERT_ERROR,stringBody));
                    }catch (Exception e){
                        e.printStackTrace();
                        listener.onFailure(new OkHttpException(OkHttpException.OTHER_ERROR,CommonBase.EMPTY_MSG,stringBody));
                    }
                }
            }

            @Override
            public void onFailure(@NotNull OkHttpException error) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    dismissDialog(context,loadingDialog);
                }
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
     * Delete发送json请求
     * @param context 上下文,如果使用了generateDialog()则这里必须传Activity
     * @param url 请求url
     * @param json body的jsonString
     * @param headers header的键值对
     * @param loadingDialog 网络请求中的加载dialog,传null则没有loading
     * @param useHttpFilter 这次请求是否经过HttpFilter过滤
     * @param listener 请求回调
     * @return 这次请求本身
     */
    protected static Call deleteJsonRequest(final Context context, String url, String json, HeaderParams headers, final Dialog loadingDialog, boolean useHttpFilter, final DisposeDataListener listener) {

        if (loadingDialog != null){
            loadingDialog.setCanceledOnTouchOutside(false);
            if (!loadingDialog.isShowing()){
                showDialog(context,loadingDialog);
            }
        }
        //创建网络请求
        Call call = HttpClient.getInstance(context).sendResquest(CommonRequest.
                createDeleteJsonRequest(url, json, headers, isApkInDebug(context)), new DisposeDataHandle(new DisposeDataListener() {
            @Override
            public void onSuccess(@NotNull Headers headerData, @NotNull String stringBody) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    dismissDialog(context,loadingDialog);
                }
                boolean sendToListener = true;
                if (HttpClient.getInstance(context).getHttpFilter() != null && useHttpFilter){
                    sendToListener = HttpClient.getInstance(context).getHttpFilter().dataFilter(context,url,headerData,stringBody);
                }

                if (listener != null && sendToListener){
                    try{
                        listener.onSuccess(headerData,stringBody);
                    }catch (JSONException e){
                        e.printStackTrace();
                        listener.onFailure(new OkHttpException(OkHttpException.JSON_ERROR, CommonBase.JSON_CONVERT_ERROR,stringBody));
                    }catch (Exception e){
                        e.printStackTrace();
                        listener.onFailure(new OkHttpException(OkHttpException.OTHER_ERROR,CommonBase.EMPTY_MSG,stringBody));
                    }
                }
            }

            @Override
            public void onFailure(@NotNull OkHttpException error) {
                if (loadingDialog != null && loadingDialog.isShowing()) {
                    dismissDialog(context,loadingDialog);
                }
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


    protected static Call createDeleteRequest(Context context, String url, RequestParams params, HeaderParams headers){
        return HttpClient.getInstance(context).createRequest(CommonRequest.createDeleteRequest(url,params,headers,isApkInDebug(context)));
    }

    protected static Call createJsonDeleteRequest(Context context, String url, String json, HeaderParams headers){
        return HttpClient.getInstance(context).createRequest(CommonRequest.createDeleteJsonRequest(url,json,headers,isApkInDebug(context)));
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
                            try{
                                listener.onSuccess(headerData);
                            }catch (Exception e){
                                e.printStackTrace();
                                listener.onFailure(new OkHttpException(OkHttpException.OTHER_ERROR,CommonBase.EMPTY_MSG,""));
                            }
                        }
                    }

                    @Override
                    public void onFailure(OkHttpException error) {
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
            if (!((Activity) context).isFinishing()){
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
                .get(context)
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
                .get(context)
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

    /**
     * 尝试修正dialog的badtoken的错误
     * @param context
     * @param dialog
     */
    private static void showDialog(Context context,Dialog dialog){
        if (context instanceof Activity
                && !((Activity) context).isFinishing()) {
            try{
                dialog.show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private static void dismissDialog(Context context,Dialog dialog){
        if (context instanceof Activity
                && !((Activity) context).isFinishing()) {
            try{
                dialog.dismiss();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }


}
