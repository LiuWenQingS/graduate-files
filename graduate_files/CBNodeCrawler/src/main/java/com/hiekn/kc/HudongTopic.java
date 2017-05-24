package com.hiekn.kc;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;

public class HudongTopic {
	
	private static Set<String> dedupSet = new HashSet<String>();
	
	public static void main(String[] args) {
		try {
//			process("医学",0);
			additionalTopic();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void additionalTopic() throws Exception {
		FileWriter fw = new FileWriter("data/zhihu/hudong_list.txt",true);
		BufferedReader br = BufferedReaderUtil.getBuffer("data/zhihu/hudong.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			JSONObject obj = JSONObject.parseObject(input);
			String key = obj.getString("topic");
			System.out.println(key);
			List<String> topicList = getTopicList(key);
			obj.put("topicList", topicList);
			fw.write(obj.toJSONString() + "\r\n");
			fw.flush();
		}
		br.close();
		fw.close();
	}
	
	public static List<String> getTopicList(String key) throws Exception {
		
		List<String> topicList = new ArrayList<String>();
		String url = "http://fenlei.baike.com/" + URLEncoder.encode(key,"utf-8") + "/list";
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet get = null;
		HttpResponse response = null;
		HttpEntity entity = null;
		String body = "";
		for (int i = 0; i < 5; i++) {
			get = new HttpGet(url);
			get.addHeader("Cookie", "hd_uid=6116658641470202839; gr_user_id=a5460c3f-8405-44d8-9c98-93fe523e253b; hd_firstaccessurl=http://www.baike.com/; ssr_sid=TBfc93Npe3GU6CSE; hd_referer=https://www.baidu.com/link?url=o8dOSSpoll_Cms3pUiE77GcfIwlBBatP8zKq_NvuzIe&wd=&eqid=b5a6c79b000140500000000457c9327f; ssr_auth=0764COVZm%2F9zEFWifYV0cwQEnfs4fNTFivek3bs25nWj8K%2BxPGvyKg; __utma=175268785.661413575.1470202839.1472786504.1472803497.4; __utmc=175268785; __utmz=175268785.1472803497.4.2.utmcsr=hangye.baike.com|utmccn=(referral)|utmcmd=referral|utmcct=/; _ga=GA1.2.661413575.1470202839; JSESSIONID=baced69_UQYd18eDG6OBv");
			get.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			get.addHeader("Accept-Encoding", "gzip, deflate, sdch");
			get.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4");
			get.addHeader("Connection", "keep-alive");
			get.addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
			get.addHeader("Host","fenlei.baike.com");
			get.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
			try {
				response = client.execute(get);
				entity = response.getEntity();
				if (entity!=null) {
					body = EntityUtils.toString(entity,"utf-8");
					Document doc = Jsoup.parse(body);
					Elements ddEle = doc.select("dl.link_blue > dd > a");
					for (Element element : ddEle) {
						topicList.add(element.text());
					}
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		client.close();
		
		TimeUnit.SECONDS.sleep(2);
		return topicList;
	}
	
	public static void process(String key,int parentId) throws Exception {
		
		FileWriter fw = new FileWriter("data/zhihu/hudong.txt",true);
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet get = null;
		HttpResponse response = null;
		HttpEntity entity = null;
		String body = "";
		for (int i = 0; i < 5; i++) {
			String catUrl = "http://www.baike.com/category/Ajax_cate.jsp?catename=" + URLEncoder.encode(key,"utf-8");
			get = new HttpGet(catUrl);
			get.addHeader("Cookie", "hd_uid=6116658641470202839; gr_user_id=a5460c3f-8405-44d8-9c98-93fe523e253b; hd_firstaccessurl=http://www.baike.com/; ssr_sid=TBfc93Npe3GU6CSE; gr_session_id_8d604bc1fcb84d12=902915ff-6781-4653-beed-a327d2da6d0a; CNZZDATA1260070928=1582652233-1472781860-%7C1472798075; hd_referer=https://www.baidu.com/link?url=o8dOSSpoll_Cms3pUiE77GcfIwlBBatP8zKq_NvuzIe&wd=&eqid=b5a6c79b000140500000000457c9327f; ssr_auth=0764COVZm%2F9zEFWifYV0cwQEnfs4fNTFivek3bs25nWj8K%2BxPGvyKg; _gat=1; __utmt=1; _ga=GA1.2.661413575.1470202839; __utma=175268785.661413575.1470202839.1472786504.1472803497.4; __utmb=175268785.3.10.1472803497; __utmc=175268785; __utmz=175268785.1472803497.4.2.utmcsr=hangye.baike.com|utmccn=(referral)|utmcmd=referral|utmcct=/; JSESSIONID=baced69_UQYd18eDG6OBv");
			get.addHeader("Accept", "*/*");
			get.addHeader("Accept-Encoding", "gzip, deflate, sdch");
			get.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4");
			get.addHeader("Connection", "keep-alive");
			get.addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
			get.addHeader("Host","www.baike.com");
			get.addHeader("Referer","http://www.baike.com/category/treeManage.jsp");
			get.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
			try {
				response = client.execute(get);
				entity = response.getEntity();
				if (entity!=null) {
					body = EntityUtils.toString(entity);
					JSONArray arr = JSONArray.parseArray(body);
					for (Object object : arr) {
						JSONObject obj = (JSONObject) object;
						String topic = obj.getString("name");
						int topicId = Integer.valueOf(obj.getString("id"));
						JSONObject resultObj = new JSONObject();
						resultObj.put("topic", topic);
						resultObj.put("parentId", parentId);
						resultObj.put("topicId", topicId);
						fw.write(resultObj.toJSONString() + "\r\n");
						fw.flush();
						if (!dedupSet.contains(topic)) {
							dedupSet.add(topic);
							process(topic,topicId);
						}
					}
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		client.close();
		fw.close();
	}
}
