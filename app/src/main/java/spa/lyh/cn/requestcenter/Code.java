package spa.lyh.cn.requestcenter;

import spa.lyh.cn.ft_httpcenter.code.OKCode;

public class Code extends OKCode {
    public final static int COUNTRY_NO_CHANGE = 202;//国家列表未更新，不查询
    public final static int VERIFY = 203;//各种类型的审核中，请等待审核
    public final static int REQUEST_FAILED = 400;//失败，message里有失败原因
    public final static int LOGIN_FAILED = 401;//登录时用户名或密码不正确
    public final static int USER_NO_REGISTERED = 403;//登录时用户未注册
    public final static int COLLECTION_FAILED = 405;//收藏失败，该文章已经不存在
    public final static int REPORT_ALREADY = 406;//已举报过该用户
    public final static int FOLLOW = 414;//已关注或已取消关注该用户

}
