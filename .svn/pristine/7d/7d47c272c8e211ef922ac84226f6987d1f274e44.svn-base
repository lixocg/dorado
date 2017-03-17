package com.experian.daas.litigation.utility;

import com.experian.comp.utility.RedisUtil;
import com.experian.core.constant.LitiBusiConstant;
import com.experian.core.utils.increment.IdEnums;

public class PKUtil {
	private PKUtil() {
	};

	public static String key(int flag) throws Exception {
		String key = null;
		switch (flag) {
		case LitiBusiConstant.DataFrom.OPTIX:
			key = LitiBusiConstant.Prefix.OPTIX + RedisUtil.incr(IdEnums.LITIGATION_DATA.getKey());
			break;
		case LitiBusiConstant.DataFrom.DB2_LITIGATION:
			key = LitiBusiConstant.Prefix.EXPERIAL + RedisUtil.incr(IdEnums.LITIGATION_DATA.getKey());
			break;
		case LitiBusiConstant.DataFrom.DB2_LITIGATION_HISTORY:
			key = LitiBusiConstant.Prefix.EXPERIAL + RedisUtil.incr(IdEnums.LITIGATION_DATA.getKey());
			break;
		case LitiBusiConstant.DataFrom.SQLSERVER_LITIGATION:
			key = LitiBusiConstant.Prefix.EXPERIAL + RedisUtil.incr(IdEnums.LITIGATION_DATA.getKey());
			break;
		case LitiBusiConstant.DataFrom.MANUAL:
			key = LitiBusiConstant.Prefix.EXPERIAL + RedisUtil.incr(IdEnums.LITIGATION_DATA.getKey());
			break;
		default:
			break;
		}
		return key;
	}

	public static String experianKey() throws Exception {
		return LitiBusiConstant.Prefix.EXPERIAL + RedisUtil.incr(IdEnums.LITIGATION_DATA.getKey());
	}

	public static String optixKey() throws Exception {
		return LitiBusiConstant.Prefix.OPTIX + RedisUtil.incr(IdEnums.LITIGATION_DATA.getKey()) + "";
	}
}
