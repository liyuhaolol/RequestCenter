package spa.lyh.cn.requestcenter;

import android.content.Context;
import android.util.Log;

import okhttp3.Headers;
import spa.lyh.cn.lib_https.filter.BaseHttpFilter;


public class HttpFilter extends BaseHttpFilter {
    @Override
    public boolean dataFilter(Context context, String url, Headers headers, Object bodyData) {
        Log.e("qwer","开心");
        return false;
    }
}
