package spa.lyh.cn.requestcenter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Headers;
import spa.lyh.cn.ft_httpcenter.model.JsonFromServer;
import spa.lyh.cn.lib_https.exception.OkHttpException;
import spa.lyh.cn.lib_https.listener.DisposeDataListener;
import spa.lyh.cn.lib_https.listener.DisposeHeadListener;
import spa.lyh.cn.lib_https.listener.DisposeMultiDownloadListener;
import spa.lyh.cn.lib_https.listener.RequestResultListener;
import spa.lyh.cn.lib_https.multirequest.MultiDownloadCall;

public class MainActivity extends AppCompatActivity {

    private TextView aaa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aaa = findViewById(R.id.aaa);
        MyCenter.headRequest(this, "https://www.baidu.com", new DisposeHeadListener() {
            @Override
            public void onSuccess(@NonNull Headers headerData) {
                Log.e("qwer",headerData.toString());
            }

            @Override
            public void onFailure(@NonNull OkHttpException error) {

            }
        });

        Call call = MyCenter.getNewVersion(this, new DisposeDataListener() {
            @Override
            public void onSuccess(@NonNull Headers headerData, @NonNull String stringBody) {
                JsonFromServer<UpdateInfo> jsonF = JSONObject.parseObject(stringBody,new TypeReference<JsonFromServer<UpdateInfo>>(){});
                if (jsonF.code == Code.SUCCESS){
                    aaa.setText(jsonF.info.toString());
                }
            }

            @Override
            public void onFailure(@NonNull OkHttpException error) {
                Log.e("qwer","报错");
            }
        });

        //showDialog(getBaseContext());
/*        MyCenter.downloadFile(this, "http://ums.offshoremedia.net/front/downloadApp?siteId=694841922577108992&appType=1", getExternalCacheDir().getAbsolutePath(), new DisposeDownloadListener() {
            @Override
            public void onSuccess(String filePath, String fileName) {
                aaa.setText(fileName);
            }

            @Override
            public void onFailure(Object reasonObj) {

            }

            @Override
            public void onProgress(boolean haveFileSize, int progress, String currentSize, String sumSize) {

            }
        });*/

/*        MyCenter.headRequest(this, "https://e97014cfe68179f7322039ee224c1e10.dlied1.cdntips.net/downv6.qq.com/qqweb/QQ_1/android_apk/Android_8.9.25.10005_537145595_64.apk", new DisposeHeadListener() {
            @Override
            public void onSuccess(Headers headerData) {
                headerData.toString();
                String size = convertFileSize(Long.parseLong(headerData.get("Content-Length")));
                Log.e("qwer","文件大小为："+ size);
            }

            @Override
            public void onFailure(Object error) {

            }
        });*/
        ArrayList<MultiDownloadCall> calls = new ArrayList<>();

        calls.add(MyCenter.createDownloadFile(
                this,
                "https://testcmpweb.sinoing.net/res/dxw/advImgs/2025/02/28/1f05d00d.2d7c.4890.983a.f60830a30d5a452.gif",
                new DisposeMultiDownloadListener() {
                    @Override
                    public boolean onSuccess(@NonNull String filePath, @NonNull String fileName) {
                        return true;
                    }

                    @Override
                    public boolean onFailure(@NonNull OkHttpException error) {
                        return true;
                    }

                    @Override
                    public void onProgress(boolean haveFileSize, int progress, @NonNull String currentSize, @NonNull String sumSize) {

                    }
                }));
        calls.add(MyCenter.createDownloadFile(
                this,
                "https://testcmpweb.sinoing.net/res/dxw/advImgs/2025/02/28/bf1d51a1.e938.459a.b445.7ad15110f10c996.gif",
                new DisposeMultiDownloadListener() {
                    @Override
                    public boolean onSuccess(@NonNull String filePath, @NonNull String fileName) {
                        return true;
                    }

                    @Override
                    public boolean onFailure(@NonNull OkHttpException error) {
                        return true;
                    }

                    @Override
                    public void onProgress(boolean haveFileSize, int progress, @NonNull String currentSize, @NonNull String sumSize) {

                    }
                }));
        calls.add(MyCenter.createDownloadFile(
                this,
                "https://testcmpweb.sinoing.net/res/dxw/advImgs/2025/02/28/5cfd236b.26ee.47e7.9ff7.94db4c4af13071.gif",
                new DisposeMultiDownloadListener() {
                    @Override
                    public boolean onSuccess(@NonNull String filePath, @NonNull String fileName) {
                        return true;
                    }

                    @Override
                    public boolean onFailure(@NonNull OkHttpException error) {
                        return true;
                    }

                    @Override
                    public void onProgress(boolean haveFileSize, int progress, @NonNull String currentSize, @NonNull String sumSize) {

                    }
                }));
        MyCenter.startDownloadRequestPool(
                this,
                calls,
                Objects.requireNonNull(getExternalFilesDir("splash")).getAbsolutePath(),
                new RequestResultListener() {
                    @Override
                    public void onFinish() {
                        Log.e("qwer","onFinish");
                    }

                    @Override
                    public void onTermination() {
                        Log.e("qwer","onTermination");
                    }

                    @Override
                    public void onCancel() {
                        Log.e("qwer","onCancel");
                    }
                });

        }

    private void showDialog(Context context){
        if (context instanceof Activity){
            Log.e("qwer","是activity");
        }else {
            Log.e("qwer","不是activity");
        }
    }

    public String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.2f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.2f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.2f KB", f);
        } else
            return String.format("%d B", size);
    }
}
