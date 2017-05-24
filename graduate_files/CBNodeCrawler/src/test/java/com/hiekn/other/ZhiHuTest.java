package com.hiekn.other;

import java.io.FileWriter;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.HttpReader;
import com.hiekn.util.StaticHttpReader;

public class ZhiHuTest {

	@Test
	public void t1() throws Exception {
		FileWriter fw = new FileWriter("data/other/zh.txt");
//		String keyword = "当当";
		String searchUrl = "https://www.zhihu.com/r/search?q=%E5%85%AC%E5%AD%90%E5%B0%8F%E7%99%BD&type=content&offset=10";
		HttpReader reader = new StaticHttpReader();
		String cookie = "d_c0=\"ACBAs-H4HQqPTsFlV5pL7dalirxdw6lQweE=|1466590091\";q_c1=7c6a91ad8d624095a0dbb2baf88215cb|1466590091000|1466590091000;_za=3d190104-3b9f-43f8-92ad-2400d59e8ffe;l_cap_id=\"NThmYTQ0NzAyNTgwNGMxNDg2MzlkMGI2MDNmMzhlYWM=|1466669212|d5873b17d3783dd308c27bc13fa63b78b1344145\";cap_id=\"YjJkODU4MTM3ZDg2NGNjZTgwYjk4NzI5NjVhYTc4MDk=|1466669212|b0588bcf42facf3da8076e7e9a12d5ccf59efb14\";l_n_c=1;login=\"NDQ1NTg1MWUwMjRhNDJmNDkwYzIzYWZmNTQ5YjA5ZGE=|1466669454|fef0b0edd71b8c9967795788e6c2ae2bbba6ffef\";z_c0=Mi4wQUFEQWdpZ2dBQUFBSUVDejRmZ2RDaGNBQUFCaEFsVk5qaWFUVndBcVVFMWV5ZHJBVXZoWEVBRjhubWtnUE9Ucnhn|1466669454|c9f325e1c5599f0eefebe930fe0822bf4cf15b93;_xsrf=eb354bc97a2e716c5accecbe232c44f3;_zap=21d4e3b0-0efc-4118-a1bd-7fe74702dd6f;s-q=%E5%85%AC%E5%AD%90%E5%B0%8F%E7%99%BD;s-i=2;sid=0tqgotj8;a_t=\"2.0AADAgiggAAAXAAAA0_e3VwAAwIIoIAAAACBAs-H4HQoXAAAAYQJVTY4mk1cAKlBNXsnawFL4VxABfJ5pIDzk68YePw65Iubw3pqcT0pThA3kMvgG2Q==\";__utma=51854390.1939403230.1469066313.1469074072.1469079552.6;__utmb=51854390.40.9.1469083647618;__utmc=51854390;__utmz=51854390.1469079552.6.5.utmcsr=baidu|utmccn=(organic)|utmcmd=organic;__utmv=51854390.100-1|2=registration_date=20131103=1^3=entry_date=20131103=1;";
		String source = reader.readSource(searchUrl, "UTF-8", cookie);
		JSONObject obj = JSONObject.parseObject(source);
		String s = obj.get("htmls").toString();
		System.out.println(s);
//		System.out.println(source);
		fw.write(s+"\n");
		fw.flush();
		reader.close();
		fw.close();
	}
	
	
	public static String unicode2String(String unicode) {
		
	    StringBuffer string = new StringBuffer();
	 
	    String[] hex = unicode.split("\\\\u");
	 
	    for (int i = 1; i < hex.length; i++) {
	 
	        // 转换出每一个代码点
	        int data = Integer.parseInt(hex[i], 16);
	 
	        // 追加成string
	        string.append((char) data);
	    }
	 
	    return string.toString();
	}
	
	
}
