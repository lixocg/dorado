package com.eaxperian.test;

public class App {
	public static void main(String[] args) {
		System.out.println(getTableSuffix(8888));
		System.out.println(getTableSuffix(10000));
		System.out.println(getTableSuffix(19999));
		System.out.println(getTableSuffix(100000));
		System.out.println(getTableSuffix(13456432));
	}
	
	/**
	 * [1~9999]=>0000,[1,0000~1,9999]=>0001....[10,0000~10,9999]=>10...
	 * [100,0000~100,9999]=>100....[1000,0000~1000,9999]=>1000
	 * @param code
	 * @return
	 */
	public static String getTableSuffix(int code){
			return leftPadZore(code / 10000);
	}
	
	public static String leftPadZore(int number){
		return String.format("_%04d", number);  
	}
	
	
}
