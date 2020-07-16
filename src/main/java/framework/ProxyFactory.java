package framework;

import protocol.http.HttpClient;
import provider.api.HelloService;
import register.Register;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Yuk on 2018/12/31.
 */
public class ProxyFactory<T> {

    public static <T> T getProxy(final Class interfaceClass){
        return (T)Proxy.newProxyInstance(
                interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {

            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

                // 调用哪个方法
                Invocation invocation = new Invocation(
                        interfaceClass.getName(),
                        method.getName(),
                        args,
                        new Class[]{String.class});

                // 模拟负载均衡，随机获取服务器
                URL url = Register.random(interfaceClass.getName());

                // 调用
                HttpClient httpClient = new HttpClient();
                return httpClient.post(url.getHostname(),url.getPort(),invocation);
            }
        });
    }
}
