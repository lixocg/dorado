package com.experian.api.filter;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

import com.experian.core.pojo.ApiResult;
import com.experian.core.pojo.BaseAPIPojo;
import com.experian.core.pojo.R;
import com.experian.core.pojo.ApiResult.Errors;
import com.google.gson.Gson;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;

public class AccessFilter extends ZuulFilter {
	private static Logger log = Logger.getLogger(AccessFilter.class);

	@Override
	public String filterType() {
		return "pre";
	}

	@Override
	public int filterOrder() {
		return 0;
	}

	@Override
	public boolean shouldFilter() {
		return true;
	}

	@Override
	public Object run() {
		try {
			Gson gson = new Gson();
			RequestContext ctx = RequestContext.getCurrentContext();
			HttpServletRequest request = ctx.getRequest();
			HttpServletResponse response = ctx.getResponse();
			log.info(String.format("%s request to %s", request.getMethod(), request.getRequestURL().toString()));
			InputStream inputStream = request.getInputStream();
			String param = IOUtils.toString(inputStream, "utf-8");
			BaseAPIPojo apiPojo = gson.fromJson(param, BaseAPIPojo.class);
			if (apiPojo == null) {
				ApiResult<Void> r = new ApiResult<Void>();
				r.setStatus(R.FAILED);
				List<Errors> errors = new ArrayList<Errors>();
				errors.add(new Errors(Errors.Type.TOKEN_UNPASSED, "", "token不能为空!"));
				r.setError(errors);
				String err = gson.toJson(r);
				log.warn(err);
				ctx.setResponseBody(err);
				//ctx.setSendZuulResponse(false);
				//ctx.setResponseStatusCode(401);
				return err;
			}

			if (!apiPojo.checked()) {
				ApiResult<Void> r = new ApiResult<Void>();
				r.setStatus(R.FAILED);
				List<Errors> errors = new ArrayList<Errors>();
				errors.add(new Errors(Errors.Type.TOKEN_UNPASSED, "", "token<" + apiPojo.getToken() + ">校验不通过!"));
				r.setError(errors);
				String err = gson.toJson(r);
				log.warn(err); 
				ctx.setResponseBody(err);
				ctx.setSendZuulResponse(false);
				ctx.setResponseStatusCode(401);
				response.getWriter().write(err);
				return null;
			}
			log.info("access token ok");
		} catch (Exception e) {
			log.error(e);
		}
		return null;
	}
}
