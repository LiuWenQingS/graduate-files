package com.hiekn.main;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.bson.Document;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class XnCrawler {

	public static void main(String[] args) {
		try {
			start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void start() throws ClientProtocolException, IOException, InterruptedException {
		Set<String> urlSet = new HashSet<String>();
		MongoClient mongoClient = new MongoClient("127.0.0.1",27017);
		MongoDatabase db = mongoClient.getDatabase("xn_new");
		MongoCollection<Document> collection = db.getCollection("news");
		String url = "http://www.xiniudata.com/api/company/track/getByTag";
		CloseableHttpClient client = HttpClients.createDefault();
		Map<Object,Object> map = new HashMap<Object,Object>();
		Set<Integer> idSet = new HashSet<Integer>();
		for (int i = 0; i < 22; i++) {
			idSet.add(i);
		}
		idSet.add(20001);
		idSet.add(20002);
		idSet.add(20003);
		for (int id : idSet) {
			HttpPost post = new HttpPost(url);
			post.setHeader("Connection","keep-alive");
			post.setHeader("Host","www.xiniudata.com");
			post.setHeader("Origin","http://www.xiniudata.com");
			post.setHeader("Cookie","keeploginsecret=PYR7YUTGXE89IEXLA9GJRI9VNSO3ET8T; userid=2350; token=7EXJG6IJD8TGBMBWD03SOQRB14YLKKRJ; Hm_lvt_42317524c1662a500d12d3784dbea0f8=1486289772,1486347249,1486703673,1488438135; Hm_lpvt_42317524c1662a500d12d3784dbea0f8=1488513049; location=http%3A%2F%2Fwww.xiniudata.com%2F%23%2F");
			post.setHeader("Content-Type","application/json");
			post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
			post.setHeader("X-Requested-With","XMLHttpRequest");
			post.setHeader("Pragma","no-cache");
			post.setHeader("Accept","*/*");
			post.setHeader("Accept-Encoding","gzip, deflate");
			post.setHeader("Accept-Language","zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4");
			
			
			for (int i = 0; i < 100; i++) {
				JSONObject pObj = new JSONObject();
				pObj.put("contentTypeId", 0);
				pObj.put("firstNewsId", null);
				pObj.put("isAuto", false);
				pObj.put("itemTypeId", 0);
				pObj.put("pageSize", 10);
				pObj.put("start", i * 10 + 200);
				pObj.put("tagId", id);
				pObj.put("tabList", new ArrayList<Integer>(){
					  {	   
						  add(60001);
						  add(60003);
					  }
						 });
				JSONObject resultObj = new JSONObject();
				resultObj.put("payload", pObj);
				StringEntity se = new StringEntity(resultObj.toString());
				post.setEntity(se);
				
				HttpResponse response = client.execute(post);
				HttpEntity entity = response.getEntity();
				String result = EntityUtils.toString(entity, "utf-8");
//				System.out.println(result);
				JSONObject jO = JSONObject.parseObject(result);
				JSONArray nArr = jO.getJSONArray("trackList");
				for (Object object : nArr) {
					JSONObject nO = (JSONObject) object;
					Document newsDoc = new Document();
					String nid = nO.getString("_id");
					if (!urlSet.contains(nid)) {
						urlSet.add(nid);
						newsDoc.append("title", nO.getString("title"));
						newsDoc.append("raw", nO.toJSONString());
						newsDoc.append("nid", nid);
						newsDoc.append("tag", nO.get("tagList"));
						String content = getNewsContentById(nid, client);
						newsDoc.append("content", content);
						collection.insertOne(newsDoc);
						TimeUnit.SECONDS.sleep(1);
					}
				}
				
				System.out.println("10 finish");
				
				try {
					map.put(id, jO.getJSONArray("trackList").getObject(0, JSONObject.class).get("tagList"));
				} catch (Exception e) {
					System.out.println(id + "put");
				}
				
				TimeUnit.SECONDS.sleep(1);
				
			}
		}
		
		for (Entry<Object,Object> entry : map.entrySet()) {
			System.out.println(entry.getKey() + "\t" + entry.getValue());
		}
		
		mongoClient.close();
	}
	
	/**
	 * 根据新闻id获得新闻详情内容
	 * @param id
	 * @param post
	 * @param client
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getNewsContentById(String id,CloseableHttpClient client) throws ClientProtocolException, IOException {
		String content = "";
		String url = "http://www.xiniudata.com/api/company/crawler/news/get";
		JSONObject pObj = new JSONObject();
		pObj.put("newsId", id);
		JSONObject resultObj = new JSONObject();
		resultObj.put("payload", pObj);
		StringEntity se = new StringEntity(resultObj.toString());
		HttpPost post = new HttpPost(url);
		post.setHeader("Connection","keep-alive");
		post.setHeader("Host","www.xiniudata.com");
		post.setHeader("Origin","http://www.xiniudata.com");
		post.setHeader("Cookie","keeploginsecret=PYR7YUTGXE89IEXLA9GJRI9VNSO3ET8T; userid=2350; token=7EXJG6IJD8TGBMBWD03SOQRB14YLKKRJ; Hm_lvt_42317524c1662a500d12d3784dbea0f8=1486289772,1486347249,1486703673,1488438135; Hm_lpvt_42317524c1662a500d12d3784dbea0f8=1488513049; location=http%3A%2F%2Fwww.xiniudata.com%2F%23%2F");
		post.setHeader("Content-Type","application/json");
		post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
		post.setHeader("X-Requested-With","XMLHttpRequest");
		post.setHeader("Pragma","no-cache");
		post.setHeader("Accept","*/*");
		post.setHeader("Accept-Encoding","gzip, deflate");
		post.setHeader("Accept-Language","zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4");
		post.setEntity(se);
		HttpResponse response = client.execute(post);
		HttpEntity entity = response.getEntity();
		String result = EntityUtils.toString(entity, "utf-8");
		JSONObject jO = JSONObject.parseObject(result);
		JSONArray cArr = jO.getJSONObject("news").getJSONArray("contents");
		for (Object object : cArr) {
			JSONObject cO = (JSONObject) object;
			content = content + cO.getString("content");
		}
		return content;
	}
	
	public static Set<String> getSet() {
		Set<String> set = new HashSet<String>();
		MongoClient client = new MongoClient("127.0.0.1",27017);
		MongoDatabase db = client.getDatabase("xn");
		MongoCollection<Document> collection = db.getCollection("news");
		MongoCursor<Document> cursor = collection.find().iterator();
		try {
			while (cursor.hasNext()) {
				set.add(cursor.next().getString("nid"));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		client.close();
		return set;
	}
}
