package com.hiekn.parser;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;

public class FpFormat {
	public static void main(String[] args) {
		try {
//			integrateAllData();
			productDedupProcess();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static Map<String,JSONArray> productDedupProcess() throws IOException {
		FileWriter fw = new FileWriter("data/dp/product_name.txt");
		FileWriter pw = new FileWriter("data/dp/product_name_map.txt");
//		Map<String,String> iosMap = getMap("data/dp/com_product_test.txt");
		Map<String,String> iosMap = getMap("data/dp/com_product_ios.txt");
//		Map<String,String> andMap = new HashMap<String,String>();
		Map<String,String> andMap = getMap("data/dp/com_product_android.txt");
		Set<String> keySet = getKeySet();
		Map<String,JSONArray> comProductMap = new HashMap<String,JSONArray>();
		for (String comKey : keySet) {
			Map<String,JSONObject> productMap = new HashMap<String,JSONObject>();
			if (iosMap.containsKey(comKey)) {
				String productArrStr = iosMap.get(comKey);
				JSONArray iosProductArr = JSONArray.parseArray(productArrStr);
				for (Object object : iosProductArr) {
					JSONObject productObj = (JSONObject) object;
					String productOriginName = productObj.getString("name");
					String orginName = productObj.getString("name");
					productOriginName = bracketStrProcess(productOriginName);
					int thresholdLength = 0;
					if (hasEnglishCharactor(productOriginName)) {
						thresholdLength = 10;
					} else {
						thresholdLength = 5;
					}
					if (productOriginName.length() > thresholdLength) {
						String[] productNameArr = productOriginName.split("-");
						if (productNameArr.length > 0) {
							String productName = productNameArr[0].split("—|－|·|：|:|•|–|-|_|●|，|_|,|\\|")[0].replace("（", "(").replace("）", ")").trim();
							productObj.put("name", productName);
							productMap.put(productName,productObj);
							pw.write(orginName + "\t" + productName + "\r\n");
							pw.flush();
						}
					} else {
						productMap.put(productOriginName, productObj);
						pw.write(orginName + "\t" + productOriginName + "\r\n");
						pw.flush();
					}
				}
			}
			
			if (andMap.containsKey(comKey)) {
				String productArrStr = andMap.get(comKey);
				JSONArray andProductArr = JSONArray.parseArray(productArrStr);
				for (Object object : andProductArr) {
					JSONObject productObj = (JSONObject) object;
					String productOriginName = productObj.getString("name");
					String orginName = productObj.getString("name");
					productOriginName = bracketStrProcess(productOriginName);
					int thresholdLength = 0;
					if (hasEnglishCharactor(productOriginName)) {
						thresholdLength = 10;
					} else {
						thresholdLength = 5;
					}
					if (productOriginName.length() > thresholdLength) {
						String[] productNameArr = productOriginName.split("-");
						if (productNameArr.length > 0) {
							String productName = productNameArr[0].split("—|－|·|：|:|•|–|-|_|●|，|_|,|\\|")[0].replace("（", "(").replace("）", ")").trim();
							productObj.put("name", productName);
							productMap.put(productName,productObj);
							pw.write(orginName + "\t" + productName + "\r\n");
							pw.flush();
						}
					} else {
						productMap.put(productOriginName, productObj);
						pw.write(orginName + "\t" + productOriginName + "\r\n");
						pw.flush();
					}
				}
			}
			
			JSONArray productArray = new JSONArray();
			for (Entry<String,JSONObject> entry : productMap.entrySet()) {
				fw.write(entry.getKey() + "\r\n" );
//				fw.write(entry.getKey() + "\t" + entry.getValue() + "\r\n" );
				fw.flush();
				productArray.add(entry.getValue());
			}
			
			if (productArray.size() > 0) {
				comProductMap.put(comKey, productArray);
			}
		}
		fw.close();
		pw.close();
		return comProductMap;
	}
	
	public static void integrateAllData() throws IOException {
		Set<String> keySet = getKeySet();
		Map<String,String> originMap = getOriginMap();
 		Map<String,String> gsMap = getMap("data/dp/com_gs.txt");
		Map<String,String> investMap = getMap("data/dp/com_invest.txt");
		Map<String,JSONArray> productMap = productDedupProcess();
//		Map<String,String> iosMap = getMap("data/dp/com_product_ios.txt");
//		Map<String,String> andMap = getMap("data/dp/com_product_android.txt");
		int count = 0;
		try {
			FileWriter fw = new FileWriter("data/dp/com_final.txt");
			for (String key : keySet) {
				JSONObject obj = new JSONObject();
				obj.put("key", key);
//				JSONArray proArr = new JSONArray();
				if (gsMap.containsKey(key)) {
					String gsStr = gsMap.get(key);
					try {
						JSONArray gsArr = JSONArray.parseArray(gsStr);
						if (gsArr.size() == 1) {
							obj.put("gs", gsArr.get(0));
						} else {
							continue;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (originMap.containsKey(key)) {
					String originStr = originMap.get(key);
					try {
						obj.put("origin", originStr);
					} catch (Exception e) {
						
					}
				}
				if (investMap.containsKey(key)) {
					String investStr = investMap.get(key);
					try {
						JSONArray investArr = JSONArray.parseArray(investStr);
						if (investArr.size() > 0) {
							obj.put("invest", investArr);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (productMap.containsKey(key)) {
					obj.put("product", productMap.get(key));
				}
//				Set<String> productSet = new HashSet<String>();
//				if (iosMap.containsKey(key)) {
//					JSONArray iosArr = JSONArray.parseArray(iosMap.get(key));
//					for (Object object : iosArr) {
//						String productName = ((JSONObject)object).getString("name");
//						productSet.add(productName);
//					}
//					proArr.addAll(iosArr);
//				}
//				if (andMap.containsKey(key)) {
//					JSONArray andArr = JSONArray.parseArray(andMap.get(key));
//					for (Object object : andArr) {
//						String productName = ((JSONObject)object).getString("name");
//						if (!productSet.contains(productName)) {
//							proArr.add(object);
//						} else {
//							count++;
//						}
//					}
//					
//				}
//				if (proArr.size() > 0) {
//					obj.put("product", proArr);
//				}
				fw.write(obj.toJSONString() + "\r\n");
				fw.flush();
			}
			fw.close();
			System.out.println(count);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 对于括号中的名称进行处理
	 * @param productOriginName
	 * @return
	 */
	private static String bracketStrProcess(String productOriginName) {
		String matchStr = "";
		String result = "";
		productOriginName = productOriginName.replace("（", "(").replace("）", ")");
		Pattern p = Pattern.compile("\\(\\S*\\)");
		Matcher matcher = p.matcher(productOriginName);
		while (matcher.find()) {
			matchStr = matcher.group();
		}
		int matchStrLength = matchStr.length();
		if (matchStrLength > 5 && (matchStr.contains("，")||matchStr.contains(",")||matchStr.contains("+")
				||matchStr.contains("·"))||matchStr.contains("：")||matchStr.contains("！")||matchStr.contains("、")) {
			result = productOriginName.replace(matchStr, "");
		} else {
			result = productOriginName;
		}
		return result;
	}
	
	
	private static boolean hasEnglishCharactor(String input) {
		boolean has = false;
		Pattern p = Pattern.compile("[a-zA-z]");
		Matcher matcher = p.matcher(input);
		if (matcher.find()) {
			has = true;
		} else {
			has = false;
		}
		return has;
	}
	
	public static Set<String> getKeySet() {
		Set<String> keySet = new HashSet<String>();
		BufferedReader br = null;
		String input = "";
		try {
			br = BufferedReaderUtil.getBuffer("data/dp/com_info.txt");
			while ((input = br.readLine())!=null) {
				JSONObject obj = JSONObject.parseObject(input);
				String key = obj.getString("id");
				keySet.add(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return keySet;
	}
	
	public static Map<String,String> getOriginMap() {
		Map<String,String> originMap = new HashMap<String,String>();
		BufferedReader br = null;
		String input = "";
		try {
			br = BufferedReaderUtil.getBuffer("data/dp/dedup.txt");
			while ((input = br.readLine())!=null) {
				JSONObject obj = JSONObject.parseObject(input);
				String key = obj.getString("id");
				originMap.put(key, obj.toJSONString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return originMap;
		
	}
	
	
	public static Map<String,String> getMap(String path) {
		Map<String,String> map = new HashMap<String,String>();
		BufferedReader br = null;
		String input = "";
		try {
			br = BufferedReaderUtil.getBuffer(path);
			while ((input = br.readLine())!=null) {
				try {
					String[] split = input.split("\t");
					if (split[1].equals("null")) continue;
					if (split.length == 2) {
						map.put(split[0], split[1]);
					} else {
						String result = "";
						for (int i = 1; i < split.length; i++) {
							result = result + split[i];
						}
						map.put(split[0], result);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
	
}
