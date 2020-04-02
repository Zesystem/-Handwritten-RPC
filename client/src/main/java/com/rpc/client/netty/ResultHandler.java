package com.rpc.client.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @ClassName ResultHandler
 * @Description
 * @Author 戴书博
 * @Date 2020/4/2 16:23
 * @Version 1.0
 **/
public class ResultHandler extends ChannelInboundHandlerAdapter {


    private Object response;
    public Object getResponse() { return response; }

    @Override //读取服务器端返回的数据(远程调用的结果)
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        response = msg;
        ctx.close();
    }


}
