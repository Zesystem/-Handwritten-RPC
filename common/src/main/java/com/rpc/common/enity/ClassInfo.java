package com.rpc.common.enity;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ClassInfo
 * @Description
 * @Author 戴书博
 * @Date 2020/4/2 15:18
 * @Version 1.0
 **/
@Data
public class ClassInfo implements Serializable {


    private String className;

    private String methodName;

    private Class<?>[] types;

    private Object[] objects;

}
