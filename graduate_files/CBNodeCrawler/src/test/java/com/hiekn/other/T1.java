package com.hiekn.other;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.digest.DigestUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;
import com.hiekn.util.HttpReader;
import com.hiekn.util.StaticHttpReader;
import com.hiekn.util.StringTimeUtils;
import com.tarsadata.memberanalyzer.util.AddressProcessUtil;

public class T1 {
	
	public Map<String,Long> getProvinceMap() {
		Map<String,Long> provinceMap = new HashMap<String,Long>();
		BufferedReader br = null;
		String input = "";
		try {
			br = BufferedReaderUtil.getBuffer("data/stock/pro_map.txt");
			while ((input = br.readLine())!=null) {
				String[] split = input.split("\t");
				provinceMap.put(split[2], Long.valueOf(split[0]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return provinceMap;
	} 
	
	@Test
	public void t10() {
		System.out.println(DigestUtils.md5Hex("http://www.leiphone.com/news/201702/iQ5Y2aB0ROg3ezyW.html"));
		String s = "郝仁剑	北京理工大学		复杂系统智能控制与决策国家重点实验室";
		String[] sp = s.split("\t");
		for (String string : sp) {
			System.out.println(string);
		}
		System.out.println(sp[3]);
	}
	
	@Test 
	public void t9() {
		try {
			FileWriter fw = new FileWriter("data/news/36kr.txt");
			HttpReader reader = new StaticHttpReader();
			String source = reader.readSource("http://36kr.com/p/5063572.html");
			fw.write(source);
			fw.flush();
			Elements scriptEle = Jsoup.parse(source).getElementsByTag("script");
			for (Element element : scriptEle) {
				String script = element.html();
				if (script.contains("var props")) {
					script = script.replace("var props=", "");
					script = script.substring(0,script.indexOf(",locationnal"));
					System.out.println(script);
					JSONObject obj = JSONObject.parseObject(script);
					System.out.println(obj.getJSONObject("detailArticle|post").getString("content"));
				}
			}
			reader.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void t8() {
		try {
			BufferedReader br = BufferedReaderUtil.getBuffer("data/stock/com_info_concept.txt");
			String input = "";
			while ((input = br.readLine())!=null) {
				JSONObject obj = JSONObject.parseObject(input);
				String concept = obj.getString("concept");
				System.out.println("sfsf");
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void t7() {
		Map<String,Long> provinceMap = getProvinceMap();
		try {
			AddressProcessUtil apu = new AddressProcessUtil();
			FileWriter fw = new FileWriter("data/stock/com_info_concept.txt");
			BufferedReader br = BufferedReaderUtil.getBuffer("data/stock/com_info.txt");
			String input = "";
			int count = 0;
			while ((input = br.readLine())!=null) {
				JSONObject obj = JSONObject.parseObject(input);
				String address = obj.getString("69");
				if (address.contains("自由贸易试验区")) {
//					System.out.println(count++ + "\t" + "上海");
					System.out.println(address + "\t" + count++ + "\t" + provinceMap.get("上海"));
					obj.put("concept", provinceMap.get("上海"));
				} else {
					JSONObject jsonObj = apu.normalizeAddress(address);
//					System.out.println(count++ + "\t" + jsonObj.getString("province").replace("省", "").replace("市", "").replace("自治区", ""));
					System.out.println(count ++ + "\t" + provinceMap.get(jsonObj.getString("province").replace("省", "").replace("市", "").replace("自治区", "").replace("维吾尔", "").replace("壮族", "")) + "\t" + address);
					obj.put("concept", provinceMap.get(jsonObj.getString("province").replace("省", "").replace("市", "").replace("自治区", "").replace("维吾尔", "").replace("壮族", "")));
				}
				fw.write(obj.toJSONString() + "\r\n");
				fw.flush();
			}
			br.close();
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	@Test
	public void t6() {
		System.out.println(StringTimeUtils.formatStringTime("2013"));
	}
	
	private static String bracketStrProcess(String productOriginName) {
		String matchStr = "";
		String result = "";
		productOriginName = productOriginName.replace("（", "(").replace("）", ")");
		Pattern p = Pattern.compile("\\(\\S*\\)");
		Matcher matcher = p.matcher(productOriginName);
		while (matcher.find()) {
			System.out.println(matcher.group());
			matchStr = matcher.group();
		}
		int matchStrLength = matchStr.length();
		if (matchStrLength > 5 && (matchStr.contains("，")||matchStr.contains(","))) {
			result = productOriginName.replace(matchStr, "");
		} else {
			result = productOriginName;
		}
		return result;
	}
	
	@Test
	public void t5() {
		String input = "视频监控";
		boolean has =false;
		Pattern p = Pattern.compile("[a-zA-z]");
		Matcher matcher = p.matcher(input);
		if (matcher.find()) {
			has = true;
		} else {
			has = false;
		}
		System.out.println(has);
	}
	
	@Test
	public void t4() {
		String s = "一览生活●优惠便利，一触即发！";
		if (s.contains("，")) {
			System.out.println("contains");
		}
		String[] ar = s.split("—|－|·|：|:|•|–|-|_|●|，");
		for (String string : ar) {
			System.out.println(string);
		}
	}
	
	@Test
	public void t3() {
		System.out.println(3 % 1000);
	}
	
	@Test
	public void t2() {
		JSONArray arr = new JSONArray();
		for (Object object : arr) {
			System.out.println(object.toString());
		}
	}
	@Test
	public void t1() {
		String input = "";
		BufferedReader br = null;
		try {
			br = BufferedReaderUtil.getBuffer("data/dp/com_final.txt");
			while ((input = br.readLine())!=null) {
				JSONObject obj = JSONObject.parseObject(input);
				String str = obj.getString("origin");
				JSONObject o = JSONObject.parseObject(str);
				System.out.println(o.get("name"));
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
