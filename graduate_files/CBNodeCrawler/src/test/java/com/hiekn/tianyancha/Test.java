package com.hiekn.tianyancha;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JOptionPane;
import javax.ws.rs.GET;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;
import com.hiekn.util.HttpReader;
import com.hiekn.util.StaticHttpReader;

public class Test {
	private final static long interval = 2000;
	private final static String staticCookie = "TYCID=979fbeefda3c4bd5941728e0625d4a26; tnet=101.81.123.253; _pk_ref.1.e431=%5B%22%22%2C%22%22%2C1470044716%2C%22https%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3DvGAXwtYzQBqSFQwOXzIDRd4uCeSEFryDDHo1kTSudi_JSt8OBFieQjrcuSfQ5m4y%26wd%3D%26eqid%3Da470c4f4000da98700000004579f0eef%22%5D; Hm_lvt_e92c8d65d92d534b0fc290df538b4758=1470028080,1470030622,1470041843,1470042482; Hm_lpvt_e92c8d65d92d534b0fc290df538b4758=1470044863; token=44d8f67add85477ea3b02f9c577269e3; _utm=19d6ae64175b4dabbb13c125b7a450bf; _pk_id.1.e431=11af6002f176e9f4.1467103721.8.1470044863.1470042503.; _pk_ses.1.e431=*; token=";
	private static String comName = "";
	private static String cookie = "";
	private static HttpReader reader = null;
	public static void main(String[] args) {
		try {
			crawler();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void crawler() throws Exception {
		Set<String> crawlerSet = getCrawlerSet();
		System.out.println("剩余公司为"+crawlerSet.size()+"家");
		FileWriter fw = new FileWriter("data/kc/result.txt",true);
		for (String com : crawlerSet) {
			System.out.println(com);
			comName = com.trim();
			getToken(comName);
			String comUrl = getComUrl(comName);
			if (comUrl.equals("nodata")) {
				fw.write(comName + "\t" + "无数据" + "\r\n");
				fw.flush();
				continue;
			}
			String result = getComInfo(comUrl);
			System.out.println(comName);
			fw.write(comName + "\t" + result + "\r\n");
			fw.flush();
		}
		fw.close();
	}
	
	/**
	 * 获得剩余公司信息
	 * @return
	 * @throws Exception
	 */
	public static Set<String> getCrawlerSet() throws Exception {
		Set<String> crawlerSet = new HashSet<String>();
		Set<String> crawleredSet = new HashSet<String>();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/kc/result.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			String com = input.split("\t")[0];
			crawleredSet.add(com);
		}
		br = BufferedReaderUtil.getBuffer("data/kc/ent_all.txt");
		while ((input = br.readLine())!=null) {
			String com = input;
			crawlerSet.add(com);
		}
		crawlerSet.removeAll(crawleredSet);
		br.close();
		return crawlerSet;
	}
	
	/**
	 * 获得公司信息
	 * @param comUrl
	 * @return
	 * @throws IOException
	 */
	public static String getComInfo(String comUrl) throws Exception {
		String result = "";
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(comUrl);
		getToken(comName);
		get.setHeader("Accept", "application/json, text/plain, */*");
		get.setHeader("Cookie",cookie);
		get.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
		get.setHeader("Referer",comUrl.replace(".json", ""));
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		result = EntityUtils.toString(entity);
		client.close();
		Thread.sleep(interval);
		return result;
	}
	
	/**
	 * 获得公司url
	 * @param comName
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getComUrl(String comName) throws Exception {
		String prefix = "http://www.tianyancha.com/company/";
		String url = "";
		String absInfoUrl = "http://www.tianyancha.com/s.json";
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpPost post = new HttpPost(absInfoUrl);
		JSONObject jObj = new JSONObject();
		jObj.put("word", comName);
		jObj.put("pageSize", "20");
		String json = jObj.toJSONString();
		StringEntity str = new StringEntity(json,"utf-8");
		str.setContentType("application/json");
		post.setEntity(str);
		post.setHeader("Accept", "application/json, text/plain, */*");
		post.setHeader("Cookie",cookie);
		post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
		post.setHeader("Referer","http://www.tianyancha.com/search/"+URLEncoder.encode(comName,"utf-8")+"?checkFrom=searchBox");
		CloseableHttpResponse response = client.execute(post);
		HttpEntity entity = response.getEntity();
		String res = EntityUtils.toString(entity);
		JSONObject obj = JSONObject.parseObject(res);
		String state = obj.getString("state");
		if (state.equals("warn")) {
			JOptionPane.showMessageDialog(null, "请滑块");
			System.exit(0);
		}
		String message = obj.getString("message");
		if (message.equals("无数据")) {
			url = "nodata";
		} else {
			JSONArray arr = JSONArray.parseArray(obj.getString("data"));
			JSONObject arrObj = (JSONObject) arr.get(0);
			String id = arrObj.get("id")+"";
			url = prefix + id + ".json";
		}
		client.close();
		Thread.sleep(interval);
		return url;
	}
	
