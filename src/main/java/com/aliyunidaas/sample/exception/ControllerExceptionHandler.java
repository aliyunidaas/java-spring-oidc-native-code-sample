package com.aliyunidaas.sample.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;

/**
 * Copyright (c)  Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/7/7 10:50 AM
 * @author: longqiuling
 **/
@ControllerAdvice
public class ControllerExceptionHandler {

    private static final String ERROR = "error";

    private static final String ERROR_DESCRIPTION = "description";

    @ExceptionHandler({IOException.class})
    public String ioException(IOException ex,Model model) {
        model.addAttribute(ERROR, ex.getMessage());
        return "/error";
    }

    @ExceptionHandler({BizException.class})
    public String bizException(BizException ex, Model model) {
        model.addAttribute(ERROR, ex.getError());
        model.addAttribute(ERROR_DESCRIPTION, ex.getDescription());
        return "/error";
    }

    @ExceptionHandler({IllegalAccessException.class})
    public String illegalAccessException(IllegalAccessException ex, Model model) {
        model.addAttribute(ERROR, ex.getMessage());
        return "/error";
    }

    @ExceptionHandler({RuntimeException.class})
    public String runtimeException(RuntimeException ex, Model model) {
        model.addAttribute(ERROR, ex.getMessage());
        return "/error";
    }
}
