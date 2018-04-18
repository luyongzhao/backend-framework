package com.mobike.iotcloud.backend.framework.controller.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * REST分页对象
 *
 * @author luyongzhao
 */
public class RestPageLimit extends MapBean {
    private static final long serialVersionUID = 1L;

    public RestPageLimit() {
        this.page(1L);
        this.pageTotal(0L);
        this.list(new ArrayList<>());
    }

    public Long page() {
        return this.getLong("page");
    }

    public void page(Long page) {
        this.put("page", page);
    }

    public Long pageTotal() {
        return this.getLong("pageTotal");
    }

    public void pageTotal(Long pageTotal) {
        this.put("pageTotal", pageTotal);
    }

    public <T> List<T> list() {
        return (List<T>) this.get("list");
    }

    public <T> void list(List<T> list) {
        this.put("list", list);
    }
}
