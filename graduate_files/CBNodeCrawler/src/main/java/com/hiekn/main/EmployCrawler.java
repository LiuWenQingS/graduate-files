package com.hiekn.main;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

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
import org.jsoup.nodes.Element;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;
import com.hiekn.util.HttpReader;
import com.hiekn.util.IReadHttpFileUtil;
import com.hiekn.util.ReadHttpFileUtil;
import com.hiekn.util.StaticHttpReader;

public class EmployCrawler {
	public static void main(String[] args) {
		try {
			search();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void search() throws Exception {
		int count = 0;
		FileWriter fw = new FileWriter("data/com_url.txt",true);
		FileWriter ew = new FileWriter("data/com_url_error.txt",true);
		BufferedReader br = BufferedReaderUtil.getBuffer("data/company_all.txt");
		IReadHttpFileUtil reader = new ReadHttpFileUtil();
		String input = "";
		while ((input = br.readLine())!=null) {
			System.out.println(count++);
			if(count<149) continue;
			JSONObject obj = new JSONObject();
			String company = input;
			String companyUrl = "";
			String searchUrl = "http://www.lagou.com/jobs/list_"+company;
			try {
				String source = reader.readPageSourceCommon(searchUrl, "utf-8");
				Document doc = Jsoup.parse(source);
				Element element = doc.select("a#tab_comp").first();
				String str = element.text();
				if (str.equals("公司 ( 1 )"))	{
					String findUrl = "";
					try {
						findUrl = doc.select("div.cl_r_top > h2 > a").first().attr("href");
						String comSource = reader.readPageSourceCommon(findUrl, "utf-8");
						Document comDoc = Jsoup.parse(comSource);
						String title = comDoc.head().select("title").text();
						if (title.contains(company)) {
							System.out.println(company);
							obj.put("name", company);
							companyUrl = findUrl;
							obj.put("url", companyUrl);
							fw.write(obj.toJSONString()+"\r\n");
							fw.flush();
						}
					} catch (Exception e) {
						// TODO: handle exception
						ew.write(company+"\r\n");
						ew.flush();
					}
				}
			} catch (Exception e) {
				// TODO: handle exception
				ew.write(company+"\r\n");
				ew.flush();
			}
			
		}
		ew.close();
		br.close();
		fw.close();
	}
	
	
	@Test
	public void post() throws Exception {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost post = new HttpPost("http://www.lagou.com/gongsi/searchPosition.json");
		post.setHeader("Cookie", "user_trace_token=20160120113308-861c3e15-bf26-11e5-8bf5-5254005c3644; LGUID=20160120113308-861c43a1-bf26-11e5-8bf5-5254005c3644; tencentSig=9335248896; LGMOID=20160612143824-B1951EB3DB5AE2D9D40EB7953E132AF7; HISTORY_POSITION=1981804%2C8k-15k%2C%E5%A8%83%E5%A8%83%E4%BA%B2%E4%BA%B2%2C%E4%BA%92%E8%81%94%E7%BD%91%E4%BA%A7%E5%93%81%E7%BB%8F%E7%90%86.%7C1908162%2C15k-30k%2C%E7%8E%BB%E6%A3%AE%E6%95%B0%E6%8D%AE%2C%E8%87%AA%E7%84%B6%E8%AF%AD%E8%A8%80%E5%A4%84%E7%90%86%7C1908135%2C9k-15k%2C%E7%8E%BB%E6%A3%AE%E6%95%B0%E6%8D%AE%2C%E6%95%B0%E6%8D%AE%E6%8C%96%E6%8E%98%E5%B7%A5%E7%A8%8B%E5%B8%88%7C1948887%2C4k-8k%2C%E6%B3%9B%E5%BE%AE%2C%E5%AE%A2%E6%88%B7%E7%BB%8F%E7%90%86%7C; JSESSIONID=BE43C463D0EC5844E7F7E79563967611; login=true; unick=%E6%9D%8E%E6%98%A5%E6%99%93; showExpriedIndex=1; showExpriedCompanyHome=1; showExpriedMyPublish=1; hasDeliver=13; SEARCH_ID=9a9e71ac1f874047a11132e8e315c3ee; index_location_city=%E4%B8%8A%E6%B5%B7; _ga=GA1.2.1393556212.1453260790; LGSID=20160621164734-cc494174-378c-11e6-88b0-525400f775ce; LGRID=20160621171327-6a10a990-3790-11e6-a3ea-5254005c3644; Hm_lvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1466043685,1466044383,1466141059,1466144687; Hm_lpvt_4233e74dff0ae5bd0a3d81c6ccf756e6=1466501605");
		post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
		post.setHeader("Referer", "http://www.lagou.com/gongsi/j43633.html");
		post.setHeader("X-Anit-Forge-Code", "0");
		post.setHeader("X-Anit-Forge-Token", "None");
		post.setHeader("X-Requested-With", "XMLHttpRequest");
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("companyId", "43633"));
		params.add(new BasicNameValuePair("positionFirstType", "全部"));
		params.add(new BasicNameValuePair("pageNo", "1"));
		params.add(new BasicNameValuePair("pageSize", "10"));
		HttpEntity postParams = new UrlEncodedFormEntity(params);
		post.setEntity(postParams);
		CloseableHttpResponse httpResponse = httpClient.execute(post);
		System.out.println("POST Response Status:: " + httpResponse.getStatusLine().getStatusCode());
		System.out.println(EntityUtils.toString(httpResponse.getEntity(), "UTF-8"));
	}

	
	public static String getSource(String url) {
		String source = "";
		HttpReader reader = new StaticHttpReader();
		source = reader.readSource(url,"utf-8");
		reader.close();
		return source;
	}
}
