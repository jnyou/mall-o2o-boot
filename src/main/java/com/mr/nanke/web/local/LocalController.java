package com.mr.nanke.web.local;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("local")
public class LocalController {
	/**
	 * 绑定账号路由
	 */
	@RequestMapping("bindlocal")
	public String bindLocalAuth() {
		return "local/ownerbind";
	}
	
	/**
	 * 修改密码页
	 * @return
	 */
	@RequestMapping("changepsw")
	public String changePsw() {
		return "local/changepsw";
	}
	/***
	 * 登陆页面
	 * @return
	 */
	@RequestMapping("login")
	public String login() {
		return "local/ownerlogin";
	}
}
