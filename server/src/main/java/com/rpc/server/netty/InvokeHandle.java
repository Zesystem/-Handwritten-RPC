package com.rpc.server.netty;

import com.rpc.common.enity.ClassInfo;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.reflections.Reflections;

import java.lang.reflect.Method;
import java.util.Set;

/**
 * @ClassName InvokeHandle
 * @Description
 * @Author 戴书博
 * @Date 2020/4/2 15:53
 * @Version 1.0
 **/
public class InvokeHandle extends ChannelInboundHandlerAdapter {

    private static String interfacePath = "com.rpc.common.service";
    private static String implPath="com.rpc.server.impl";

    private String getImplClassName(ClassInfo classInfo)throws Exception{
        int lastDot = classInfo.getClassName().lastIndexOf(".");
        String interfaceName = classInfo.getClassName().substring(lastDot);

        Class superClass = Class.forName(interfacePath + interfaceName);

        Reflections reflections = new Reflections(implPath);
        //得到接口下面的实现类
        Set<Class> implClassSet = reflections.getSubTypesOf(superClass);
        if(implClassSet.size() == 0){
            System.out.println("未找到实现类");
            return null;
        }else if(implClassSet.size() > 1){
            System.out.println("找到多个实现类");
            return null;
        }else{
            Class[] classes = implClassSet.toArray(new Class[0]);
            return classes[0].getName();//实现类的名字
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws Exception {
        ClassInfo classInfo = (ClassInfo)msg;
        Object clazz = null;
        try {
            clazz = Class.forName(getImplClassName(classInfo)).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("类名提取异常");
        }
        Method method = clazz.getClass().getMethod(classInfo.getMethodName(),classInfo.getTypes());
        Object result = method.invoke(clazz,classInfo.getObjects());
        ctx.writeAndFlush(result);
        ctx.close();
    }

}
