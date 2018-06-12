package com.lyz.backend.framework.dao.dto;

import java.io.Serializable;

/**
 * @author luyongzhao
 */
public interface DtoIdxSupport extends DtoSupport {

    public static Integer DEFALUT_IDX = 100000000;

    public Serializable getId();

    public Integer getIdx();

    public void setIdx(Integer idx);
}
