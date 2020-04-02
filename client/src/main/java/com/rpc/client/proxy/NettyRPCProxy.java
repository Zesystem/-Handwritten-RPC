package com.rpc.client.proxy;



import com.rpc.client.netty.NettyClient;
import com.rpc.common.enity.ClassInfo;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @ClassName ProxyUtil
 * @Description
 * @Author 戴书博
 * @Date 2020/4/2 16:27
 * @Version 1.0
 **/
public class NettyRPCProxy implements InvocationHandler {

    private static Class clazz;

    public static Object create(Class target){
        clazz = target;
        return Proxy.newProxyInstance(target.getClassLoader(),new Class[]{target},new NettyRPCProxy());
    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //封装Classinfo
        ClassInfo classInfo = new ClassInfo();
        classInfo.setClassName(clazz.getName());
        classInfo.setMethodName(method.getName());
        classInfo.setObjects(args);
        classInfo.setTypes(method.getParameterTypes());

        //开始netty发送数据
        return new NettyClient().start(classInfo);
    }

}
