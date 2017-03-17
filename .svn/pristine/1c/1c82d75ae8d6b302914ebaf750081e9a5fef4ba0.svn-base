package com.experian.daas.litigation.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.IOUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.experian.core.pojo.ApiResult;
import com.experian.core.pojo.ApiResult.Errors;
import com.experian.core.pojo.R;
import com.experian.daas.litigation.service.LitigationChannelService;
import com.experian.daas.litigation.utility.DateTypeAdapter;
import com.experian.dto.pojo.litigation.CaseCategoryAPIPojo;
import com.experian.dto.pojo.litigation.CourtAPIPojo;
import com.experian.dto.pojo.litigation.LitigationAPIPojo;
import com.experian.dto.pojo.litigation.LitigationAPIPojos;
import com.experian.dto.pojo.litigation.PartyCategoryAPIPojo;
import com.experian.dto.pojo.litigation.SourceUrlAPIPojo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/litigation/channel")
@Api(value = "诉讼供应商推送诉讼数据API")
public class LitigationChannelRest {
	@Resource(name = "litigationChannelService")
	private LitigationChannelService litigationChannelService;

	@RequestMapping(value = "/v", method = RequestMethod.GET)
	public String v() {
		return "ddddd";
	}

	@ApiOperation(value = "诉讼主体数据推送")
	@RequestMapping(value = "/newData", method = RequestMethod.POST)
	public ApiResult<Void> newData(HttpServletRequest request) {
		InputStream inputStream = null;
		try {
			inputStream = request.getInputStream();
			String param = IOUtils.toString(inputStream, "utf-8");
			Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, new DateTypeAdapter()).create();
			LitigationAPIPojos litigationAPIPojos = gson.fromJson(param, LitigationAPIPojos.class);
			// 校验token
			if (!litigationAPIPojos.checked()) {
				ApiResult<Void> r = new ApiResult<Void>();
				r.setStatus(R.FAILED);
				List<Errors> errors = new ArrayList<Errors>();
				errors.add(new Errors(Errors.Type.TOKEN_UNPASSED, "",
						"token<" + litigationAPIPojos.getToken() + ">校验不通过!"));
				r.setError(errors);
				return r;
			}
			List<LitigationAPIPojo> litigations = litigationAPIPojos.getData();
			return litigationChannelService.litigationChannel(litigations);
		} catch (IOException e) {
			ApiResult<Void> r = new ApiResult<Void>();
			r.setStatus(R.FAILED);
			List<Errors> errors = new ArrayList<Errors>();
			errors.add(new Errors(Errors.Type.OTHER, "", "token<" + e.getMessage() + ">校验不通过!"));
			r.setError(errors);
			return r;
		} finally {
			IOUtils.closeQuietly(inputStream);
		}

	}

	@ApiOperation(value = "在线推送新增案件类型数据")
	@RequestMapping(value = "/newCaseCategory", method = RequestMethod.POST)
	public ApiResult<Void> newCaseCategory(@RequestBody CaseCategoryAPIPojo pojo) {
		// 校验token
		if (!pojo.checked()) {
			ApiResult<Void> r = new ApiResult<Void>();
			r.setStatus(R.FAILED);
			List<Errors> errors = new ArrayList<Errors>();
			errors.add(new Errors(Errors.Type.TOKEN_UNPASSED, "", "token<" + pojo.getToken() + ">校验不通过!"));
			r.setError(errors);
			return r;
		}
		return litigationChannelService.litigationNewCaseCategory(pojo.getData());
	}

	@ApiOperation(value = "在线推送新增法院字典数据")
	@RequestMapping(value = "/newCourt", method = RequestMethod.POST)
	public ApiResult<Void> newCourt(@RequestBody CourtAPIPojo pojo) {
		if (!pojo.checked()) {
			ApiResult<Void> r = new ApiResult<Void>();
			r.setStatus(R.FAILED);
			List<Errors> errors = new ArrayList<Errors>();
			errors.add(new Errors(Errors.Type.TOKEN_UNPASSED, "", "token<" + pojo.getToken() + ">校验不通过!"));
			r.setError(errors);
			return r;
		}
		return litigationChannelService.litigationNewCourt(pojo.getData());

	}

	@ApiOperation(value = "在线推送新增诉讼数据来源")
	@RequestMapping(value = "/newSourceUrl", method = RequestMethod.POST)
	public ApiResult<Void> newSourceUrl(@RequestBody SourceUrlAPIPojo pojo) {
		if (!pojo.checked()) {
			ApiResult<Void> r = new ApiResult<Void>();
			r.setStatus(R.FAILED);
			List<Errors> errors = new ArrayList<Errors>();
			errors.add(new Errors(Errors.Type.OTHER, "", "token<" + pojo.getToken() + ">校验不通过!"));
			r.setError(errors);
			return r;
		}
		return litigationChannelService.litigationNewSourceUrl(pojo.getData());

	}

	@ApiOperation(value = "在线推送新增企业/自然人类型字典")
	@RequestMapping(value = "/newPartyCategory", method = RequestMethod.POST)
	public ApiResult<Void> newPartyCategory(@RequestBody PartyCategoryAPIPojo pojo) {
		if (!pojo.checked()) {
			ApiResult<Void> r = new ApiResult<Void>();
			r.setStatus(R.FAILED);
			List<Errors> errors = new ArrayList<Errors>();
			errors.add(new Errors(Errors.Type.TOKEN_UNPASSED, "", "token<" + pojo.getToken() + ">校验不通过!"));
			r.setError(errors);
			return r;
		}
		return litigationChannelService.litigationNewPartyCategory(pojo.getData());

	}
}
