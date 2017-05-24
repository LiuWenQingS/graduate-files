package com.hiekn.kc;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;
import com.hiekn.util.HttpReader;
import com.hiekn.util.StaticHttpReader;

public class ZhihuTopic {
	
	private static final String cookie = "d_c0=\"AACA2RBFeQqPTnKpVRyoW-IUVkZkC8xRKj0=|1472716969\"; _zap=69909963-adfe-4022-8fa5-0ff42990e5d5; _za=74211e80-c5de-4907-b4b5-a60f6b1a1868; __utmt=1; __utma=51854390.1392318335.1472716972.1472716972.1472716972.1; __utmb=51854390.2.10.1472716972; __utmc=51854390; __utmz=51854390.1472716972.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utmv=51854390.000--|3=entry_date=20160901=1; q_c1=a4002561883248ba8665a573849d195d|1472716969000|1472716969000; l_cap_id=\"MzUyODAzNTIzMDgwNDBkNmE3YTEwYTJiNTFhMGVlMDY=|1472716969|c8b3b25a75d665309f4f73b4cdbdd10c88ded4e4\"; cap_id=\"OTgyYjczMjRlMTE4NDBiZWE3NmFjZjM4ZGQ4OGMyYzk=|1472716969|488c3409947bd648d3fdc3bd9816a3a1d37c8fbf\"; n_c=1";
	private static final String logCookie = "q_c1=d0734d5c59a34a31856b70f387b5461b|1472795577000|1472795577000; _xsrf=f03df42f71136b9a71635a0c8261bee3; l_cap_id=\"Y2ExYTBhZGEzODAwNGMyNTk2ZjM2OWM1M2ExMzEwNTk=|1472795577|4310c469febae8ada8619df805d710f5db4fab1d\"; cap_id=\"ZTc2MzZmZDI1NWQ3NGRlNGJiYzU2YTQ0MzFlY2E4MzY=|1472795577|af38c79ae7027c1d86ea8098ea334fa27b830073\"; _za=9196ff2d-8e80-4d99-ac73-c7a31e329fe8; d_c0=\"AEAASu9wegqPToYEsBGkDI-8aEs8Olv3FFc=|1472795578\"; _zap=32e05284-c5bd-46ab-90a7-f209f50fe3ba; __utmt=1; login=\"YjM2MDg3ZTUzMGNmNGZiMTlmZTk0YjY1YzM4YWY1ZDQ=|1472795592|e778e5337d1783c0e31fe2262f2952cabffb4aec\"; n_c=1; s-q=%E5%A4%A7%E6%95%B0%E6%8D%AE; s-i=1; sid=nc7ugmmo; s-t=autocomplete; __utma=51854390.1355841258.1472795582.1472795582.1472795582.1; __utmb=51854390.16.9.1472795647946; __utmc=51854390; __utmz=51854390.1472795582.1.1.utmcsr=(direct)|utmccn=(direct)|utmcmd=(none); __utmv=51854390.100--|2=registration_date=20160902=1^3=entry_date=20160902=1; a_t=\"2.0AHBA8SxTegoXAAAABaHwVwBwQPEsU3oKAEAASu9wegoXAAAAYQJVTcig8FcAbFk1teUFQBMHQbK3HlVT0j3SBnjjguyZ3x3_MnJDYcHicDVfIYs14Q==\"; z_c0=Mi4wQUhCQThTeFRlZ29BUUFCSzczQjZDaGNBQUFCaEFsVk55S0R3VndCc1dUVzE1UVZBRXdkQnNyY2VWVlBTUGRJR2VB|1472795653|4c697dc38bea4c27464126d14690c04c423fb893";
	private static final String file = "iterator_id_science.txt";
	private static final int sleep = 3;
	
