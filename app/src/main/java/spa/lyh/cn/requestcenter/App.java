package spa.lyh.cn.requestcenter;

import android.app.Application;

import spa.lyh.cn.lib_https.HttpClient;

public class App extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        HttpClient.getInstance(getApplicationContext()).setHttpFilter(new HttpFilter());//设置监听
    }
}
