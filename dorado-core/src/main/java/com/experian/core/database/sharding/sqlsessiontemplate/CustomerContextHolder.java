package com.experian.core.database.sharding.sqlsessiontemplate;

/**
 * 
 * @author bin
 */
public abstract class CustomerContextHolder {
	public static final String DB2_CREDIT = "db2_credit";
	public static final String SQLSERVER = "sqlserver";
	public static final String MYSQL = "mysql";
	public static final String DB2_CENTER = "db2_center";

	private static final ThreadLocal<String> contextHolder = new ThreadLocal<String>();

	public static void setContextType(String contextType) {
		contextHolder.set(contextType);
	}

	public static String getContextType() {
		return contextHolder.get();
	}

	public static void clearContextType() {
		contextHolder.remove();
	}
}