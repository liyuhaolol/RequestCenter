# 请求中心模块

封装的请求中心

- Github: https://github.com/liyuhaolol/HttpUtils

```gradle
    implementation 'com.github.liyuhaolol:RequestCenter:1.4.7'
```
## 1.4.7更新

- 例行更新http底层

## 1.4.6更新

- 例行更新http底层

## 1.4.5更新

- 增加post请求的下载方法
## 1.4.4更新

- 例行更新http底层
## 1.4.3更新

- 例行更新http底层
## 1.4.2更新

- response回调进行异常抓取，避免App异常闪退
## 1.4.1更新

- OKCode增加网络请求中的一些基础报错
## 1.4.0更新

- 例行更新http底层
## 1.3.9更新

- 例行更新http底层

## 1.3.8更新

- 例行更新http底层

## 1.3.7更新

- 例行更新http底层

## 1.3.6更新

- 同步http底层与json的拆分

## 1.3.5更新

- 例行更新http底层

## 1.3.4更新

- 例行更新http底层

## 1.3.3更新

- 迁移MavenCentral

## 1.3.2更新

- 例行更新http底层

## 1.3.1更新

- 例行更新http底层

## 1.3.0更新

- 增加几种创建请求的类型

## 1.2.9更新

- 更新Http底层库

## 1.2.8更新

- 更新Http底层库

## 1.2.7更新

- 请求中心支持传递，Intager，Float，Double，Long，Boolean等表单值，用于兼容Json数据类型转换

## 1.2.6更新

- 增加JsonString请求类型

## 1.2.5更新

- 更换json解析框架为fastjson2，使用本封装的用户推荐跟随更换，不更换不保证是否会出现BUG

## 1.2.4更新

- 尝试修正一个WindowManager$BadTokenException的异常

## 1.2.3更新

- 同步更新网络框架1.3.1，修正log的静态资源复用问题

## 1.2.2更新

- 同步更新网络框架1.3.0，请求池增加终止标识，和拦截器

## 1.2.0更新

- 同步更新网络框架1.2.8，增加请求池

## 1.1.9更新

- 同步更新网络框架1.2.7，增加请求时长显示

## 1.1.8更新

- 增加单一请求可以不走全局拦截器的逻辑

## 1.1.7更新

- 增加http请求的全局拦截器

## 1.1.6更新

- 更新http库

## 1.1.5更新

- 修正部分方法对成功回调的抓错逻辑

## 1.1.4更新

- 更新http库

## 1.1.3更新

- 更新http库

## 1.1.2更新

- 更新http库

## 1.1.1更新

- 将fastjson暴露出来

## 1.1.0更新

- 更新一些请求

## 1.0.9更新

- 替换到jitpack

## 1.0.8更新

- 更新lib_https到1.1.1版本

## 1.0.7更新

- 更新lib_https到1.1.0版本

## 1.0.6更新

- 更新lib_https到1.0.9版本

## 1.0.5更新

- 更新lib_https到1.0.8版本

## 1.0.4更新

- 更新lib_https到1.0.7版本

## 1.0.3更新

- 更新lib_https到1.0.6版本

## 1.0.2更新

- 最低sdk版本修改为21

## 1.0.1更新

- 去除exception里无用的拉美相关code

## 1.0.0更新

- 发布到云，修正dialog加载逻辑

## 框架引用方法

- 在gradle中:

```gradle
    allprojects {
        repositories {
            maven{url'https://jitpack.io'}
        }
    }
```

## 引用的主要类

- `api 'com.github.liyuhaolol:HttpUtils'` 对okhttp封装本体

- lib_https框架同时引入了

- `api 'com.squareup.okhttp3:okhttp'`

- `implementation 'com.alibaba.fastjson2:fastjson2'`

- `implementation 'com.github.liyuhaolol:IO'`

## 类说明

BaseException

```java
    //这里只是一个范例，更多code需要你自己继承这个类添加，这里只有最常用的3个
    public final static int SUCCESS = 200;//程序执行成功
    public final static int NODATA = 201;//程序执行成功，查询无数据
    public final static int REMOTE_ERROR = 500;//服务器错误
    //固定值
    public final static int NETWORK_ERROR = -1; // 网络错误
    public final static int JSON_ERROR = -2; // JSON格式错误
    public final static int OTHER_ERROR = -3; // 未定义错误
    public final static int CANCEL_REQUEST = -4; // 请求取消
    public final static int SERVER_ERROR = -5; // 服务器访问出错
    public final static int IO_ERROR = -6; // IO流错误
```

