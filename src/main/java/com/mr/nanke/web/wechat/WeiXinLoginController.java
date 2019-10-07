package com.mr.nanke.web.wechat;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.mr.nanke.dto.ShopExecution;
import com.mr.nanke.dto.WechatAuthExecution;
import com.mr.nanke.entity.PersonInfo;
import com.mr.nanke.entity.WechatAuth;
import com.mr.nanke.enums.WechatAuthStateEnum;
import com.mr.nanke.service.PersonInfoService;
import com.mr.nanke.service.ShopService;
import com.mr.nanke.service.WechatAuthService;
import com.mr.nanke.service.impl.WechatAuthServiceImpl;
import com.mr.nanke.utils.weixin.WeiXinUser;
import com.mr.nanke.utils.weixin.WeiXinUserUtil;
import com.mr.nanke.utils.weixin.message.pojo.UserAccessToken;

@Controller
@RequestMapping("wechatlogin")

/***
 * 从微信菜单点击后调用的接口，可以在url里增加参数（role_type）来表明是从商家还是从玩家按钮进来的，依次区分登陆后跳转不同的页面
 * 玩家会跳转到index.html页面 商家如果没有注册，会跳转到注册页面，否则跳转到任务管理页面 如果是商家的授权用户登陆，会跳到授权店铺的任务管理页面
 * 
 * @author 夏小颜
 *
 */
public class WeiXinLoginController {

	private static Logger log = LoggerFactory.getLogger(WeiXinLoginController.class);

	@Resource
	private PersonInfoService personInfoService;
	@Resource
	private WechatAuthService wechatAuthService;

	@Resource
	private ShopService shopService;

	// @Resource
	// private ShopAuthMapService shopAuthMapService;

	private static final String FRONTEND = "1";
	private static final String SHOPEND = "2";

	@RequestMapping(value = "/logincheck", method = { RequestMethod.GET })
	public String doGet(HttpServletRequest request, HttpServletResponse response) {
		log.debug("weixin login get...");
		// 获取微信公众号传过来的code，通过code可获得AccessToken，进而获取用户信息
		String code = request.getParameter("code");
		// 自定义的信息，方便调用，也可以不使用
		String roleType = request.getParameter("state");
		log.debug("weixin login code:" + code);
		WechatAuth auth = null;
		WeiXinUser user = null;
		String openId = null;
		if (null != code) {
			UserAccessToken token;
			try {
				// 通过code获取token
				token = WeiXinUserUtil.getUserAccessToken(code);
				log.debug("weixin login token:" + token.toString());
				//通过token获取access_Token
				String accessToken = token.getAccessToken();
				// 通过token获取openId
				openId = token.getOpenId();
				// 通过access_token和openId获取用户昵称等信息
				user = WeiXinUserUtil.getUserInfo(accessToken, openId);
				log.debug("weixin login user:" + user.toString());
				request.getSession().setAttribute("openId", openId);
				// 根据openID进行数据库查询，看是否有这个openID
				auth = wechatAuthService.queryWechatInfoByOpenId(openId);
			} catch (IOException e) {
				log.error("error in getUserAccessToken or getUserInfo or findByOpenId: " + e.toString());
				e.printStackTrace();
			}
		}
		// 使用获取的到的openID去数据库判断该微信账号是否存在，没有的话自动创建，直接实现微信与平台的无缝对接
		log.debug("weixin login success.");
		log.debug("login role_type:" + roleType);
		// 若微信账号为空则注册微信账号，同时注册微信用户信息
		if (auth == null) {
			// 获取用户信息
			PersonInfo personInfo = WeiXinUserUtil.getPersonInfoFromRequest(user);
			auth = new WechatAuth();
			auth.setOpenId(openId);
			if (FRONTEND.equals(roleType)) {
				personInfo.setCustomerFlag(1);
			}else {
				personInfo.setCustomerFlag(2);
			}
			auth.setPersonInfo(personInfo);
			WechatAuthExecution we = wechatAuthService.register(auth);
			if (we.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
				// 失败则返回null
				return null;
			}else {
				personInfo = personInfoService.getPersonInfoById(auth.getUserId());
				request.getSession().setAttribute("user", personInfo);
				
			}
		}
		if (FRONTEND.equals(roleType)) {
			return "frontend/index";  //用户则去前端展示页
		}else {
			return "shopadmin/shoplist"; //店家则进入到管理系统
		}
		
//		if (SHOPEND.equals(roleType)) {
//			PersonInfo personInfo = null;
//			WechatAuthExecution we = null;
//			if (auth == null) {
//				auth = new WechatAuth();
//				auth.setOpenId(openId);
//				personInfo = WeiXinUserUtil.getPersonInfoFromRequest(user);
//				personInfo.setShopOwnerFlag(1);
//				auth.setPersonInfo(personInfo);
//				we = WechatAuthService.register(auth);
//				if (we.getState() != WechatAuthStateEnum.SUCCESS.getState()) {
//					return null;
//				}
//			}
//			personInfo = personInfoService.getPersonInfoById(auth.getUserId());
//			request.getSession().setAttribute("user", personInfo);
//			ShopExecution se = shopService.getByEmployeeId(personInfo.getUserId());
//			request.getSession().setAttribute("user", personInfo);
//			if (se.getShopList() == null || se.getShopList().size() <= 0) {
//				return "shop/registershop";
//			} else {
//				request.getSession().setAttribute("shopList", se.getShopList());
//				return "shop/shoplist";
//			}
//		}
	}
}
