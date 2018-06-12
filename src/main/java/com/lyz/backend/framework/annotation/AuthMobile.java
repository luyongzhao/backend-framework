package com.lyz.backend.framework.annotation;

import java.lang.annotation.*;

@Target(value = { ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface AuthMobile
{
	public static final class LoginType
	{
		public static final Integer checkLogin = 0;
		public static final Integer forceWxLogin = 1;
		public static final Integer forceRegisterLogin = 2;
	}

	/**
	 * 有这个信息就是强制微信授权oauth2_userinfo接口
	 * 
	 * @return
	 */
	public boolean forceWxLogin() default false;

	/**
	 * 检查是否登录:::可以通过微信拿到openID, unionID, 微信oauth2_base接口
	 * 
	 * @return
	 */
	public boolean checkLogin() default false;

	/**
	 * 强制注册登录（需要绑定手机号）
	 * 
	 * @return
	 */
	public boolean forceRegisterLogin() default false;

	/**
	 * js签名使用的公众号别名
	 */
	public String jsAPISignAccount() default "";
}
