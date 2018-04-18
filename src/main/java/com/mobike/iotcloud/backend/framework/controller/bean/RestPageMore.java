package com.mobike.iotcloud.backend.framework.controller.bean;

import java.util.List;

/**
 * REST分页对象
 *
 * @author luyongzhao
 */
public class RestPageMore extends MapBean {

    public static void main(String[] args) {
        System.out.println(new RestPageMore());
    }

    private static final long serialVersionUID = 1L;

    public boolean more() {
        return (Boolean) this.get("more");
    }

    public void more(Boolean more) {
        this.put("more", more);
    }

    public Integer idx() {
        return this.getInt("idx");
    }

    public void idx(Integer idx) {
        this.put("idx", idx);
    }

    public List<?> list() {
        return (List<?>) this.get("list");
    }

    public void list(List<?> list) {
        this.put("list", list);
    }
}
