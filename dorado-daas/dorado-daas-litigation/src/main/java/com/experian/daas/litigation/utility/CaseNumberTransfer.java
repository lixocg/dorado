package com.experian.daas.litigation.utility;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.assertj.core.util.Lists;

/**
 * 案号、上期案号转简单案号、简单上期案号
 * 
 * @author e00769a
 *
 */
public class CaseNumberTransfer {
	public static final String SPECIAL_CHARS_REGEX = "[()（）字第号\\[\\]【】、,<>《》.{}\\?？]";

	public static String filter(String origin) {
		Pattern p = Pattern.compile(SPECIAL_CHARS_REGEX);
		Matcher m = p.matcher(origin);
		return m.replaceAll("").trim();
	}

	public static String toSimple(String origin) {
		if (StringUtils.isBlank(origin)) {
			return null;
		}
		Pattern p = Pattern.compile("\\d+");
		String filter = filter(origin);
		Matcher m = p.matcher(filter);
		List<String> list = Lists.newArrayList();
		while (m.find()) {
			list.add(m.group());
		}
		if (list.size() == 0) {
			throw new RuntimeException("转简单案号或简单上期案号异常，请检查：" + origin);
		}
		String last = list.get(list.size() - 1);
		return filter.substring(0, filter.indexOf(last)).concat(last.replace("0", ""));
	}

}
