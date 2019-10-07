package com.mr.nanke.web.local;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mr.nanke.dto.LocalAuthExecution;
import com.mr.nanke.entity.LocalAuth;
import com.mr.nanke.entity.PersonInfo;
import com.mr.nanke.enums.LocalAuthStateEnum;
import com.mr.nanke.exceptions.LocalAuthOperationException;
import com.mr.nanke.service.LocalAuthService;
import com.mr.nanke.utils.CodeUtil;
import com.mr.nanke.utils.HttpServletRequestUtil;
import com.mr.nanke.utils.MD5;

@Controller
@RequestMapping("local")
public class LocalAuthController {

	@Autowired
	private LocalAuthService localAuthService;
	
	/***
	 * 用户信息与平台绑定
	 * @param request
	 * @return
	 */
	@RequestMapping("bindlocalauth")
	@ResponseBody
	public Map<String,Object> bindLocalAuth(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入验证码错误");
			return modelMap;
		}
		//获取输入的账号
		String username = HttpServletRequestUtil.getString(request, "username");
		//获取输入的密码
		String password = HttpServletRequestUtil.getString(request, "password");
		//从session中获取userID
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		//非空判断
		if(username != null && password != null && user != null && user.getUserId() != null) {
			LocalAuth localAuth = new LocalAuth();
			localAuth.setUsername(username);
			localAuth.setPassword(password);
			localAuth.setPersonInfo(user);
			LocalAuthExecution lae = localAuthService.bindLocalAuth(localAuth);
			if(lae.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
				modelMap.put("success", true);
			}else {
				modelMap.put("success", false);
				modelMap.put("errMsg", lae.getStateInfo());
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名和密码不能为空");
		}
		return modelMap;
	}
	
	/***
	 * 修改平台密码
	 * @param request
	 * @return
	 */
	@RequestMapping("changelocalauthpwd")
	@ResponseBody
	public Map<String,Object> changeLocalAuthPwd(HttpServletRequest request){
		Map<String, Object> modelMap = new HashMap<String, Object>();
		if (!CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入验证码错误");
			return modelMap;
		}
		//获取输入的账号
		String username = HttpServletRequestUtil.getString(request, "username");
		//获取原密码
		String password = HttpServletRequestUtil.getString(request, "password");
		//获取新密码
		String newPassword = HttpServletRequestUtil.getString(request, "newPassword");
		//从session中获取userID
		PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
		//非空判断
		if(username != null && password != null && user != null && newPassword != null && !password.equals(newPassword)) {
			try {
				//判断账号是否一致
				LocalAuth localAuth = localAuthService.getLocalByUserId(user.getUserId());
				if(localAuth == null || !localAuth.getUsername().equals(username)) {
					modelMap.put("success", false);
					modelMap.put("errMsg", "输入的账号非本次登陆的账号");
					return modelMap;
				}
				//修改密码
				LocalAuthExecution modifyLocalAuth = localAuthService.modifyLocalAuth(user.getUserId(), username, password, newPassword);
				if(modifyLocalAuth.getState() == LocalAuthStateEnum.SUCCESS.getState()) {
					modelMap.put("success", true);
				}else {
					modelMap.put("success", false);
					modelMap.put("errMsg", modifyLocalAuth.getStateInfo());
				}
			}catch (LocalAuthOperationException e) {
				modelMap.put("success", false);
				modelMap.put("errMsg", e.getMessage());
				return modelMap;
			}
		}else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "请输入密码");
		}
		return modelMap;
	}
	
	/***
	 * 登陆验证
	 * @param request
	 * @return
	 */
	@RequestMapping("logincheck")
	@ResponseBody
	private Map<String, Object> loginCheck(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		boolean needVerify = HttpServletRequestUtil.getBoolean(request, "needVerify");
		if (needVerify && !CodeUtil.checkVerifyCode(request)) {
			modelMap.put("success", false);
			modelMap.put("errMsg", "输入验证码错误");
			return modelMap;
		}
		String username = HttpServletRequestUtil.getString(request, "username");
		String password = HttpServletRequestUtil.getString(request, "password");
		if (username != null && password != null) {
			LocalAuth localAuth = localAuthService.getLocalByUserNameAndPwd(username, password);
			if (localAuth != null) {
					modelMap.put("success", true);
					request.getSession().setAttribute("user",
							localAuth.getPersonInfo());
			} else {
				modelMap.put("success", false);
				modelMap.put("errMsg", "用户名或密码错误");
			}
		} else {
			modelMap.put("success", false);
			modelMap.put("errMsg", "用户名和密码均不能为空");
		}
		return modelMap;
	}
	
	/***
	 * 登出  注销session
	 * @param request
	 * @return
	 */
	@RequestMapping("logout")
	@ResponseBody
	private Map<String, Object> logout(HttpServletRequest request) {
		Map<String, Object> modelMap = new HashMap<String, Object>();
		//将session置空
		request.getSession().setAttribute("user",null);
		modelMap.put("success", true);
		return modelMap;
	}
}
