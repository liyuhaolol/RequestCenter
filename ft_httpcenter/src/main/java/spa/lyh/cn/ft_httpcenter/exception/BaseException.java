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
    public final static int COUNTRY_NO_CHANGE = 202;//国家列表未更新，不查询
    public final static int VERIFY = 203;//各种类型的审核中，请等待审核
    public final static int REQUEST_FAILED = 400;//失败，message里有失败原因
    public final static int LOGIN_FAILED = 401;//登录时用户名或密码不正确
    public final static int USER_NO_REGISTERED = 403;//登录时用户未注册
    public final static int COLLECTION_FAILED = 405;//收藏失败，该文章已经不存在
    public final static int REPORT_ALREADY = 406;//已举报过该用户
    public final static int FOLLOW = 414;//已关注或已取消关注该用户

    public BaseException(int ecode, String emsg) {
        super(ecode, emsg);
    }
}
