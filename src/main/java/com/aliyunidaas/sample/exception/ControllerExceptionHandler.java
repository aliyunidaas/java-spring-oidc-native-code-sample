package com.aliyunidaas.sample.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * Copyright (c)  Alibaba Cloud Computing
 * Description:
 *
 * @date: 2022/7/7 10:50 AM
 * @author: longqiuling
 **/
@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    private static final String ERROR = "error";

    private static final String ERROR_DESCRIPTION = "description";

    private static final String ERROR_DETAIL = "detail";

    @ExceptionHandler({BizException.class})
    public String bizException(BizException ex, Model model) {
        model.addAttribute(ERROR, ex.getError());
        model.addAttribute(ERROR_DESCRIPTION, ex.getDescription());
        model.addAttribute(ERROR_DETAIL, ex);
        LOGGER.error("BizException:{}, error:{}, description:{} ", ex, ex.getError(), ex.getDescription());
        return "/error";
    }

    @ExceptionHandler({Exception.class})
    public String exception(Exception ex, Model model) {
        model.addAttribute(ERROR, ex.getMessage());
        model.addAttribute(ERROR_DETAIL, ex);
        LOGGER.error("Exception:", ex);
        return "/error";
    }

}
