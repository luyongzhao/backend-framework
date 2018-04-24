package ${pkgName};

import java.util.List;

import org.apache.commons.lang.StringUtils;
import com.yumo.console.ectable.limit.MultyDBTableLimit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yumo.console.controller.ConsoleSupportController;
import com.yumo.core.rest.RestResponse;
import com.yumo.xschool.module.dto.${entityName};
import com.yumo.xschool.module.facade.DbFacade;
import com.yumo.xschool.module.facade.${entityName}Facade;

@Controller
@RequestMapping("/console/xschool/${entityName?uncap_first}")
public class ${entityName}Controller extends ConsoleSupportController
{

	@Autowired
	private ${entityName}Facade ${entityName?uncap_first}Facade;

	@Autowired
	private DbFacade dbFacade;
	/**
	 * 列表
	 * 
	 * @param model
	 * @param type
	 *            
	 * @return
	 */
	@RequestMapping("index")
	public String index(Model model)
	{
		MultyDBTableLimit<${entityName}> pageLimit = new MultyDBTableLimit<>();

		pageLimit.doQuery(${entityName}.class, dbFacade);
		
		return "index";
	}
	
	@RequestMapping("view")
	public String view(Model model, @RequestParam(value="id", required=false) String id)
	{
	
		if(StringUtils.isNotBlank(id))
		{
			model.addAttribute("dto", ${entityName?uncap_first}Facade.get(id));
		}
		
		
		return "view";
	}
	
	@RequestMapping("create")
	public @ResponseBody RestResponse create(Model model, ${entityName} ${entityName?uncap_first})
	{
		
		if(StringUtils.isBlank(${entityName?uncap_first}.getId()))
		{
			${entityName?uncap_first}Facade.save(${entityName?uncap_first});
		}else
		{
			${entityName?uncap_first}Facade.update(${entityName?uncap_first});
		}
		
		return RestResponse.succ(${entityName?uncap_first}.getId());
	}

	
}
