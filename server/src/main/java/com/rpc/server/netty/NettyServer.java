package com.rpc.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * @ClassName NettyServer
 * @Description
 * @Author 戴书博
 * @Date 2020/4/2 16:11
 * @Version 1.0
 **/
public class NettyServer {

    private int port;

    EventLoopGroup bossGroup = new NioEventLoopGroup();
    EventLoopGroup workerGroup = new NioEventLoopGroup();

    public NettyServer(int port){
        this.port = port;
    }

    public void start(){
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .localAddress(port)
                    .childHandler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            //编码器
                            pipeline.addLast("encoder",new ObjectEncoder());
                            //解码器
                            pipeline.addLast("decoder",new ObjectDecoder(Integer.MAX_VALUE,
                                    ClassResolvers.cacheDisabled(null)));
                            //服务器端业务处理类
                            pipeline.addLast(new InvokeHandle());
                        }
                    });
            ChannelFuture future = serverBootstrap.bind(port).sync();
            System.out.println("服务启动-------------");
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            bossGroup.shutdownGracefully(); workerGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) {
        new NettyServer(9999).start();
    }
}
