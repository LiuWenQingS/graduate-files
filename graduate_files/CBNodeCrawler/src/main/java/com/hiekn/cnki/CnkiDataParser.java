package com.hiekn.cnki;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;
import com.hiekn.util.StringTimeUtils;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class CnkiDataParser {
	
	private static MongoClient client = new MongoClient("127.0.0.1",27017);
	private static MongoCollection<Document> collection = client.getDatabase("data").getCollection("cnki");
	private static MongoCollection<Document> pcollection = client.getDatabase("data").getCollection("cnki_parser");
	private static Map<String,Long> idMap = new HashMap<String,Long>();
	
	public static void main(String[] args) {
		init();
//		docParse();
//		paperParse();
//		patentParse();
//		standardParse();
	}
	
	public static void docParse() {
		int count = 0;
		Document searchDoc = new Document("type","doc");
		MongoCursor<Document> cursor = collection.find(searchDoc).iterator();
		Map<String,Set<Long>> map = new HashMap<String,Set<Long>>();
		Map<String,JSONObject> resultMap = new HashMap<String,JSONObject>();
		try {
			FileWriter fw = new FileWriter("data/cnki/doc.txt");
			while (cursor.hasNext()) {
//				System.out.println(count++);
				JSONObject obj = new JSONObject();
				Document doc = cursor.next();
				String key = doc.getString("publishTime") + doc.getString("title") + doc.getString("ins");
				if (map.containsKey(key)) {
					System.out.println(key + " 重复");
					Set<Long> tempList = map.get(key);
					tempList.add(idMap.get(doc.getString("keyword")));
					map.put(key, tempList);
				} else {
					Set<Long> list = new HashSet<Long>();
					list.add(idMap.get(doc.getString("keyword")));
					map.put(key, list);
				}
				String source = doc.getString("source");
				org.jsoup.nodes.Document jDoc = Jsoup.parse(source);
				obj.put("search_word", map.get(key));
				obj.put("year", doc.getString("publishTime").replace("年", ""));
				obj.put("source_url", doc.getString("url"));
				obj.put("institution", doc.getString("ins"));
				obj.put("title", doc.getString("title"));
				obj.put("type", doc.getString("from"));
				Elements aEle = jDoc.select("div.author > span > a");
				JSONArray aArr = new JSONArray();
				for (Element author : aEle) {
					aArr.add(author.text().trim());
				}
				obj.put("author", aArr);
				try {
					Elements pEle = jDoc.select("div.wxBaseinfo > p");
					for (Element element : pEle) {
						if (element.text().contains("分类号")) {
							String classNum = element.text().replace("分类号：", "").trim();
							obj.put("classification_num", classNum);
						} else if (element.text().contains("摘要")) {
							String abs = element.text().replace("摘要：", "").replace("更多还原", "").trim();
							obj.put("abs", abs);
						} else if (element.text().contains("关键词")) {
							String kt = element.text().replace("关键词：", "").trim();
							JSONArray kArr = new JSONArray();
							String[] split = kt.split(";");
							for (String kw : split) {
								kArr.add(kw.trim());
							}
							obj.put("keywords", kArr);
						} else if (element.text().contains("导师")) {
							String tt = element.text().replace("导师：", "").trim();
							JSONArray tArr = new JSONArray();
							String[] split = tt.split(";");
							for (String t : split) {
								tArr.add(t.trim());
							}
							obj.put("tutor", tArr);
						} 
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				resultMap.put(key, obj);
//				fw.write(obj.toJSONString()+ "\r\n");
//				fw.flush();
			}
			for (String key : resultMap.keySet()) {
				fw.write(resultMap.get(key) + "\r\n");
				fw.flush();
			}
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
	}
	
	public static void paperParse() {
		int count = 0;
		Document searchDoc = new Document("type","paper");
		MongoCursor<Document> cursor = collection.find(searchDoc).iterator();
		Map<String,Set<Long>> map = new HashMap<String,Set<Long>>();
		Map<String,JSONObject> resultMap = new HashMap<String,JSONObject>();
		try {
			FileWriter fw = new FileWriter("data/cnki/paper.txt");
			while (cursor.hasNext()) {
//				System.out.println(count++);
				JSONObject obj = new JSONObject();
				Document doc = cursor.next();
				String key = doc.getString("publishTime") + doc.getString("title") + doc.getString("ins");
				if (map.containsKey(key)) {
					System.out.println(key + " 重复");
					Set<Long> tempList = map.get(key);
					tempList.add(idMap.get(doc.getString("keyword")));
					map.put(key, tempList);
				} else {
					Set<Long> list = new HashSet<Long>();
					list.add(idMap.get(doc.getString("keyword")));
					map.put(key, list);
				}
				String source = doc.getString("source");
				org.jsoup.nodes.Document jDoc = Jsoup.parse(source);
				obj.put("search_word", map.get(key));
				obj.put("date", StringTimeUtils.formatStringTime(doc.getString("publishTime")).substring(0,10));
				obj.put("source_url", doc.getString("url"));
				obj.put("name", doc.getString("title"));
				obj.put("press", doc.getString("ins"));
				Elements aEle = jDoc.select("div.author > span > a");
				JSONArray aArr = new JSONArray();
				for (Element author : aEle) {
					if (!(author.text().contains("通讯员") || author.text().contains("记者"))) {
						if (author.text().contains("")) {
							String[] as = author.text().split("");
							for (String a : as) {
								aArr.add(a.trim());
							}
						} else if (author.text().contains(" ")) {
							String[] as = author.text().split(" ");
							for (String a : as) {
								aArr.add(a.trim());
							}
						} else if (author.text().contains("　")) {
							String[] as = author.text().split("　");
							for (String a : as) {
								aArr.add(a.trim());
							}
						} else {
							aArr.add(author.text().trim());
						}
					}
				}
				obj.put("author", aArr);
				try {
					Elements pEle = jDoc.select("div.wxBaseinfo > p");
					for (Element element : pEle) {
						if (element.text().contains("分类号")) {
							String classNum = element.text().replace("分类号：", "").trim();
							obj.put("classification_num", classNum);
						} else if (element.text().contains("版号")) {
							String pN = element.text().replace("版号：", "").trim();
							obj.put("page_num", pN);
						} else if (element.text().contains("版名")) {
							String pName = element.text().replace("版名：", "").trim();
							obj.put("page_name", pName);
						} else if (element.text().contains("正文快照")) {
							String abs = element.text().replace("正文快照：", "").trim();
							obj.put("abs", abs);
						} else if (element.text().contains("关键词")) {
							String kt = element.text().replace("关键词：", "").trim();
							JSONArray kArr = new JSONArray();
							String[] split = kt.split(";");
							for (String kw : split) {
								kArr.add(kw.trim());
							}
							obj.put("keywords", kArr);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
//				fw.write(obj.toJSONString()+ "\r\n");
//				fw.flush();
				resultMap.put(key, obj);
			}
			for (String key : resultMap.keySet()) {
				fw.write(resultMap.get(key) + "\r\n");
				fw.flush();
			}
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
	}
	
	public static void patentParse() {
		int count = 0;
		Document searchDoc = new Document("type","patent");
		MongoCursor<Document> cursor = collection.find(searchDoc).iterator();
		Map<String,Set<Long>> map = new HashMap<String,Set<Long>>();
		Map<String,JSONObject> resultMap = new HashMap<String,JSONObject>();
		try {
			FileWriter fw = new FileWriter("data/cnki/patent.txt");
			while (cursor.hasNext()) {
//				System.out.println(count++);
				JSONObject obj = new JSONObject();
				Document doc = cursor.next();
//				String key = doc.getString("title");
				String key = getFileName(doc.getString("url")) + doc.getString("title") + doc.getString("ins");
				if (map.containsKey(key)) {
					System.out.println(key + " 重复");
					Set<Long> tempList = map.get(key);
					tempList.add(idMap.get(doc.getString("keyword")));
					map.put(key, tempList);
				} else {
					Set<Long> list = new HashSet<Long>();
					list.add(idMap.get(doc.getString("keyword")));
					map.put(key, list);
				}
				String source = doc.getString("source");
				org.jsoup.nodes.Document jDoc = Jsoup.parse(source);
				obj.put("search_word", map.get(key));
				obj.put("patent_url", doc.getString("url"));
				obj.put("patent_name", doc.getString("title"));
				try {
					Elements infoEle = jDoc.select("table#box >tbody > tr > td");
					for (int i = 0; i < infoEle.size()-1; i = i + 2) {
						String ct = infoEle.get(i).text();
						String nt = infoEle.get(i+1).text();
						if (ct.contains("专利分类号")) {
							JSONArray pcArr = new JSONArray();
							String[] pC = nt.split(";");
							for (String pc : pC) {
								pcArr.add(pc.replace("\u00A0", "").trim());
							}
							obj.put("patent_classification", pcArr);
						} else if (ct.contains("申请日")) {
							obj.put("application_date", nt.replace("\u00A0", "").trim());
						} else if (ct.contains("公开日")) {
							obj.put("publication_date", nt.replace("\u00A0", "").trim());
						} else if (ct.contains("地址")) {
							obj.put("address", nt.replace("\u00A0", "").trim());
						} else if (ct.contains("代理机构")) {
							obj.put("agency", nt.replace("\u00A0", "").trim());
						} else if (ct.contains("代理人")) {
							obj.put("agent", nt.replace("\u00A0", "").trim());
						} else if (ct.contains("公开号")) {
							obj.put("publication_num", nt.replace("\u00A0", "").trim());
						} else if (ct.contains("【申请人】")) {
							JSONArray appArr = new JSONArray();
							String[] as = nt.split(";");
							for (String a : as) {
								appArr.add(a.replace("\u00A0", "").trim());
							}
							obj.put("holder", appArr);
						} else if (ct.contains("发明人")) {
							JSONArray invArr = new JSONArray();
							String[] is = nt.split(";");
							for (String s : is) {
								invArr.add(s.replace("\u00A0", "").trim());
							}
							obj.put("inventor", invArr);
						} else if (ct.contains("摘要")) {
							obj.put("patent_abs", nt.replace("\u00A0", "").trim());
						} else if (ct.contains("主权项")) {
							obj.put("patent_claim", nt.replace("\u00A0", "").trim());
						} else if (ct.contains("【申请号】")) {
							obj.put("application_num", nt.replace("\u00A0", "").trim());
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				resultMap.put(key, obj);
//				fw.write(obj.toJSONString()+ "\r\n");
//				fw.flush();
			}
			for (String key : resultMap.keySet()) {
				fw.write(resultMap.get(key) + "\r\n");
				fw.flush();
			}
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
	}
	
	public static void standardParse() {
		int count = 0;
		Document searchDoc = new Document("type","standard");
		MongoCursor<Document> cursor = collection.find(searchDoc).iterator();
		Map<String,Set<Long>> map = new HashMap<String,Set<Long>>();
		Map<String,JSONObject> resultMap = new HashMap<String,JSONObject>();
		Set<String> kSet = new HashSet<String>();
		try {
			FileWriter fw = new FileWriter("data/cnki/standard.txt");
			FileWriter kw = new FileWriter("data/cnki/standard_key.txt");
			while (cursor.hasNext()) {
//				System.out.println(count++);
				JSONObject obj = new JSONObject();
				Document doc = cursor.next();
//				String key = doc.getString("title");
				String key = getFileName(doc.getString("url")) + doc.getString("title") + doc.getString("publishTime");
				if (map.containsKey(key)) {
					System.out.println(key + " 重复");
					Set<Long> tempList = map.get(key);
					tempList.add(idMap.get(doc.getString("keyword")));
					map.put(key, tempList);
				} else {
					Set<Long> list = new HashSet<Long>();
					list.add(idMap.get(doc.getString("keyword")));
					map.put(key, list);
				}
				String source = doc.getString("source");
				org.jsoup.nodes.Document jDoc = Jsoup.parse(source);
				obj.put("search_word", map.get(key));
				obj.put("standard_url", doc.getString("url"));
				try {
					Elements infoEle = jDoc.select("table#box >tbody > tr > td");
					for (int i = 0; i < infoEle.size()-1; i = i + 2) {
						String ct = infoEle.get(i).text();
						kSet.add(ct);
						String nt = infoEle.get(i+1).text();
						if (ct.contains("主题词")) {
							JSONArray pcArr = new JSONArray();
							String[] pC = nt.split(";");
							for (String pc : pC) {
								pcArr.add(pc.replace("\u00A0", "").trim());
							}
							obj.put("standard_keyword", pcArr);
						} else if (ct.contains("英文标准名称")) {
							obj.put("standard_name_en", nt.replace("\u00A0", "").trim());
						} else if (ct.contains("原文标准名称") || ct.contains("中文标准名称")) {
							obj.put("standard_name_cn", nt.replace("\u00A0", "").trim());
						} else if (ct.contains("【标准号】")) {
							obj.put("standard_num", nt.replace("\u00A0", "").trim());
						} else if (ct.contains("【标准状态】")) {
							obj.put("standard_status", nt.replace("\u00A0", "").trim());
						} else if (ct.contains("【发布日期】")) {
							obj.put("standard_pub_date", nt.replace("\u00A0", "").trim());
						} else if (ct.contains("实施")) {
							obj.put("standard_exc_date", nt.replace("\u00A0", "").trim());
						} else if (ct.contains("摘要")) {
							obj.put("standard_abs", nt.replace("\u00A0", "").trim());
						} else if (ct.contains("发布单位") || ct.contains("发布部门")) {
							obj.put("standard_pub_unit", nt.replace("\u00A0", "").trim());
						} else if (ct.contains("起草单位")) {
							obj.put("standard_draft_unit", nt.replace("\u00A0", "").trim());
						} else if (ct.contains("中国标准分类号")) {
							obj.put("standard_ccn", nt.replace("\u00A0", "").trim());
						} else if (ct.contains("国际标准分类号")) {
							obj.put("standard_icn", nt.replace("\u00A0", "").trim());
						}
					}
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				resultMap.put(key, obj);
//				fw.write(obj.toJSONString()+ "\r\n");
//				fw.flush();
			}
			for (String key : resultMap.keySet()) {
				fw.write(resultMap.get(key) + "\r\n");
				fw.flush();
			}
			for (String string : kSet) {
				kw.write(string+ "\r\n");
				kw.flush();
			}
			fw.close();
			kw.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cursor.close();
		}
	}
	
	
	public static String getFileName(String input) {
		String fileName = "";
		String regex = "(?=filename).*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		while (m.find()) {
			fileName = m.group();
		}
		return fileName;
	}
	
	public static void init() {
		try {
			BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/kw.txt");
			String input = "";
			while ((input = br.readLine())!=null) {
				String[] split = input.split("\t");
				idMap.put(split[1], Long.valueOf(split[0]));
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
