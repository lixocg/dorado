package com.experian.web.controller.baseinfo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.experian.core.enums.Srv;
import com.experian.core.pojo.R;
import com.experian.core.rest.RestClient;

@Controller
@RequestMapping("baseinfo")
public class BaseInfoController {
	@RequestMapping("page")
	public String page(){
		return "baseinfo/page";
	}
	
	@ResponseBody
	@RequestMapping("/matchsbdnum")
	public R<String> matchsbdnum(@RequestParam String name) {
		try {
			String sbdnum = RestClient.get(Srv.baseinfo,"/baseinfo/match/{name}", String.class,name);
			return new R<String>(sbdnum);
		} catch (Exception e) {
			return new R<String>(R.FAILED, e.getMessage());
		}

	}
}
