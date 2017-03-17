package com.eaxperian.test;

import com.eaxperian.test.service.TableService;
import com.experian.core.database.sharding.builder.ShardObject;
import com.experian.core.database.sharding.shard.Strategy;
import com.experian.core.utils.ReflectionUtils;
import com.experian.core.utils.SpringContextUtil;

public class IdStrategy implements Strategy {
	@Override
	public String getTargetSql(String oldSql, Object parm, ShardObject object) {
		TableService tableService = (TableService) SpringContextUtil.getBean("tableService");
		String sql = null;
		int id = -1;
		if (parm.getClass().equals(Integer.class)) {
			id = Integer.parseInt(parm.toString());
		} else if (parm.getClass().equals(String.class)) {
			id = Integer.parseInt(parm.toString());
		} else {
			Object idObj = ReflectionUtils.getFieldValue(parm, "id");
			id = Integer.parseInt(idObj.toString());
		}

		if (oldSql.trim().toLowerCase().startsWith("insert")) {
			tableService.check(id(id));
		}

		String[] tables = object.getTables();
		for (String t : tables) {
			if (id % 2 == 0) {
				sql = oldSql.replace(t, t + "_3");
			} else {
				sql = oldSql.replace(t, t + "_4");
			}
		}
		System.out.println(sql);
		return sql;
	}

	public int id(int id) {
		if (id % 2 == 0) {
			return 3;
		} else {
			return 4;
		}
	}

}
