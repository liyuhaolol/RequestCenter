package spa.lyh.cn.ft_httpcenter.code;


import spa.lyh.cn.lib_https.exception.OkHttpException;

/**
 * Created by liyuhao on 2018/1/3.
 * 报错分析类，目前没有需要全局拦截的错误
 */

public class OKCode {
    public final static int NETWORK_ERROR = OkHttpException.NETWORK_ERROR; // the network relative error
    public final static int OTHER_ERROR = OkHttpException.OTHER_ERROR; // the unknow error
    public final static int CANCEL_REQUEST = OkHttpException.CANCEL_REQUEST; // cancel request
    public final static int SERVER_ERROR = OkHttpException.SERVER_ERROR; // server throw error code
    public final static int IO_ERROR = OkHttpException.IO_ERROR; // IO error during downloading
    //商议的状态码
    public final static int SUCCESS = 200;//程序执行成功
    public final static int NODATA = 201;//程序执行成功，查询无数据
    public final static int REMOTE_ERROR = 500;//服务器错误

}