	public static void main(String[] args) {
//		try {
//			Map<String,Integer> mainTopicIdMap = getTopicId();
//			for (Entry<String,Integer> entry : mainTopicIdMap.entrySet()) {
//				int mainTopicId = entry.getValue();
//				getSubTopicId(mainTopicId);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		try {
//			List<Integer> subTopicIdList = getSubTopicIdList();
//			for (Integer integer : subTopicIdList) {
//				int subTopicId = integer;
//				
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		try {
			iteratorId(19556664,0);
		} catch (Exception e) {
			
		}
	}
	
	/**
	 * 循环遍历子话题
	 * @param topicId
	 */
	public static void iteratorId(int topicId, int more) {
		try {
			FileWriter fw = new FileWriter("data/zhihu/"+file,true);
			CloseableHttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = null;
			HttpEntity entity = null;
			HttpPost post = null;
			String body = "";
			try {
				for (int i = 0; i < 5; i++) {
					String url = "";
					if (more == 0) {
						url = "https://www.zhihu.com/topic/" + topicId + "/organize/entire?child=&parent=";
					} else {
						url = "https://www.zhihu.com/topic/" + topicId + "/organize/entire?child="+more+"&parent=";
					}
					post = new HttpPost(url);
					List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
					nvps.add(new BasicNameValuePair("_xsrf", "f03df42f71136b9a71635a0c8261bee3"));  
					post.setEntity(new UrlEncodedFormEntity(nvps)); 
					post.addHeader("Cookie", logCookie);
					post.addHeader("Accept", "*/*");
					post.addHeader("Accept-Encoding", "gzip, deflate");
					post.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4");
					post.addHeader("Connection", "keep-alive");
					post.addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
					post.addHeader("Host","www.zhihu.com");
					post.addHeader("Origin","https://www.zhihu.com");
					post.addHeader("Referer","https://www.zhihu.com/topic/"+topicId+"/organize/entire");
					post.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
					response = client.execute(post);
					entity = response.getEntity();
					if (entity!=null) {
						body = EntityUtils.toString(entity);
						TimeUnit.SECONDS.sleep(sleep);
						break;
					}
				}
				JSONObject parseObj = JSONObject.parseObject(body);
				if (parseObj.containsKey("msg")) {
					JSONArray arr1 = parseObj.getJSONArray("msg");
					JSONArray parentTopicArr = (JSONArray) JSONArray.parseArray(arr1.toJSONString()).get(0);
					JSONArray subTopicArr = (JSONArray) JSONArray.parseArray(arr1.toJSONString()).get(1);
					int parentTopicId = Integer.parseInt(parentTopicArr.getString(2));
					if (subTopicArr.size() > 0) {
						isArray(subTopicArr.toJSONString(),parentTopicId);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			fw.close();
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static boolean isArray(String str,int parentTopicId) {
		try {
			JSON json = (JSON) JSON.parse(str);
			if (json instanceof JSONArray) {
				JSONArray arr = JSONArray.parseArray(json.toJSONString());
				for (Object object : arr) {
					String objStr = object.toString();
					if (!isArray(objStr,parentTopicId)) {
						System.out.println(arr);
						if (arr.get(0).equals("topic")) {
							int iteratorId = Integer.parseInt(arr.getString(2));
							String iteratorTopic = arr.getString(1);
							JSONObject resultObj = new JSONObject();
							resultObj.put("parentId", parentTopicId);
							resultObj.put("topic", iteratorTopic);
							resultObj.put("topicId", iteratorId);
							
							FileWriter fw = new FileWriter("data/zhihu/"+file,true);
							fw.write(resultObj.toJSONString() + "\r\n");
							fw.flush();
							fw.close();
							iteratorId(iteratorId,0);
						} else if (arr.get(1).equals("加载更多")) {
							int iteratorId = Integer.parseInt(arr.getString(2));
							int iteratorParentId = Integer.parseInt(arr.getString(3));
							iteratorId(iteratorParentId,iteratorId);
						}
						break;
					}
				}
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	/**
	 * 获得主要topicId
	 * @return
	 * @throws Exception
	 */
	public static Map<String,Integer> getTopicId() throws Exception {
		FileWriter fw = new FileWriter("data/zhihu/topic_id.txt",true);
		Map<String,Integer> topicMap = new HashMap<String,Integer>();
		HttpReader reader = new StaticHttpReader();
		String source = reader.readSource("https://www.zhihu.com/topics");
		Document doc = Jsoup.parse(source);
		Elements topElements = doc.select("li.zm-topic-cat-item");
		for (Element element : topElements) {
			try {
				String topic = element.text();
				int topicId = Integer.parseInt(element.attr("data-id"));
				JSONObject resultObj = new JSONObject();
				resultObj.put("topic",topic);
				resultObj.put("topicId", topicId);
				fw.write(resultObj.toJSONString()+"\r\n");
				fw.flush();
				topicMap.put(topic, topicId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		reader.close();
		fw.close();
		return topicMap;
	}
	
	/**
	 * 获得第二层topicId
	 * @param mainTopicId
	 * @throws Exception
	 */
	public static void getSubTopicId(int mainTopicId) throws Exception {
		FileWriter fw = new FileWriter("data/zhihu/topic_id.txt",true);
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpResponse response = null;
		HttpEntity entity = null;
		HttpPost post = null;
		int offset = 0;
		while (true) {
			String body = "";
			for (int i = 0; i < 5; i++) {
				String url = "https://www.zhihu.com/node/TopicsPlazzaListV2";
				JSONObject paramObj = new JSONObject();
				paramObj.put("topic_id", mainTopicId);
				paramObj.put("offset", offset);
				paramObj.put("hash_id", "");
				post = new HttpPost(url);
				List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
				nvps.add(new BasicNameValuePair("method", "next"));  
				nvps.add(new BasicNameValuePair("params", paramObj.toJSONString()));  
				post.setEntity(new UrlEncodedFormEntity(nvps)); 
				post.setHeader("Cookie", cookie);
				post.setHeader("Accept", "*/*");
				post.setHeader("Accept-Encoding", "gzip, deflate");
				post.setHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4");
				post.setHeader("Connection", "keep-alive");
				post.setHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
				post.setHeader("Host","www.zhihu.com");
				post.setHeader("Origin","https://www.zhihu.com");
				post.setHeader("Referer","https://www.zhihu.com/topics");
				post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
				response = client.execute(post);
				entity = response.getEntity();
				if (entity!=null) {
					body = EntityUtils.toString(entity);
					break;
				}
			}
			JSONObject parseObj = JSONObject.parseObject(body);
			if (!parseObj.containsKey("msg")) break;
			JSONArray msgArr = parseObj.getJSONArray("msg");
			for (Object object : msgArr) {
				String result = object.toString().replace("\n", "");
				Document doc = Jsoup.parse(result);
				Element element = doc.getElementsByTag("a").first();
				String topic = element.text();
				int topicId = Integer.parseInt(element.attr("href").replace("/topic/", ""));
				JSONObject resultObj = new JSONObject();
				resultObj.put("topic",topic);
				resultObj.put("topicId", topicId);
				resultObj.put("parentId", mainTopicId);
				fw.write(resultObj.toJSONString() + "\r\n");
				fw.flush();
			}
			if (msgArr.size() == 0) {
				break;
			}
			offset = offset + 20;
			TimeUnit.SECONDS.sleep(5);
		}
		client.close();
		fw.close();
	}
	
	
	/**
	 * 获得子话题id
	 * @return
	 * @throws Exception
	 */
	public static List<Integer> getSubTopicIdList()	throws Exception {
		List<Integer> subTopicIdList = new ArrayList<Integer>();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/zhihu/topic_id.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			JSONObject obj = JSONObject.parseObject(input);
			if (obj.containsKey("parentId")) {
				int id = obj.getIntValue("topicId");
				subTopicIdList.add(id);
			}
		}
		br.close();
		return subTopicIdList;
	}
	
	
	
}
