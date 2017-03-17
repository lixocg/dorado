package com.experian.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.experian.dto.pojo.litigation.ResultPojo;

@Controller
@RequestMapping("/login")
public class LoginController {

	@RequestMapping("/page")
	public String page() {
		return "login/page";
	}

	@RequestMapping("/doLogin")
	@ResponseBody
	public ResultPojo doLogin(String user, String pwd,HttpServletRequest request) {
		ResultPojo pojo = null;
		if (StringUtils.isEmpty(user) || StringUtils.isEmpty(pwd)) {
			pojo = new ResultPojo("11", "用户名或密码不能为空！");
			return pojo;
		}
		if ("admin".equals(user) && "admin".equals(pwd)) {
			HttpSession session = request.getSession();
			session.setAttribute("user", user);
			pojo = new ResultPojo("01", "认证成功！");
		} else {
			pojo = new ResultPojo("21", "用户名或密码错误！");
		}
		return pojo;
	}
}
