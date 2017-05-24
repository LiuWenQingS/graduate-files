package com.hiekn.crawler;

import com.hiekn.util.HttpReader;
import com.hiekn.util.BufferedReaderUtil;
import com.hiekn.util.StaticHttpReader;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.hiekn.bean.ContentBean;
import com.hiekn.bean.DocSearchBean;
import com.hiekn.bean.PaperSearchBean;
import com.hiekn.bean.PatentSearchBean;
import com.hiekn.bean.SearchBean;
import com.hiekn.bean.StandardSearchBean;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
public class Baiduxueshu {

	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub
		try
		{
			crawler_chinese();
//		    crawler_english();
//		    parser();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
	}
	public static void crawler_chinese () throws Exception
	{
		HttpReader reader = new StaticHttpReader();
		
		FileWriter fw = new FileWriter("data/baidu/result_chinese.txt");
		BufferedReader br = BufferedReaderUtil.getBuffer("data/baidu/keyword_chinese.txt");
		
		String input = "";
		int count = 0;
		
		while((input = br.readLine()) != null)
		{
			String keyWord = input;
			
			String paperUrl = "http://xueshu.baidu.com/s?wd=" + URLEncoder.encode(keyWord,"utf-8") + "&pn=" + count;
			String paperSource = reader.readSource(paperUrl);
			
			Document paperDoc = Jsoup.parse(paperSource); 
			Elements paperElement = paperDoc.select("div#bdxs_result_lists").select("div.sc_content");
			
			for(Element element : paperElement)
			{
				String articleUrl = "http://xueshu.baidu.com" + element.select("h3").attr("a").trim();
				String articleSource = reader.readSource(articleUrl);
				
				Document articleDoc = Jsoup.parse(articleSource);
				Elements articleElement = element.select("div#dtl_l") 
			}
		}
		
	}
		

}
