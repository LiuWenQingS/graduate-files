package com.hiekn.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * StringUtils
 * 
 * 
 * @author pzn
 * @version 1.0
 * @since 1.7
 */
public class StringUtils {

	/**
	 * 
	 * 过滤字符串两端空格
	 * 
	 * @param str
	 * @return
	 */
	public static final String trim(String str) {
		str = str.trim();
		if (str.startsWith("\u3000")) str = str.substring(1, str.length());
		if (str.endsWith("\u3000")) str = str.substring(0, str.length() - 1);
		return str.trim();
	}
	
	/**
	 * 
	 * 判断字符串是否为null或者""
	 * 
	 * @param str
	 * @return
	 */
	public static final boolean isNullOrEmpty(String str) {
		return null == str || str.length() == 0;
	}
	
	/**
     * 
     * 根据过滤正则，对源码进行<br>
     * 
     * 需要传入保留的group，这个group会被保留，其他字符会被过滤掉。
     * 
     * <li>0:表示filterRegex 匹配的所有内容
     * <li>1：表示filterRegex 中第一个捕获组匹配的内容
     * <li>2:...
     * <li>3:...
     * @param src
     * @param filterRegex
     * @param group
     * @return
     */
    public static final String filterByRegex(String src, String filterRegex, int group) {
    	Matcher matcher = Pattern.compile(filterRegex).matcher(src);
    	if (matcher.find()) return matcher.group(group);
    	else return "";
    }
    
    /**
	 * 通过截取表达式对输入字符串进行截取
	 * 
	 * @param val
	 *            待截取的字符串
	 * @param substringExp
	 *            三种截取方式： 
	 *            <li>1.传入一个数字：表示从这个位置开始截取到字符串末尾。 
	 *            <li>2.传入两个正数，用逗号(,)隔开，比如：
	 *            '0,4','2,5'：表示从第一个数字的位置开始（包括）截取到第二个数字（不包括）
	 *            <li>3. 传入两个整数，第一个为正数表示起始位置，第二个为负数，表示末尾减去的字符数，比如： '0,-1' -- 从第一个字符截取到字符串长度-1
	 *            <br><b>位置的从0开始</b>
	 * @return 截取后的子字符串
	 */
    public static final String substringByExp(String val, String substringExp) {
    	// 不用截取
    	if (isNullOrEmpty(substringExp)) return val;
    	// 没有字符串截取
    	if (isNullOrEmpty(val)) return "";
    	//
    	int beginIndex = 0;
		int endIndex = val.length();
		String[] fes = substringExp.split(",");
		if (fes.length == 1) {
			beginIndex = Integer.parseInt(fes[0]);
		} else if (fes.length == 2) {
			beginIndex = Integer.parseInt(fes[0]);
			endIndex = Integer.parseInt(fes[1]);
		}
		
		// 如果为负数 表示字符串长度减去这个数字
		if (endIndex < 0) endIndex = val.length() + endIndex;
		
		//
		if (beginIndex > endIndex) return "";
		//
		if (beginIndex >= val.length() || endIndex > val.length()) return "";
		
		return val.substring(beginIndex, endIndex);
    }
}
