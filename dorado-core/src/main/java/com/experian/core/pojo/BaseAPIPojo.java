package com.experian.core.pojo;

import com.experian.core.enums.ChannelTokenEnum;

/**
 * 基础API通讯POJO
 * @author e00898a
 *
 */
public class BaseAPIPojo {

	public String token;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	/**
	 * 校验token是否合法
	 * @return
	 */
	public boolean checked(){
		return ChannelTokenEnum.checkToken(token);
	}
}
