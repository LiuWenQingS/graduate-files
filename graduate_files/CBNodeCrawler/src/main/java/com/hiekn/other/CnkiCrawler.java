package com.hiekn.other;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.hiekn.bean.ContentBean;
import com.hiekn.util.HttpReader;
import com.hiekn.util.ReadHttpFileUtil;
import com.hiekn.util.StaticHttpReader;

public class CnkiCrawler {
	public static int totalCount = 1;
	public static void main(String[] args) {
		try {
			crawler("北京理工大学");
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
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
	
	/**
	 * @param keyWord
	 * @throws Exception
	 */
	public static void crawler(String keyWord) throws Exception{
		HttpReader reader = new StaticHttpReader();
		String url = "";
		String pageContent = "";
		FileWriter fw = new FileWriter("data/other/"+keyWord+"_61-end.txt");
		ArrayList<String> controlUrl = new ArrayList<String>();
		ArrayList<ContentBean> detailUrlList = new ArrayList<ContentBean>();
		String key = URLEncoder.encode(keyWord,"utf-8");
		Calendar now = Calendar.getInstance();
		String year = now.get(Calendar.YEAR) + "";
		String month = "Dec";
		String day = now.get(Calendar.DAY_OF_MONTH) + "";
		String hou = now.get(Calendar.HOUR) + "";
		String min = now.get(Calendar.MINUTE) + "";
		String sec = now.get(Calendar.SECOND) + "";
//		System.out.println(key);
		try {
//			String requestUrl = ReadHttpFileUtil.readPageSourceCommon("http://epub.cnki.net/kns/request/SearchHandler.ashx?action=&NaviCode=*&ua=1.11&PageName=ASP.brief_default_result_aspx&DbPrefix=SCDB&DbCatalog=%e4%b8%ad%e5%9b%bd%e5%ad%a6%e6%9c%af%e6%96%87%e7%8c%ae%e7%bd%91%e7%bb%9c%e5%87%ba%e7%89%88%e6%80%bb%e5%ba%93&ConfigFile=SCDBINDEX.xml&db_opt=CJFQ%2CCJFN%2CCDFD%2CCMFD%2CCPFD%2CIPFD%2CCCND&txt_1_sel=AF%24%25&txt_1_value1="+key+"&txt_1_special1=%25&his=0&parentdb=SCDB&__=Fri%20"+month+"%20"+day+"%20"+year+"%20"+hou+"%3A"+min+"%3A"+sec+"%20GMT%2B0800%20(%E4%B8%AD%E5%9B%BD%E6%A0%87%E5%87%86%E6%97%B6%E9%97%B4)","utf-8");
//			String finalUrl = "http://epub.cnki.net/kns/brief/brief.aspx?pagename="+requestUrl;
//			System.out.println(finalUrl);
//			String content = "";
//			for (int i = 0; i < 5; i++) {
//				System.out.println(i);
//				content = ReadHttpFileUtil.readPageSourceCommon(finalUrl,"utf-8");
//				Thread.sleep(100);
//				if(!"再来一次".equals(content)){
//					break;
//				}
//				if(content.isEmpty()){
//					System.out.println("continue");
//					continue;
//				}
//			}
//			System.out.println(finalUrl);
//			System.out.println(content);
		    String urlCandidate = "http://epub.cnki.net/kns/brief/brief.aspx?curpage=1&RecordsPerPage=50&QueryID=22&ID=&turnpage=1&tpagemode=L&dbPrefix=SCDB&Fields=&DisplayMode=listmode&PageName=ASP.brief_default_result_aspx&ctl=20590bc7-b354-4efc-bf65-362be22a5895&Param=%E5%B9%B4+%3d+%272016%27#J_ORDER";
		    if (urlCandidate.isEmpty()) {
//		    	detailUrlList.addAll(getDetailUrlList(content));
		    } else {
		    	controlUrl = controlFilter(urlCandidate);
			    for (int i = 1; i < 2; i++) {
			    	//反爬虫设置
			    	if(totalCount++%14==0){
			    		System.out.println("thread sleep");
			    		Thread.sleep(60000);
			    	}
			    	System.out.println("第"+i+"页");
					url = controlUrl.get(0)+i+controlUrl.get(1);
					System.out.println(url);
					for (int j = 0; j < 5; j++) {
						pageContent = reader.readSource(url,"utf-8");
						if(!"再来一次".equals(pageContent)){
							break;
						}
					}
//					System.out.println(url);
					List<ContentBean> detailList = getDetailUrlList(pageContent);
					if(detailList.isEmpty()){
						break;
					}
					detailUrlList.addAll(detailList);
					System.out.println(detailList.size());
//					Thread.sleep(5000);
			    }
		    }
		    System.out.println("size "+detailUrlList.size());
		    for (ContentBean detail : detailUrlList) {
				String info = getDetail(detail);
				System.out.println(info);
				fw.write(info+"\r\n");
				fw.flush();
			}
		    fw.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		reader.close();
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
			Document doc = Jsoup.parse(body);
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
			Document doc = Jsoup.parse(body);
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
	public static ArrayList<ContentBean> getDetailUrlList(String body){
		ArrayList<ContentBean> urlList = new ArrayList<ContentBean>();
		String url = "";
		String result="";
		int i = 0;
		Document doc = Jsoup.parse(body);
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
				publishTime = element.select("td").get(4).text();
			} catch (Exception e) {
				// TODO: handle exception
			}
			String source = "";
			try {
				source = element.select("td").get(5).text();
			} catch (Exception e) {
				// TODO: handle exception
			}
//			System.out.println(i);
//			i++;
			url = "http://www.cnki.net/KCMS/detail/"+filter(candiUrl);
//			System.out.println(url);
			bean.setUrl(url);
			if (!publishTime.equals("")) {
				bean.setPublishTime(publishTime);
			}
			if (!source.equals("")) {
				bean.setSource(source);
			}
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
