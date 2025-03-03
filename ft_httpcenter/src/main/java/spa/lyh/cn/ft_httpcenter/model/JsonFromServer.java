package spa.lyh.cn.ft_httpcenter.model;


import java.io.Serializable;

/**
 * Created by liyuhao on 2017/7/12.
 */

public class JsonFromServer<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    public int code;
    public String msg;
    public T data;
    public T info;

}
