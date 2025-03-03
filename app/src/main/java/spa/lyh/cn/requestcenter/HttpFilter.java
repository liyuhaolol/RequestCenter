package spa.lyh.cn.requestcenter;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import okhttp3.Headers;
import spa.lyh.cn.lib_https.filter.BaseHttpFilter;


public class HttpFilter extends BaseHttpFilter {

    @Override
    public boolean dataFilter(@NonNull Context context, @NonNull String url, @NonNull Headers headers, @NonNull String stringBody) {
        Log.e("qwer","开心");
        return false;
    }
}
