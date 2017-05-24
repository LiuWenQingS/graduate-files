package com.hiekn.other;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.hiekn.util.BufferedReaderUtil;
import com.hiekn.util.HttpReader;
import com.hiekn.util.StaticHttpReader;

public class AirCrawler {
	public static void main(String[] args) {
		try {
//			process();
//			getJiAirDis();
//			add();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
	
	public static void process() throws Exception {
		Map<String,String> map = getMap();
		FileWriter fw = new FileWriter("data/other/air_dis.txt");
		BufferedReader br = BufferedReaderUtil.getBuffer("data/other/list.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			String[] split = input.split("\t");
			if (split.length < 2) {
				String id = split[0].trim();
				if (map.containsKey(id)) {
					fw.write(id + "\t" + map.get(id) + "\r\n");
					fw.flush();
				} else {
					fw.write(id + "\t" + "\r\n");
					fw.flush();
				}
			} else {
				fw.write(input + "\r\n");
				fw.flush();
			}
		}
		fw.close();
		br.close();
	}
	
	public static void getJiAirDis() throws Exception {
		int count = 0;
		HttpReader reader = new StaticHttpReader();
		FileWriter fw = new FileWriter("data/other/air_dis_new_ji.txt",true);
		String input = "";
		BufferedReader br = BufferedReaderUtil.getBuffer("data/other/ds.txt");
		while ((input = br.readLine())!=null) {
			String[] split = input.split("\t");
			System.out.println(count++);
			if (split.length < 2) {
				String id = split[0].trim();
				String url = "http://jipiao.pailv.com/flightno/" + id + ".html";
				String source = reader.readSource(url);
				Document doc = Jsoup.parse(source);
				String dis = "";
				try {
					dis = doc.select("table.flight_det > tbody > tr").first().select("td").get(3)
							.select("p").get(1).text().replace("公里", "");
				} catch (Exception e) {
					// TODO: handle exception
				}
				fw.write(id + "\t" + dis + "\r\n");
				fw.flush();
			}
		}
		br.close();
		fw.close();
		reader.close();
	}
	
	public static void add() throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		FileWriter fw = new FileWriter("data/other/add.txt");
		BufferedReader br = BufferedReaderUtil.getBuffer("data/other/ds.txt");
		String input = "";
		while((input = br.readLine())!=null) {
			String[] split = input.split("\t");
			if (split.length > 2) {
				map.put(split[0]+split[1], split[2]);
				map.put(split[1]+split[0], split[2]);
			}
		}
		BufferedReader br1 = BufferedReaderUtil.getBuffer("data/other/ds.txt");
		while ((input = br1.readLine())!=null) {
			String[] split = input.split("\t");
			String key = split[0]+split[1];
			if (split.length > 2) {
				fw.write(input + "\r\n");
				fw.flush();
			} else {
				if (map.containsKey(key)) {
					fw.write(input + map.get(key) + "\r\n");
					fw.flush();
				} else {
					fw.write(input + "\r\n");
					fw.flush();
				}
			}
		}
		fw.close();
		br.close();
		br1.close();
	}
	
	
	public static Map<String,String> getMap() throws Exception {
		Map<String,String> map = new HashMap<String,String>();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/other/air_dis_new.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			String[] split = input.split("\t");
			if (split.length > 1) {
				map.put(split[0], split[1]);
			}
		}
		br.close();
		return map;
	}
	
	public static void getAliAirDis() throws Exception {
		String input = "";
		FileWriter fw = new FileWriter("data/other/air_dis_new_ali.txt",true);
		BufferedReader br = BufferedReaderUtil.getBuffer("data/other/ali.txt");
		HttpReader reader = new StaticHttpReader();
		int count = 0;
		while ((input = br.readLine())!=null) {
			System.out.println(count++);
			String url = "https://www.alitrip.com/jipiao/status/" + input;
			String source = reader.readSource(url);
			Document doc = Jsoup.parse(source);
			String dis = "";
			try {
				dis = doc.select("div.flight-info-bd > div.flight-introduction-item").get(3).select("span.value").text();
			} catch (Exception e) {
				// TODO: handle exception
			}
			fw.write(input + "\t" + dis + "\r\n");
			fw.flush();
		}
		reader.close();
		br.close();
		fw.close();
	}
	
	public static void getAirDis() throws Exception {
		int count = 0;
		HttpReader reader = new StaticHttpReader();
		Set<String> idSet = getIdSet();
		FileWriter fw = new FileWriter("data/other/air_dis_new.txt",true);
		for (String id : idSet) {
			System.out.println(count++);
			if (count < 2239) continue;
			String url = "http://flight.qunar.com/status/fquery.jsp?flightCode=" + id;
			String source = reader.readSource(url);
			Document doc = Jsoup.parse(source);
			String dis = "";
			try {
				dis = doc.select("dl.state_detail > dd").get(1).select("span.sd_5").text().replace("飞行距离：", "");
			} catch (Exception e) {
				// TODO: handle exception
			}
			fw.write(id + "\t" + dis + "\r\n");
			fw.flush();
		}
		reader.close();
		fw.close();
	}
	
	public static Set<String> getIdSet() throws Exception{
		Set<String> idSet = new HashSet<String>();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/other/air.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			idSet.add(input.trim());
		}
		System.out.println(idSet.size());
		br.close(); 
		return idSet;
	}
	
	
}
