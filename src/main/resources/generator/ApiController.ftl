package ${controllerPkgName};

import java.util.List;

import com.mobike.iotcloud.backend.framework.controller.BasicValidateController;
import com.mobike.iotcloud.backend.framework.controller.bean.AppUserAgent;
import com.mobike.iotcloud.backend.framework.controller.bean.MapBean;
import com.mobike.iotcloud.backend.framework.controller.bean.RestResponse;
import ${entityPkgName}.${entityName};
import ${servicePkgName}.${entityName}Service;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
* ${entityDesc}相关接口
* @author luyongzhao
*/
@RestController
@RequestMapping("${mappingPath}")
@Api(tags = "null",value="${entityDesc}")
@Slf4j
public class ${entityName}Controller extends BasicValidateController
{

    @Autowired
    private ${entityName}Service ${entityName?uncap_first}Service;

    @RequestMapping("/get")
    @ResponseBody
    public RestResponse get(String id){

        ${entityName} ${entityName?uncap_first} = ${entityName?uncap_first}Service.get(id);

        return RestResponse.succ(${entityName?uncap_first});
    }


    @RequestMapping("/insert")
    @ResponseBody
    public RestResponse insert(@Valid ${entityName} ${entityName?uncap_first}, BindingResult bindingResult){

        //校验请求参数
        validate(bindingResult);

        //获取账户id
        AppUserAgent appUserAgent = AppUserAgent.current();
        ${entityName?uncap_first}.setAccountId(appUserAgent.getAccountId());


        ${entityName?uncap_first}Service.create(${entityName?uncap_first});

        return RestResponse.succ(${entityName?uncap_first});
    }


    @RequestMapping("/list")
    @ResponseBody
    public RestResponse list(){

        List<${entityName}> all = ${entityName?uncap_first}Service.all();

        return RestResponse.succ(new MapBean("${entityName?uncap_first}List",all));
    }

    @RequestMapping("/update")
    @ResponseBody
    public RestResponse update(@Valid ${entityName} ${entityName?uncap_first}, BindingResult bindingResult){

        //校验请求参数
        validate(bindingResult);

        ${entityName?uncap_first}Service.update(${entityName?uncap_first});

        return RestResponse.succ(${entityName?uncap_first});
    }


}
