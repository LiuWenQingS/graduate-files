package com.hiekn.other;

import java.net.CookieStore;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class GetQxbCookie {
	public static void main(String[] args) {
		try {
			getCookie();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void getCookie() throws Exception {
		String url = "http://www.qixin.com/?from=wap";
		CloseableHttpClient client = HttpClientBuilder.create().build();
		HttpGet get = new HttpGet(url);
		HttpResponse response = client.execute(get);
		client.close();
	}
}