JsonFromServer<T>

```java
    /**
    *json统一的格式，T为实际类的泛型
    */
    public class JsonFromServer<T> implements Serializable {
        private static final long serialVersionUID = 1L;
        public int code;
        public String msg;
        public T data;
        public T info;

      }
```

BaseRequestCenter

```java
    /**
    * 发起post请求
    * @param activity 上下文
    * @param url 请求连接
    * @param params body内的请求参数键值对，RequestParams为键值对对象
    * @param headers header内的请求参数键值对，RequestParams为键值对对象
    * @param typeReference 泛型
    * @param loadingDialog 请求等待窗，展位，传null则没有等待窗
    * @param listener 请求回调
    * @return 这个请求的call
    */
    Call postRequest(final Activity activity, String url, RequestParams params, RequestParams headers, TypeReference<?> typeReference, final Dialog loadingDialog, final DisposeDataListener listener)

    /**
     * 主要给service这种无法得到activity对象的地方使用post请求
     * @param context 上下文
     * @param url 请求连接
     * @param params body内的请求参数键值对，RequestParams为键值对对象
     * @param headers header内的请求参数键值对，RequestParams为键值对对象
     * @param typeReference 泛型
     * @param listener 请求回调
     * @return 这个请求的call
     */
    Call postServiceRequest(Context context, String url, RequestParams params, RequestParams headers, TypeReference<?> typeReference, final DisposeDataListener listener)

    /**
     * 上传文件的请求
     * @param activity 上下文
     * @param url 请求连接
     * @param params body内的请求参数键值对，RequestParams为键值对对象
     * @param fileList 上传的文件列表
     * @param headers header内的请求参数键值对，RequestParams为键值对对象
     * @param typeReference 泛型
     * @param loadingDialog 请求等待窗，展位，传null则没有等待窗
     * @param listener 请求回调
     * @param uploadListener 上传过程中的回调
     * @return 这个请求的call
     */
    Call postFileRequest(final Activity activity, String url, RequestParams params, List<FilePart> fileList, RequestParams headers, TypeReference<?> typeReference, final Dialog loadingDialog, final DisposeDataListener listener, UploadProgressListener uploadListener)

    /**
    * 发起get请求
    * @param activity 上下文
    * @param url 请求连接
    * @param params body内的请求参数键值对，RequestParams为键值对对象
    * @param headers header内的请求参数键值对，RequestParams为键值对对象
    * @param typeReference 泛型
    * @param loadingDialog 请求等待窗，展位，传null则没有等待窗
    * @param listener 请求回调
    * @return 这个请求的call
    */
    Call getRequest(final Activity activity, String url, RequestParams params, RequestParams headers, TypeReference<?> typeReference, final Dialog loadingDialog, final DisposeDataListener listener)

    /**
     * 主要给service这种无法得到activity对象的地方使用get请求
     * @param context 上下文
     * @param url 请求连接
     * @param params body内的请求参数键值对，RequestParams为键值对对象
     * @param headers header内的请求参数键值对，RequestParams为键值对对象
     * @param typeReference 泛型
     * @param listener 请求回调
     * @return 这个请求的call
     */
    Call getServiceRequest(Context context, String url, RequestParams params, RequestParams headers, TypeReference<?> typeReference, final DisposeDataListener listener)

    /**
     * 文件下载的请求
     * @param context 上下文
     * @param url 请求连接
     * @param path 下载的路径
     * @param ioMethod 写入方法，HttpClient.OVERWRITE_FIRST，覆盖优先。HttpClient.ADD_ONLY，添加优先
     * @param listener 下载的监听回调
     * @return 这个请求的call
     */
    Call downloadFile(Context context,String url, String path, int ioMethod, DisposeDownloadListener listener)

    /**
     * 生成dialog
     * @param activity 上下文
     * @param className 类名
     * @return 对应的dialog对象
     */
    Dialog generateDialog(Activity activity, String className)
```
## 使用方法

在各个对应模块内新建对应的Center文件继承BaseRequestCenter，在对应文件内构建对应的接口<br/>

如有自定义的code值，可以选择继承BaseException然后添加进去<br/>

## 全局Dialog的使用方法

generateDialog();方法，可以通过类名反射使用dialog，如果是子集的module无法直接生成指定的dialog时，可以在对应子集的module中写死对应的dialog的类名，进行反射使用。
