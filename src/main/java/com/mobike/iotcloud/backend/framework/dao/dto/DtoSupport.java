package com.mobike.iotcloud.backend.framework.dao.dto;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import java.io.Serializable;

/**
 * @author luyongzhao
 */
@JsonSerialize(include = Inclusion.NON_NULL)
public interface DtoSupport extends Serializable {

}
