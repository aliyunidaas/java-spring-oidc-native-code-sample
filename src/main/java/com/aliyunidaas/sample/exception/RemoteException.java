package com.aliyunidaas.sample.exception;

/**
 * Copyright (c)  Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/7/4 10:26 AM
 * @author: longqiuling
 **/
public class RemoteException extends RuntimeException {

    private static final long serialVersionUID = 3815212662244862728L;

    public RemoteException(String message, Throwable cause) {
        super(message, cause);
    }
}
