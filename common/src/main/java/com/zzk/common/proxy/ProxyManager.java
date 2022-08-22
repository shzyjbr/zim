package com.zzk.common.proxy;

import com.alibaba.fastjson.JSONObject;
import com.zzk.common.exception.ZIMException;
import com.zzk.common.util.HttpClient;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

import static com.zzk.common.enums.StatusEnum.VALIDATION_FAIL;


public final class ProxyManager<T> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ProxyManager.class);

    private Class<T> clazz;

    private String url;

    private OkHttpClient okHttpClient;

    private Map<String, String> heads;

    /**
     *
     * @param clazz Proxied interface
     * @param url server provider url
     * @param okHttpClient http client
     */
    public ProxyManager(Class<T> clazz, String url, OkHttpClient okHttpClient) {
        this.clazz = clazz;
        this.url = url;
        this.okHttpClient = okHttpClient;
    }

    public ProxyManager(Class<T> clazz, String url, OkHttpClient okHttpClient, Map<String, String> heads) {
        this.clazz = clazz;
        this.url = url;
        this.okHttpClient = okHttpClient;
        this.heads = heads;
    }

    /**
     * Get proxy instance of api.
     * @return
     */
    public T getInstance() {
        return (T) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), new Class[]{clazz}, new ProxyInvocation());
    }


    private class ProxyInvocation implements InvocationHandler {
        //通过反射去进行网络调用，route服务器-> IM-Server  内容为转发推送消息， 方式为通过代理和反射， 去发送对应的参数
        //服务器的api和 接口一致，因此反射方法名和参数就可以作为api和url参数发送
        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            JSONObject jsonObject = new JSONObject();
            String serverUrl = url + "/" + method.getName() ;

            if (args != null && args.length > 1) {
                throw new ZIMException(VALIDATION_FAIL);
            }

            if (method.getParameterTypes().length > 0){
                Object para = args[0];
                Class<?> parameterType = method.getParameterTypes()[0];
                for (Field field : parameterType.getDeclaredFields()) {
                    field.setAccessible(true);
                    //从para里面拿出每个域对应的值，填充到json对象中
                    jsonObject.put(field.getName(), field.get(para));
                }
            }

            if (heads!=null) {
                //携带head发送请求
                return HttpClient.callWithHeads(okHttpClient, serverUrl, jsonObject.toJSONString(), heads);
            } else {
                return HttpClient.call(okHttpClient, jsonObject.toString(), serverUrl);
            }
        }
    }
}
