package weixin.popular.bean.templatemessage;

import weixin.popular.bean.BaseResult;

import java.util.List;

public class TemplateList extends BaseResult {
	private List<TemplateItem> template_list;

	public List<TemplateItem> getTemplate_list() {
		return template_list;
	}

	public void setTemplate_list(List<TemplateItem> template_list) {
		this.template_list = template_list;
	}
	
}
