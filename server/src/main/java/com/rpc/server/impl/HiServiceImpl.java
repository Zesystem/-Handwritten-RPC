package com.rpc.server.impl;

import com.rpc.common.service.HiService;

/**
 * @ClassName HiImpl
 * @Description
 * @Author 戴书博
 * @Date 2020/4/2 15:14
 * @Version 1.0
 **/
public class HiServiceImpl implements HiService {

    @Override
    public String HiRPC(String name) {
        return "Hi" + name;
    }
}
