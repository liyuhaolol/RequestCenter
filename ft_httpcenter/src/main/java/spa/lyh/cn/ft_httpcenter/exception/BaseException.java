package spa.lyh.cn.ft_httpcenter.exception;


import spa.lyh.cn.lib_https.exception.OkHttpException;

/**
 * Created by liyuhao on 2018/1/3.
 * 报错分析类，目前没有需要全局拦截的错误
 */

public class BaseException extends OkHttpException {
    //OKhttp定义的4种状态，-1网络错误，-2Json错误，-3未知错误，-4取消请求
    //商议的状态码
    public final static int SUCCESS = 200;//程序执行成功
    public final static int NODATA = 201;//程序执行成功，查询无数据
    public final static int REMOTE_ERROR = 500;//服务器错误


    public BaseException(int ecode, String emsg) {
        super(ecode, emsg);
    }
}
