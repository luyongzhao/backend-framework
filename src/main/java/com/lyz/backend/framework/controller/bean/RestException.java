package com.lyz.backend.framework.controller.bean;

public class RestException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private RestResponse resp;

    public RestException(String error) {
        super();
        this.resp = RestResponse.error(error);
    }

    public RestException(RestResponse resp) {
        super();
        this.resp = resp;
    }

    public RestResponse getResp() {
        return resp;
    }

    public void setResp(RestResponse resp) {
        this.resp = resp;
    }
}
