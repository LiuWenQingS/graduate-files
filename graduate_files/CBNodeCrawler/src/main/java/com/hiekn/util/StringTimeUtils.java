package com.hiekn.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StringTimeUtils {
	
	//
	private static final DateFormat HIKNE_SIMPLE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	private static final DateFormat HIKNE_SIMPLE_DAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
	//
	private static final String EXCLUDE_NUMBER_REGEX = "[^0-9]";
	//
	private static final String EXCLUDE_TIME_REGEX = "[^\\d:\\s-]";
	/**
	 * 
	 * 输入一个日期字符串
	 * 
	 * 格式化成:yyyy-MM-dd HH:mm:ss
	 * 
	 * expample:
	 * 输入：2015/12/12 12:10
	 * 输出：2015-12-12 12:10:00
	 * 
	 * @param time
	 * @return
	 */
	public static final String formatStringTime(String time) {
		
		if (null == time || time.length() == 0) return HIKNE_SIMPLE_TIME_FORMAT.format(System.currentTimeMillis());
		
		// 过滤两端空白字符
		time = StringUtils.trim(time);
		
		String resultTime = "";
		
		// 语义上的时间
		if (time.indexOf("秒前") > -1) {
			String second = time.replaceAll(EXCLUDE_NUMBER_REGEX, "");
			resultTime = HIKNE_SIMPLE_TIME_FORMAT.format(System.currentTimeMillis() - Integer.parseInt(second) * 1000);
			return resultTime;
		} else if (time.indexOf("分钟前") > -1) {
			String second = time.replaceAll(EXCLUDE_NUMBER_REGEX, "");
			resultTime = HIKNE_SIMPLE_TIME_FORMAT.format(System.currentTimeMillis() - Integer.parseInt(second) * 1000 * 60);
			return resultTime;
		} else if (time.indexOf("小时前") > -1) {
			String second = time.replaceAll(EXCLUDE_NUMBER_REGEX, "");
			resultTime = HIKNE_SIMPLE_TIME_FORMAT.format(System.currentTimeMillis() - Integer.parseInt(second) * 1000 * 60 * 60);
			return resultTime;
		} else if (time.indexOf("天前") > -1) {
			String second = time.replaceAll(EXCLUDE_NUMBER_REGEX, "");
			resultTime = HIKNE_SIMPLE_TIME_FORMAT.format(System.currentTimeMillis() - Integer.parseInt(second) * 1000 * 60 * 60 * 24);
			return resultTime;
		} else if (time.indexOf("昨天") > -1) {// youdao
			resultTime = HIKNE_SIMPLE_TIME_FORMAT.format(System.currentTimeMillis() - 1000 * 60 * 60 * 24 * 1);
			return resultTime;
		} else if (time.indexOf("今天") > -1) {// 新浪财经
			if (time.length() == 2) {
				resultTime = HIKNE_SIMPLE_TIME_FORMAT.format(System.currentTimeMillis());;
				return resultTime;
			}
			// 带有时间 时分秒等
			time = HIKNE_SIMPLE_DAY_FORMAT.format(System.currentTimeMillis()) + " " + time.substring(2, time.length());
		}
		
		// 中文时间
		if (time.indexOf("年") > -1 || time.indexOf("月") > -1 || time.indexOf("日") > -1) 
			time = time.replaceAll("[年月]", "-").replaceAll("日", "");
		if (time.indexOf("时") > -1 || time.indexOf("分") > -1 || time.indexOf("秒") > -1) 
			time = time.replaceAll("[时分]", ":").replaceAll("秒", "");
		// msn 2016/03/03 10:41
		if (time.indexOf("/") > -1) 
			time = time.replaceAll("/", "-");
		// juzi 2015.11
		if (time.indexOf(".") > -1) 
			time = time.replaceAll("\\.", "-");
		
		// 格式化  替换非时间表示字符和两端空白字符
		time = time.replaceAll(EXCLUDE_TIME_REGEX, "").trim();
		
		// 月-日  3-15
		if (time.matches("\\d{1,2}-\\d{1,2}")) 
			resultTime = Calendar.getInstance().get(Calendar.YEAR) + "-" + time + " 00:00:00";
		// 月-日 时 3-15 15
		else if (time.matches("\\d{1,2}-\\d{1,2}\\s+\\d{1,2}")) 
			resultTime = Calendar.getInstance().get(Calendar.YEAR) + "-" + time + ":00:00";
		// 月-日 时:分 3-15 15:30
		else if (time.matches("\\d{1,2}-\\d{1,2}\\s+\\d{1,2}:\\d{1,2}")) 
			resultTime = Calendar.getInstance().get(Calendar.YEAR) + "-" + time + ":00";
		// 月-日 时:分:秒 3-15 15:30:15
		else if (time.matches("\\d{1,2}-\\d{1,2}\\s+\\d{1,2}:\\d{1,2}:\\d{1,2}")) 
			resultTime = Calendar.getInstance().get(Calendar.YEAR) + "-" + time;
		// 年-月 2015-11
		else if (time.matches("\\d{2,4}-\\d{1,2}"))
			resultTime = time + "-01 00:00:00";
		// 年-月-日 2016-3-15
		else if (time.matches("\\d{2,4}-\\d{1,2}-\\d{1,2}")) 
			resultTime = time + " 00:00:00";
		// 年-月-日 时 2016-3-15 15
		else if (time.matches("\\d{2,4}-\\d{1,2}-\\d{1,2}\\s+\\d{2}")) 
			resultTime = time + ":00:00";
		// 年-月-日 时:分 2016-3-15 15:30
		else if (time.matches("\\d{2,4}-\\d{1,2}-\\d{1,2}\\s+\\d{2}:\\d{2}")) 
			resultTime = time + ":00";
		// 正常时间
		else resultTime = time;
		
		// 补零
		if (resultTime.matches("\\d{2,4}-\\d{1}-\\d{1}\\s+\\d{2}:\\d{2}:\\d{2}")) {
			resultTime = resultTime.replaceAll("-", "-0");
		} else if (resultTime.matches("\\d{2,4}-\\d{1}-\\d{2}\\s+\\d{2}:\\d{2}:\\d{2}")) {
			String prefix = resultTime.substring(0, resultTime.indexOf("-") + 1);
			String suffix = resultTime.substring(resultTime.indexOf("-") + 1, resultTime.length());
			resultTime = prefix + "0" + suffix;
		} else if (resultTime.matches("\\d{2,4}-\\d{2}-\\d{1}\\s+\\d{2}:\\d{2}:\\d{2}")) {
			String prefix = resultTime.substring(0, resultTime.lastIndexOf("-") + 1);
			String suffix = resultTime.substring(resultTime.lastIndexOf("-") + 1, resultTime.length());
			resultTime = prefix + "0" + suffix;
		}
		
		// 年份补全  16-03-15 15:40:20
		if (resultTime.matches("\\d{2}-\\d{1,2}-\\d{1,2}\\s+\\d{2}:\\d{2}:\\d{2}"))
			resultTime = String.valueOf(Calendar.getInstance().get(Calendar.YEAR)).substring(0, 2) + resultTime;
		
		return resultTime;
	}
	
	public static final String formatLongTime(String longTime) {
		long timeMillis = Long.parseLong(longTime, 10);
		return HIKNE_SIMPLE_TIME_FORMAT.format(timeMillis);
	}
	
	public static void main(String[] args) {
		System.out.println(StringTimeUtils.formatStringTime("2015.11"));
		//System.out.println(StringTimeUtils.formatLongTime("1458552756000"));
	}
}
