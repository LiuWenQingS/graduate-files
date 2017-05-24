package com.hiekn.other;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ApiTest {
	public static void main(String[] args) {
		try {
			long start = System.currentTimeMillis();
			crawler();
			long finish = System.currentTimeMillis() - start;
			System.out.println(finish);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	
	public static void crawler() throws IOException {
		OkHttpClient client = new OkHttpClient();
		RequestBody formBody = new FormBody.Builder()
		    .add("entityId", "1005500").add("distance", "1").add("kgName", "kg_library")
		    .build();
		Request request = new Request.Builder()
		    .url("http://192.168.1.189:8081/SSE/ws/kg/graph/full/hasatts")
		    .post(formBody)
		    .build();
	
		Response response = client.newCall(request).execute();
		String source = response.body().string();
		System.out.println(source);
	}
	
	public static void test() throws Exception  {
		FileWriter fw = new FileWriter("work/k.txt");
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost("http://kejso.com/servlet/SearchServlet");
		post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
		post.setHeader("Cookie", "JSESSIONID=84280C1E32D2BE03E525A72D9EA08D21");
		post.setHeader("Host", "kejso.com");
		post.setHeader("Referer", "http://kejso.com/servlet/SearchServlet");
		post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
		post.setHeader("Upgrade-Insecure-Requests", "application/x-www-form-urlencoded");
		post.setHeader("Content-Type", "1");
		post.setHeader("Accept-Encoding", "gzip, deflate");
//		HttpPost post = new HttpPost("http://127.0.0.1:8080/mrdsj/ws/crawl");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("org", ""));
		params.add(new BasicNameValuePair("query", "宫琳"));
		params.add(new BasicNameValuePair("core", "people"));
		HttpEntity postParams = new UrlEncodedFormEntity(params,"utf-8");
		post.setEntity(postParams);
		CloseableHttpResponse httpResponse = httpClient.execute(post);
		String entity = EntityUtils.toString(httpResponse.getEntity());
		System.out.println("POST Response Status:: " + httpResponse.getStatusLine().getStatusCode());
		System.out.println(entity);
		if (entity.contains("宫琳")) {
			System.out.println("contains");
		}
		fw.write(entity);
		fw.flush();
		fw.close();
	}
}
