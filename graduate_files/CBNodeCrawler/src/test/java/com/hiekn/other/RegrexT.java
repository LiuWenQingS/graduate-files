package com.hiekn.other;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegrexT {
	public static void main(String[] args) {
		String text = "RAR 5.31 版权所有 (C) 1993-2016 Alexander Roshal 4 二月 2016 试用版本 输入 RAR -? 以获得帮助 压缩文件: d:\1.rar 详细资料: RAR 4, 已加密的头 属性 大小 日期 时间 名称 ----------- --------- ---------- ----- ---- * ..A.... 1177811944 2016-03-12 13:45 android-studio-bundle-141.2288178-windows.exe ----------- --------- ---------- ----- ---- 1177811944 1";
		String pattern = "(?<=\\d{2}:\\d{2}).*(?=)";
//		String pattern = "\\d{1,2}:\\d{1,2}";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(text);
		while (m.find()) {
			System.out.println(m.group());
		}
			
	}
}
