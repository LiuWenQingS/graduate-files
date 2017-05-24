package com.hiekn.util;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

public class ReadHttpFileUtil implements IReadHttpFileUtil {
	private Logger log = Logger.getLogger(ReadHttpFileUtil.class);

	public String readPageSourceCommon(String urlString, String encode) {
		URL url;
		int responsecode;
		HttpURLConnection urlConnection;
		String str = "";
		try {
//			InetSocketAddress proxyAddr = new InetSocketAddress("127.0.0.1",1080);
//			Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddr);
			// 生成一个URL对象，要获取源代码的网页地址为：http://www.sina.com.cn
			url = new URL(urlString);
			// 打开URL
			urlConnection = (HttpURLConnection) url.openConnection();
//			urlConnection = (HttpURLConnection) url.openConnection(proxy);
			
			urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36");
			urlConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
			urlConnection.setRequestProperty("Connection", "keep-alive");
			urlConnection.setRequestProperty("Host", "www.itjuzi.com");
			urlConnection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
			urlConnection.setConnectTimeout(10*1000);
			urlConnection.setReadTimeout(10*10000);
			urlConnection.setUseCaches(false);
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setAllowUserInteraction(false);

			// 获取服务器响应代码
			responsecode = urlConnection.getResponseCode();

//			System.out.println(urlConnection.getContent());
			if (responsecode == 200) {
				// 得到输入流，即获得了网页的内容
				str = InputStreamUtils.InputStreamTOString(urlConnection.getInputStream(),encode);
			} else {
				System.out.println("Common获取不到网页的源码，服务器响应代码为：" + responsecode);
				return str = "再来一次";
			}
		} catch (Exception e) {
			System.out.println("Common获取不到网页的源码,出现异常：");
			log.error(e.toString());
			e.printStackTrace();
			return str = "再来一次";
		}
		urlConnection.disconnect();
		return str;
	}

	public String readPageSourceAjax(String urlString, String encode) {

		URL url;
		int responsecode;
		HttpURLConnection urlConnection;
		String str = "";
		try {
			// 生成一个URL对象，要获取源代码的网页地址为：http://www.sina.com.cn
			url = new URL(urlString);
			// 打开URL
			urlConnection = (HttpURLConnection) url.openConnection();

			urlConnection.setRequestProperty("User-Agent",
					"Mozilla/4.0 (compatible; MSIE 6.01; Windows NT 5.0)");
			urlConnection.setRequestProperty("Content-type",
					"application/x-www-form-urlencoded");
			urlConnection.setRequestProperty("X-Requested-With",
					"XMLHttpRequest");
			urlConnection.setConnectTimeout(15000);
			urlConnection.setReadTimeout(50000);
			urlConnection.setUseCaches(false);
			urlConnection.setDoOutput(true);
			urlConnection.setDoInput(true);
			urlConnection.setAllowUserInteraction(false);

			// 获取服务器响应代码
			responsecode = urlConnection.getResponseCode();
			if (responsecode == 200) {
				// 得到输入流，即获得了网页的内容
				str = InputStreamUtils.InputStreamTOString(urlConnection.getInputStream(),encode);
			} else {
				System.out.println("Ajax获取不到网页的源码，服务器响应代码为：" + responsecode);
				return str = "再来一次";
			}
		} catch (Exception e) {
			System.out.println("Ajax获取不到网页的源码,出现异常：" + e);
			return str = "再来一次";
		}

		urlConnection.disconnect();

		return str;
	}

	public static void main(String[] args) {
		ReadHttpFileUtil util = new ReadHttpFileUtil();
		try {
			// System.out.println(util.readPageSourceCommon("http://zh.wikipedia.org/zh-cn/"
			// + URLEncoder.encode("习近平", "UTF-8"), "UTF-8"));
			URLEncoder.encode("习近平", "UTF-8");
			String encode = "utf-8";
			
			System.out.println(util.readPageSourceCommon("https://zh.wikipedia.org/wiki/%E9%82%93%E8%B6%85", "utf-8"));
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
