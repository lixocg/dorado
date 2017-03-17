package com.experian.daas.test.litigation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DataTest {
	public static void main(String[] args) throws Throwable {
		String file = "E:\\workspace\\dorado\\dorado-daas\\dorado-daas-litigation\\src\\test\\java\\com\\experian\\daas\\test\\litigation\\optix.json";
		BufferedReader reader = new BufferedReader(new FileReader(file));
		StringBuilder builder = new StringBuilder();

		String line = null;
		while ((line = reader.readLine()) != null) {
			builder.append(line);
		}
		System.out.println(builder.toString());
		String rs = sendPost(UrlEnum.LitigationUrl.getUrl(), builder.toString());
		System.out.println(rs);
		reader.close();
	}

	/**
	 * 发送 POST 工具
	 * 
	 * @param url
	 * @param param
	 * @return
	 */
	public static String sendPost(String url, String param) {
		OutputStream out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			URLConnection conn = realUrl.openConnection();
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("content-type", "application/json;charset=UTF-8");
			conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setDoOutput(true);
			conn.setDoInput(true);
			out = conn.getOutputStream();
			out.write(param.getBytes("utf-8"));
			out.flush();
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * API枚举常量
	 * 
	 * @author e00898a
	 *
	 */
	public enum UrlEnum {
		//
		LitigationUrl("http://localhost:8080/api-liti/litigation/channel/newData",
				"诉讼数据API"),
		//
		CaseUrl("http://127.0.0.1:8080/dorado-daas-web/daas/intergration/channel/litigation/api/newCaseCategory",
				"新增案件类型API"),
		//
		CourtUrl("http://127.0.0.1:8080/dorado-daas-web/daas/intergration/channel/litigation/api/newCourt", "新增法院URL"),
		//
		SourceUrl("http://127.0.0.1:8080/dorado-daas-web/daas/intergration/channel/litigation/api/newSourceUrl",
				"新增诉讼来源URL"),
		//
		PartyUrl("http://127.0.0.1:8080/dorado-daas-web/daas/intergration/channel/litigation/api/newPartyCategory",
				"新增案件类型字典URL");

		UrlEnum(String url, String name) {
			this.url = url;
			this.name = name;
		}

		private String url;
		private String name;

		public String getUrl() {
			return url;
		}

		public String getName() {
			return name;
		}

		public static String nameof(String url) {
			for (UrlEnum u : values()) {
				if (u.getUrl().equals(url)) {
					return u.getName();
				}
			}
			return "NotFoundUrl";
		}
	}
}
