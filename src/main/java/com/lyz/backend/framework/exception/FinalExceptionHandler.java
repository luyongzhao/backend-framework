package com.lyz.backend.framework.exception;

import com.lyz.backend.framework.controller.bean.RestResponse;
import org.springframework.boot.autoconfigure.web.ErrorController;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 在进入Controller之前，譬如请求一个不存在的地址，404错误。
 */
//@RestController
public class FinalExceptionHandler implements ErrorController{


    @Override
    public String getErrorPath() {
        return "/error";
    }

    @RequestMapping(value = "/error")
    public RestResponse error(HttpServletResponse resp, HttpServletRequest req) {
        // 错误处理逻辑
        return RestResponse.error("客户端请求错误！");
    }
}
