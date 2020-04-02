package com.rpc.server.impl;

import com.rpc.common.service.HelloService;

/**
 * @ClassName HelloImpl
 * @Description
 * @Author 戴书博
 * @Date 2020/4/2 15:14
 * @Version 1.0
 **/
public class HelloServiceImpl implements HelloService {

    @Override
    public String helloRPC() {
        return "helloRPC";
    }
}
