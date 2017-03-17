package com.experian.daas.baseinfo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.experian.daas.baseinfo.service.BaseInfoService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/baseinfo")
@Api(value = "dorado基本信息API")
public class BaseInfoRest {
	@Autowired
	private BaseInfoService baseInfoService;

	@ApiOperation(value = "匹配SBDNUM", notes = "企业名称匹配SBDNUM",response=String.class)
	@ApiImplicitParam(name = "corpName", value = "企业名称", required = true, dataType = "string", paramType = "path", defaultValue = "")
	@RequestMapping(value = "/match/{corpName}", method = RequestMethod.GET)
	public String match(@PathVariable("corpName") String corpName) throws Exception {
		return baseInfoService.match(corpName);
	}
}
