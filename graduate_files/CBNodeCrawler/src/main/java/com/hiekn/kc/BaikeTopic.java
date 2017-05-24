package com.hiekn.kc;

import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.HttpReader;
import com.hiekn.util.StaticHttpReader;

public class BaikeTopic {
	public static void main(String[] args) {
		try {
			List<Integer> idList = getId();
			for (Integer id : idList) {
				System.out.println(id + " start");
				process(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void process(int catId) throws Exception {
		int page = 0;
		
		try {
			FileWriter fw = new FileWriter("data/zhihu/baike_"+getDate()+".txt",true);
			CloseableHttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = null;
			HttpEntity entity = null;
			HttpPost post = null;
			
			try {
				while (true) {
					System.out.println(catId  + " " + page);
					String body = "";
					for (int i = 0; i < 5; i++) {
						String url = "http://baike.baidu.com/wikitag/api/getlemmas";
						post = new HttpPost(url);
						List<NameValuePair> nvps = new ArrayList<NameValuePair>();  
						nvps.add(new BasicNameValuePair("limit", "24"));  
						nvps.add(new BasicNameValuePair("timeout", "3000"));  
						nvps.add(new BasicNameValuePair("tagId", catId+""));  
						nvps.add(new BasicNameValuePair("fromLemma", "false"));  
						nvps.add(new BasicNameValuePair("contentLength", "40"));  
						nvps.add(new BasicNameValuePair("page", page+""));  
						post.setEntity(new UrlEncodedFormEntity(nvps)); 
						post.addHeader("Cookie", "BDUSS=9FdGhMSERIREZacTFOTmZjU29IbG92dExmN2ctV0hGVFRVR1NiUWZWRmtidFpYQVFBQUFBJCQAAAAAAAAAAAEAAACmml0G0uNBzOXT~QAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAGThrldk4a5XS2; BAIDUID=CF0CFF183DA3A291D996A518E56C5490:FG=1; BIDUPSID=CF0CFF183DA3A291D996A518E56C5490; PSTM=1472696091; BDRCVFR[feWj1Vr5u3D]=I67x6TjHwwYf0; pgv_pvi=2914264064; pgv_si=s3622172672; H_PS_PSSID=1457_17944_18133_11967_20857_20733_20836_20928");
						post.addHeader("Accept", "application/json, text/javascript, */*; q=0.01");
						post.addHeader("Accept-Encoding", "gzip, deflate");
						post.addHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6,zh-TW;q=0.4");
						post.addHeader("Connection", "keep-alive");
						post.addHeader("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
						post.addHeader("Host","baike.baidu.com");
						post.addHeader("Origin","http://baike.baidu.com");
						post.addHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
						response = client.execute(post);
						entity = response.getEntity();
						if (entity!=null) {
							body = EntityUtils.toString(entity);
//							TimeUnit.SECONDS.sleep(3);
							break;
						}
					}
					JSONObject parseObj = JSONObject.parseObject(body);
					JSONArray resultArr = parseObj.getJSONArray("lemmaList");
					if (resultArr.size() == 0) {
						break;
					} else {
						for (Object object : resultArr) {
							JSONObject arrObj = (JSONObject) object;
							String topic = arrObj.getString("lemmaTitle");
							int topicId = arrObj.getIntValue("lemmaId");
							String topidUrl = arrObj.getString("lemmaUrl");
							String subDes = arrObj.getString("lemmaDesc");
							String des = getDes(topidUrl);
							JSONObject resultObj = new JSONObject();
							resultObj.put("topic", topic);
							resultObj.put("topicId", topicId);
							resultObj.put("parentId", catId);
							if (!des.equals("")) {
								resultObj.put("des", des);
							} else {
								resultObj.put("des", subDes);
							}
							fw.write(resultObj.toJSONString() + "\r\n");
							fw.flush();
						}
					}
					page++;
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
	
	public static String getDes(String url) {
		HttpReader reader = new StaticHttpReader();
		String source = reader.readSource(url);
		Document doc = Jsoup.parse(source);
		String des = "";
		try {
			des = doc.select("div.lemma-summary").text();
		} catch (Exception e) {
			// TODO: handle exception
		}
		reader.close();
		return des;
	}
	
	/**
	 * 获得百度词条分类主要门类
	 * @return
	 * @throws Exception
	 */
	public static List<Integer> getId() throws Exception {
		FileWriter fw = new FileWriter("data/zhihu/baike_"+getDate()+".txt",true);
		List<Integer> idList = new ArrayList<Integer>();
		HttpReader reader = new StaticHttpReader();
		String source = reader.readSource("http://baike.baidu.com/science");
		Document doc = Jsoup.parse(source);
		Elements idElements = doc.select("ul.cmn-clearfix > li > h4 > a");
		for (Element element : idElements) {
			String topic = element.text();
			if (topic.equals("健康医疗")) {
				String medSource = reader.readSource("http://baike.baidu.com/science/medical");
				Document medDoc = Jsoup.parse(medSource);
				Elements medEle = medDoc.select("ul.knowledge-list.cmn-clearfix > li");
				for (Element element2 : medEle) {
					String medTopic = element2.select("span").text();
					String idStr = element2.select("a").attr("href").replace("http://baike.baidu.com/wikitag/taglist?tagId=", "");
					idList.add(Integer.parseInt(idStr));
					JSONObject resultObj = new JSONObject();
					resultObj.put("topic", medTopic);
					resultObj.put("topicId", Integer.parseInt(idStr));
					fw.write(resultObj.toJSONString() + "\r\n");
					fw.flush();
				}
			} else {
				String idStr = element.attr("href").replace("http://baike.baidu.com/wikitag/taglist?tagId=", "");
				idList.add(Integer.parseInt(idStr));
				JSONObject resultObj = new JSONObject();
				resultObj.put("topic", topic);
				resultObj.put("topicId", Integer.parseInt(idStr));
				fw.write(resultObj.toJSONString() + "\r\n");
				fw.flush();
			}
		}
		reader.close();
		fw.close();
		return idList;
	}
	
	public static String getDate() {
		String date = "";
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
		date = sdf.format(now);
		return date;
	}
}
