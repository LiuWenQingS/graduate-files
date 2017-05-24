/**
 * 
 */
/**
 * @author lcx
 * @date 2017年1月13日 下午3:12:15
 */
package com.hiekn.cnki;

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
import org.bson.Document;
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
import com.hiekn.util.BufferedReaderUtil;
import com.hiekn.util.HttpReader;
import com.hiekn.util.StaticHttpReader;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class CnkiAdvanceSearchCrawler {
	
	private static int total = 1;
	
	private static MongoClient client =  new MongoClient("127.0.0.1",27017);
	private static MongoDatabase db = client.getDatabase("data");
	
	public static void main(String[] args) {
		try {
			DocSearchBean dbean = new DocSearchBean();
			PaperSearchBean pbean = new PaperSearchBean();
			PatentSearchBean pabean = new PatentSearchBean();
			StandardSearchBean sbean = new StandardSearchBean();
			Set<String> keywordSet = getKeyword();
			for (String kw : keywordSet) {
//				crawler(dbean, kw);
//				crawler(pbean, kw);
//				crawler(pabean, kw);
//				crawler(sbean, kw);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		String input = "";
//		BufferedReader br = ReadFileUtil.getBufferedReader("patent.txt");
//		while((input = br.readLine())!=null){
//			input = input.trim();
//			System.out.println(input);
//			crawler(input);
//			Thread.sleep(1000);
//		}
//		ArrayList<String> tarList = new ArrayList<String>();
//		tarList.add("3D打印");
//		tarList.add("大数据");
//		tarList.add("生物发电");
//		tarList.add("互联网");
//		tarList.add("机器人");
//		for (String string : tarList) {
//			System.out.println(string);
//			crawler(string);
//			Thread.sleep(1000);
//		}
//		for (int i = 0; i < 50; i++) {
//			String url = "http://epub.cnki.net/kns/brief/brief.aspx?curpage=15&RecordsPerPage=20&QueryID=19&ID=&turnpage=1&tpagemode=L&dbPrefix=SCDB&Fields=&DisplayMode=listmode&PageName=ASP.brief_default_result_aspx#J_ORDER";
//			String body = ReadHttpFileUtil.readPageSourceCommon(url, "utf-8");
//			System.out.println(getUrl(body).size());
//			Thread.sleep(1000);
//		}
//		getDetail("http://www.cnki.net/KCMS/detail/detail.aspx?QueryID=1&CurRec=45&recid=&FileName=2007101979.nh&DbName=CMFD2007&DbCode=CMFD&pr=");
//		crawler("无人机");
//		fw.close();
	}
	
	private static Set<String> getKeyword() throws Exception{
		Set<String> set = new HashSet<String>();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/kw.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			String[] split = input.split("\t");
			String kw = split[1];
			set.add(kw);
		}
//		set.add("电喷");
		br.close();
		return set;
	}
	/**
	 * @param keyWord
	 * @throws Exception
	 */
	public static void crawler(SearchBean bean,String searchWord) throws Exception{
		HttpReader reader = new StaticHttpReader();
//		FileWriter fw = new FileWriter("data/cnki/result/"+searchWord+ "_"+bean.getName()+".txt");
		ArrayList<ContentBean> detailUrlList = new ArrayList<ContentBean>();
		String key = URLEncoder.encode("柴油机","utf-8");
		String key2 = URLEncoder.encode(searchWord,"utf-8");
		Calendar now = Calendar.getInstance();
		String year = now.get(Calendar.YEAR) + "";
		String month = "Jan";
		String day = now.get(Calendar.DAY_OF_MONTH) + "";
		String hou = now.get(Calendar.HOUR) + "";
		String min = now.get(Calendar.MINUTE) + "";
		String sec = now.get(Calendar.SECOND) + "";
		String dbPrefix = bean.getDbPrefix();
		String dbCatalog = bean.getDbCatalog();
		String configFile = bean.getConfigFile();
		String dbOpt = bean.getDbOpt();
		String dbValue = bean.getDbValue();
		String searchPrefix = bean.getSearchPrefix();
		try {
			String requestUrl = "";
			if (dbPrefix.equals("CISD")) {
				System.out.println("std");
				requestUrl = reader.readSource("http://kns.cnki.net/kns/request/SearchHandler.ashx?action=undefined&NaviCode=*&PageName=ASP.brief_result_aspx&DbPrefix="+dbPrefix
						+"&DbCatalog="+dbCatalog+"&ConfigFile="+configFile+"&db_opt="+dbOpt+"&txt_1_sel="
						+searchPrefix+"&txt_1_value1="+key+"&txt_1_relation=%23CNKI_AND&txt_1_special1=%3D&txt_2_sel="+searchPrefix+
						"&txt_2_value1="+key2+"&txt_2_logical=and&txt_2_relation=%23CNKI_AND&txt_2_special1=%3D&his=0&__=Fri%20"
						+month+"%20"+day+"%20"+year+"%20"+hou+"%3A"+min+"%3A"+sec+"%20GMT%2B0800%20(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)","utf-8");

			} else {
				requestUrl = reader.readSource("http://kns.cnki.net/kns/request/SearchHandler.ashx?action=undefined&NaviCode=*&PageName=ASP.brief_result_aspx&DbPrefix="+dbPrefix
						+"&DbCatalog="+dbCatalog+"&ConfigFile="+configFile+"&db_opt="+dbOpt+"&db_value="+dbValue+"&txt_1_sel="
						+searchPrefix+"&txt_1_value1="+key+"&txt_1_relation=%23CNKI_AND&txt_1_special1=%3D&txt_2_sel="+searchPrefix+
						"&txt_2_value1="+key2+"&txt_2_logical=and&txt_2_relation=%23CNKI_AND&txt_2_special1=%3D&his=0&__=Fri%20"
						+month+"%20"+day+"%20"+year+"%20"+hou+"%3A"+min+"%3A"+sec+"%20GMT%2B0800%20(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)","utf-8");
				
			}
			int i = 0;
			while (true) {
				i++;
				//反爬虫设置
				if(total++%14==0){
		    		System.out.println("thread sleep");
		    		Thread.sleep(60000);
		    	}
				String finalUrl = "";
				if (i!=1) {
					finalUrl = "http://kns.cnki.net/kns/brief/brief.aspx?curpage="+i+"&RecordsPerPage=50&QueryID=1&ID=&turnpage=1&tpagemode=L&dbPrefix="+dbPrefix+"&Fields=&DisplayMode=listmode&PageName=ASP.brief_result_aspx#J_ORDER&";
				} else {
					finalUrl = "http://kns.cnki.net/kns/brief/brief.aspx?pagename=ASP.brief_result_aspx&dbPrefix="+bean.getDbPrefix();
				}
				String content = reader.readSource(finalUrl,"utf-8");
				System.out.println("第"+i+"页");
				List<ContentBean> detailList = getDetailUrlList(content,bean.getName());
				if(detailList.isEmpty()){
					break;
				}
				detailUrlList.addAll(detailList);
				System.out.println(detailList.size());
			}
		    System.out.println("size "+detailUrlList.size());
		    for (ContentBean detail : detailUrlList) {
//				String info = JSON.toJSONString(detail);
//				String info = getDetail(detail);
//				fw.write(info+"\r\n");
//				fw.flush();
		    	Document doc = beanTDoc(detail);
		    	doc.append("type", bean.getName()).append("keyword", searchWord);
		    	db.getCollection("cnki").insertOne(doc);
		    	System.out.println("insert success");
			}
//		    fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		reader.close();
	}
	
	private static Document beanTDoc(ContentBean bean) {
		Document doc = new Document();
		doc.append("url", bean.getUrl());
		doc.append("publishTime", bean.getPublishTime());
		doc.append("title", bean.getTitle());
		doc.append("from", bean.getSource());
		doc.append("ins", bean.getInstitute());
		HttpReader reader = new StaticHttpReader();
		String source = reader.readSource(bean.getUrl().replace(" ", "%20"));
		doc.append("source", source);
		reader.close();
		return doc;
	}
	
	/**
	 * 获得论文的详细信息
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	public static String getDetail(ContentBean bean) throws Exception{
		HttpReader reader = new StaticHttpReader();
		String body = "";
		String author = "";
		String institute = "";
		ArrayList<String> keywordsList = new ArrayList<String>();
		try {
			for (int i = 0; i < 5; i++) {
				body = reader.readSource(bean.getUrl(),"utf-8");
				Thread.sleep(500);
				if(!"再来一次".equals(body)){
					break;
				}
			}
			org.jsoup.nodes.Document doc = Jsoup.parse(body);
			String title = doc.getElementById("chTitle").text();
			bean.setTitle(title);
			Elements keyE = doc.getElementById("ChDivKeyWord").getElementsByTag("a");
			for (Element element : keyE) {
//				System.out.println(element.text());
				keywordsList.add(element.text());
			}
			bean.setKeywords(keywordsList);
			try {
				Elements authorEle = doc.select("div > p");
				for (Element element : authorEle) {
					if (element.text().contains("作者")) {
						author = element.text();
						break;
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			try {
				Elements instituteEle = doc.select("div > p");
				for (Element element : instituteEle) {
					if (element.text().contains("机构")) {
						institute = element.text();
						break;
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
			String abstracts = doc.getElementById("ChDivSummary").text();
			bean.setAbstracts(abstracts);
			bean.setAuthor(author);
			bean.setInstitute(institute);
			String info = JSON.toJSONString(bean);
//			System.out.println(info);
			reader.close();
			return info;
		} catch (Exception e) {
			// TODO: handle exception
			String info = JSON.toJSONString(bean);
			reader.close();
			return info;
		}
	}
	//获得能够控制页数的url
	public static String getControlUrl(String body) {
		String url = "";
		String prefix = "http://epub.cnki.net/kns/brief/brief.aspx";
		try {
			org.jsoup.nodes.Document doc = Jsoup.parse(body);
			Elements nextPageUrl = doc.select("a#Page_next");
			for (Element element : nextPageUrl) {
				url = prefix + element.getElementsByTag("a").attr("href");
			}
//			System.out.println(url);
			url = url.replace("RecordsPerPage=20", "RecordsPerPage=50");
			return url;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return "";
	}
	//获得页面中的urlList
	public static ArrayList<ContentBean> getDetailUrlList(String body,String name) throws UnsupportedEncodingException{
		ArrayList<ContentBean> urlList = new ArrayList<ContentBean>();
		String url = "";
		String result="";
		int i = 0;
		org.jsoup.nodes.Document doc = Jsoup.parse(body);
		Elements detail = doc.select("table.GridTableContent > tbody > tr");
		for (Element element : detail) {
//			System.out.println(element.getElementsByTag("a").attr("href"));
			ContentBean bean = new ContentBean();
			String candiUrl = "";
			try {
				candiUrl = element.select("a.fz14").first().attr("href");
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (candiUrl.equals("")) continue;
			String publishTime = "";
			try {
				if (name.equals("doc")) {
					publishTime = element.select("td").get(5).text();
				} else if (name.equals("paper")) {
					publishTime = element.select("td").get(4).text();
				} else if (name.equals("standard")) {
					publishTime = element.select("td").get(3).text();
				}
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			String title = "";
			try {
				title = element.select("td").get(1).text();
			} catch (Exception e) {
				// TODO: handle exception
			}
			String ins = "";
			try {
				ins = element.select("td").get(3).text();
			} catch (Exception e) {
				// TODO: handle exception
			}
			if (name.equals("standard") || name.equals("doc")) {
				String source = "";
				try {
					source = element.select("td").get(4).text();
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (!source.equals("")) {
					bean.setSource(source);
				}
			}
//			System.out.println(i);
//			i++;
//			url = "http://www.cnki.net/KCMS/detail/"+filter(candiUrl);
			if (!name.equals("standard")) {
				url = "http://kns.cnki.net/KCMS/detail/"+filter(candiUrl);
			} else {
				url = "http://dbpub.cnki.net/grid2008/dbpub/detail.aspx?" + standardFilter(candiUrl);
			}
//			System.out.println(url);
			bean.setUrl(url);
			if (!title.equals("")) {
				bean.setTitle(title);
			}
			if (!ins.equals("")) {
				bean.setInstitute(ins);
			}
			if (!publishTime.equals("")) {
				bean.setPublishTime(publishTime);
			}
//			if (!source.equals("")) {
//				bean.setSource(source);
//			}
			urlList.add(bean);
		}
		return urlList;
	}
	//http请求
	public static String getUrlContent(String url) throws Exception{
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(url);
		get.setHeader("cookie", "ASP.NET_SessionId=da1z14jzujhslea5ykg0ss45; RsPerPage=50; LID=WEEvREcwSlJHSldTTGJhYlJRMVozNGRrUkFXOHVDc0xFc3Vncm4xZTV3ZGlwUS8zOGRsL2hDQTI3b0hVUzVvM0Nrdz0=$9A4hF_YAuvQ5obgVAqNKPCYcEjKensW4IQMovwHtwkF4VYPoHbKxJw!!");
		HttpResponse response = client.execute(get);
		HttpEntity entity = response.getEntity();
		String body = EntityUtils.toString(entity);
		return body;
	}
	
	public static BufferedReader getBuffer(String file) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		return br;
	}
	
	public static String filter(String url){
		String filterUrl="";
		String[] split = url.split("/");
		filterUrl = split[split.length-1];
		return filterUrl;
	}
	
	@Test
	public void t1() {
		String t = "/kns/detail/detail.aspx?QueryID=0&CurRec=11&dbcode=SOSD&dbname=SOSD&filename=SOSD000005816893";
		System.out.println(standardFilter(t));
	}
	
	public static String standardFilter(String url) {
		String filterUrl = "";
		String regex = "(?=dbcode).*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(url);
		while (m.find()) {
			filterUrl = m.group();
		}
		return filterUrl;
	}
	
	
	public static ArrayList<String> controlFilter(String url) {
		ArrayList<String> controlList = new ArrayList<String>();
		String prefix = "";
		String suffix = "";
		String reg = "";
		String[] urla = url.split("curpage=[\\d]*");
		prefix = urla[0]+"curpage=";
		suffix = urla[1];
		controlList.add(prefix);
		controlList.add(suffix);
		return controlList;
	}
}
