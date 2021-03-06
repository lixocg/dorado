package com.experian.core.enums;

import org.springframework.util.DigestUtils;

/**
 * 供应商
 * @author e00898a
 *
 */
public enum ChannelTokenEnum {
	OPTIX_TOKEN("optix","Optix 诉讼Token");
	ChannelTokenEnum(String token,String name){
		this.token=token;
		this.name=name;
	}
	private String token;
	private String name;
	
	public String getToken() {
		return token;
	}

	public String getName() {
		return name;
	}
	public static boolean checkToken(String tokenStr){
		for(ChannelTokenEnum c:values()){
			if(getMd5(c.getToken()).equals(tokenStr)){
				return true;
			}
		}
		return false;
	}
	public static String tokenOf(String tokenStr){
		for(ChannelTokenEnum c:values()){
			if(getMd5(c.getToken()).equals(tokenStr)){
				return c.getName();
			}
		}
		return "";
	}
	/**
	 * 诉讼盐
	 */
	private static final String litigationSalt="experian_litigation";
	private static String getMd5(String token){
		return DigestUtils.md5DigestAsHex((token + "/" + litigationSalt).getBytes());
	}
}
