package weixin.popular.bean;

import java.util.List;

public class UserInfoList extends BaseResult{

	private List<WeixinUserInfoResp> user_info_list;

	public List<WeixinUserInfoResp> getUser_info_list() {
		return user_info_list;
	}

	public void setUser_info_list(List<WeixinUserInfoResp> user_info_list) {
		this.user_info_list = user_info_list;
	}

}
