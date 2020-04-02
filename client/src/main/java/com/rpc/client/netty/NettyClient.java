package com.rpc.client.netty;

import com.rpc.client.proxy.NettyRPCProxy;
import com.rpc.common.enity.ClassInfo;
import com.rpc.common.service.HelloService;
import com.rpc.common.service.HiService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * @ClassName NettyClient
 * @Description
 * @Author 戴书博
 * @Date 2020/4/2 16:41
 * @Version 1.0
 **/
public class NettyClient {

    private  EventLoopGroup group = new NioEventLoopGroup();
    private ResultHandler resultHandler =  new ResultHandler();

    public Object start(ClassInfo classInfo){
        try {
            Bootstrap client = new Bootstrap();

            client.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //编码器
                            pipeline.addLast("encoder",new ObjectEncoder());
                            //解码器
                            pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE,
                                    ClassResolvers.cacheDisabled(null)));
                            //服务器端业务处理类
                            pipeline.addLast("handle",resultHandler);
                        }
                    });
            ChannelFuture future = client.connect("127.0.0.1",9999).sync();
            future.channel().writeAndFlush(classInfo).sync();
            future.channel().closeFuture().sync();
        } catch (Exception e) {

        }finally {
            group.shutdownGracefully();
        }
        return resultHandler.getResponse();
    }


    public static void main(String[] args) {
        //第一次调用
        HelloService helloService = (HelloService) NettyRPCProxy.create(HelloService.class);
        System.out.println(helloService.helloRPC());
        //第二次调用
        HiService hiService = (HiService) NettyRPCProxy.create(HiService.class);
        System.out.println(hiService.HiRPC("baby"));

    }
}
