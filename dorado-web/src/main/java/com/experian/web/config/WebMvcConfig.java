package com.experian.web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.experian.web.interceptor.CtxInterceptor;
import com.experian.web.interceptor.LoginInterceptor;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	 @Override
	    public void addInterceptors(InterceptorRegistry registry) {
	        // 多个拦截器组成一个拦截器链
	        // addPathPatterns 用于添加拦截规则
	        // excludePathPatterns 用户排除拦截
	        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/**").excludePathPatterns("/").excludePathPatterns("/login/*");
	        registry.addInterceptor(new CtxInterceptor()).addPathPatterns("/**");
	        super.addInterceptors(registry);
	    }
}