	/**
	 * 获得token
	 * @param comName
	 * @throws UnsupportedEncodingException
	 */
	public static void getToken(String comName) throws Exception {
		reader = new StaticHttpReader();
		String tokenUrl = "http://www.tianyancha.com/tongji/"+URLEncoder.encode(comName, "utf-8")+".json?random=?"+System.currentTimeMillis();
		String searchSource = reader.readSource(tokenUrl, "utf-8", staticCookie);
		JSONObject obj = JSONObject.parseObject(searchSource);
		String attStr = JSONObject.parseObject(obj.getString("data")).getString("v");
		String[] arr = attStr.split(",");
		char[] charr = new char[arr.length];
		int j = 0;
		for (String string : arr) {
			int i = Integer.valueOf(string);
			char c = (char) i;
			charr[j] = c;
			j++;
		}
		String result = String.valueOf(charr);
		String pattern = "(?<=token=).*?(?=;)";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(result);
		String token = "";
		while (m.find()) {
			token = m.group();
		}
		cookie = staticCookie + token;
		Thread.sleep(interval);
	}
	
	
	public static void T1() throws ClientProtocolException, IOException {
		reader = new StaticHttpReader();
		String searchUrl = "http://www.tianyancha.com/tongji/%E6%A1%90%E6%A2%93%E5%88%9B%E5%85%B4%E8%87%AA%E5%8A%A8%E5%8C%96%E8%AE%BE%E5%A4%87%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8.json?random=" + System.currentTimeMillis();
		String cookie = "TYCID=979fbeefda3c4bd5941728e0625d4a26; tnet=101.81.123.253; _pk_ref.1.e431=%5B%22%22%2C%22%22%2C1470044716%2C%22https%3A%2F%2Fwww.baidu.com%2Flink%3Furl%3DvGAXwtYzQBqSFQwOXzIDRd4uCeSEFryDDHo1kTSudi_JSt8OBFieQjrcuSfQ5m4y%26wd%3D%26eqid%3Da470c4f4000da98700000004579f0eef%22%5D; Hm_lvt_e92c8d65d92d534b0fc290df538b4758=1470028080,1470030622,1470041843,1470042482; Hm_lpvt_e92c8d65d92d534b0fc290df538b4758=1470044863; token=44d8f67add85477ea3b02f9c577269e3; _utm=19d6ae64175b4dabbb13c125b7a450bf; _pk_id.1.e431=11af6002f176e9f4.1467103721.8.1470044863.1470042503.; _pk_ses.1.e431=*; token=";
		String source = reader.readSource(searchUrl, "utf-8", cookie);
		System.out.println(source);
		JSONObject obj = JSONObject.parseObject(source);
		String attStr = JSONObject.parseObject(obj.getString("data")).getString("v");
		String[] arr = attStr.split(",");
		char[] charr = new char[arr.length];
		int j = 0;
		for (String string : arr) {
			int i = Integer.valueOf(string);
			char c = (char) i;
			charr[j] = c;
			j++;
		}
		String result = String.valueOf(charr);
		System.out.println(result);
		String pattern = "(?<=token=).*?(?=;)";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(result);
		while (m.find()) {
			String token = m.group();
			cookie = cookie + token;
		}
		String absInfoUrl = "http://www.tianyancha.com/s.json";
		CloseableHttpClient client = HttpClientBuilder.create().build();
//		String res = reader.readSource(absInfoUrl,"utf-8",cookie);
//		System.out.println(res);
		HttpPost post = new HttpPost(absInfoUrl);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("word","桐梓创兴自动化设备有限公司"));
		params.add(new BasicNameValuePair("pageSize","20"));
		JSONObject jObj = new JSONObject();
		jObj.put("word", "桐梓创兴自动化设备有限公司");
		jObj.put("pageSize", "20");
		String json = jObj.toJSONString();
		StringEntity str = new StringEntity(json,"utf-8");
		str.setContentType("application/json");
		post.setEntity(str);
		post.setHeader("Accept", "application/json, text/plain, */*");
		post.setHeader("Cookie",cookie);
		post.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
		post.setHeader("Referer","http://www.tianyancha.com/search/%E4%B8%8A%E6%B5%B7%E6%B5%B7%E7%BF%BC%E7%9F%A5?checkFrom=searchBox");
		CloseableHttpResponse response = client.execute(post);
		HttpEntity entity = response.getEntity();
		String res = EntityUtils.toString(entity);
		System.out.println(res);
		client.close();
		reader.close();
	}
	
}
