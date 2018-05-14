package weixin.popular.bean.templatemessage;

import weixin.popular.bean.BaseResult;

public class IndustryInfo extends BaseResult{
	private IndustryClass primary_industry;
	private IndustryClass secondary_industry;
	public IndustryClass getPrimary_industry() {
		return primary_industry;
	}
	public void setPrimary_industry(IndustryClass primary_industry) {
		this.primary_industry = primary_industry;
	}
	public IndustryClass getSecondary_industry() {
		return secondary_industry;
	}
	public void setSecondary_industry(IndustryClass secondary_industry) {
		this.secondary_industry = secondary_industry;
	}
	

}
