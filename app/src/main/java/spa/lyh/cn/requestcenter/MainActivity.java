package spa.lyh.cn.requestcenter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import okhttp3.Call;
import spa.lyh.cn.ft_httpcenter.exception.BaseException;
import spa.lyh.cn.ft_httpcenter.model.JsonFromServer;
import spa.lyh.cn.lib_https.listener.DisposeDataListener;

public class MainActivity extends AppCompatActivity {

    private TextView aaa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aaa = findViewById(R.id.aaa);


        Call call = MyCenter.getNewVersion(this, new DisposeDataListener() {
            @Override
            public void onSuccess(Object responseObj) {
                JsonFromServer<UpdateInfo> jsonF = (JsonFromServer<UpdateInfo>) responseObj;
                if (jsonF.code == MyException.SUCCESS){
                    aaa.setText(jsonF.data.getVersionInfo());
                }
            }

            @Override
            public void onFailure(Object reasonObj) {

            }
        });
    }
}
