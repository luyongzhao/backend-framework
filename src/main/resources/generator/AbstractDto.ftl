package ${pkgName};

import com.mobike.iotcloud.backend.framework.dao.dto.DtoSupport;
import com.mobike.iotcloud.backend.framework.validate.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.hibernate.validator.constraints.*;


@Data
@ApiModel(description = "")
public class Abstract${table.name} implements DtoSupport
{
	private static final long serialVersionUID = 1L;
<#list table.columns as col>

	<#if col.isNull=="NO">
	@NotBlank(message = "{validation.notEmpty}")
	</#if>
	<#if (col.length>0) && col.type=="String">
	@Length(min=1,max=${col.length},message = "{validation.length}")
	</#if>
	@ApiModelProperty(value = "${col.comments}")
	private ${col.type} ${col.code};
</#list>

	public Abstract${table.name}()
	{

	}

<#list table.columns as col>
	public ${col.type} get${col.code?cap_first}()
	{
		return this.${col.code};
	}

	public void set${col.code?cap_first}(${col.type} ${col.code})
	{
		this.${col.code} = ${col.code};
	}
</#list>
}