package com.hiekn.other;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;
import com.hiekn.util.ConstResource;
import com.hiekn.util.HttpReader;
import com.hiekn.util.StaticHttpReader;

public class ZXCrawler {
	static String originUrl = "http://www.cninfo.com.cn/information/brief/";
	static HttpReader reader = new StaticHttpReader();
	static Map<String,String> nameMap = new HashMap<String,String>();
	public static void main(String[] args) {
		try {
//			init();
//			System.out.println(getId());
//			importProcess();
//			patch();
			getAllStock();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void getAllStock() throws Exception {
		FileWriter fw = new FileWriter("data/stock/stock_list.txt");
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("data/stock/stock_html.txt")));
		String input = "";
		while ((input = br.readLine())!=null) {
			String s = Jsoup.parse(input).text().split("\\(")[0];
			fw.write(s + "\r\n");
			fw.flush();
			System.out.println(s);
		}
		br.close();
		fw.close();
	}
	
	public static void patch() throws Exception {
		Connection conn = getConnection();
		String insert = "insert into entity_id (name,meaning) values (?,?)";
		PreparedStatement pstat = conn.prepareStatement(insert);
		BufferedReader br = BufferedReaderUtil.getBuffer("data/stock/a.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			String[] split = input.split("\t");
			long comId = Long.valueOf(split[0]);
			String s = split[6].trim();
			String comName = s.split("_-_")[0].trim();
			pstat.setString(1, comName);
			pstat.setLong(2, comId);
			pstat.addBatch();
			String simple = split[2].trim();
			pstat.setString(1, simple);
			pstat.setLong(2, comId);
			pstat.addBatch();
		}
		pstat.executeBatch();
		br.close();
		conn.close();
	}
	
	private static void init() {
		nameMap.put("61", "英文全称");
		nameMap.put("68", "注册资本");
		nameMap.put("69", "注册地址");
		nameMap.put("71", "联系方式");
		nameMap.put("72", "传真");
		nameMap.put("90", "公司网址");
		nameMap.put("91", "上市时间");
		nameMap.put("92", "招股时间");
		nameMap.put("93", "发行数量");
		nameMap.put("94", "发行价格");
		nameMap.put("95", "发行市盈率");
		nameMap.put("96", "发行方式");
	}
	
	

	public static void importProcess() throws Exception {
		Connection conn = getConnection();
		PreparedStatement pstat = null;
		long id = getId();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/stock/com_info_concept.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			JSONObject obj = JSONObject.parseObject(input);
			String comName = obj.getString("simpleName");
			String synonym = obj.getString("name") + "_-_" + obj.getString("code");
			long comId = ++id;
			String concept = obj.getString("concept");
			System.out.println(comId);
			String insert = "insert into basic_info (id,type,name,synonym,parent) values (?,?,?,?,?)";
			pstat = conn.prepareStatement(insert);
			pstat.setLong(1, comId);
			pstat.setString(2,"1");
			pstat.setString(3,comName);
			pstat.setString(4,synonym);
			pstat.setString(5,concept);
			pstat.executeUpdate();
			insert = "insert into entity_id (name,meaning) values (?,?)";
			pstat = conn.prepareStatement(insert);
			pstat.setString(1, obj.getString("code"));
			pstat.setLong(2, comId);
			pstat.executeUpdate();
			insert = "insert into instance (entity_id,ins_id) values (?,?)";
			pstat = conn.prepareStatement(insert);
			pstat.setLong(1, Long.valueOf(concept));
			pstat.setLong(2,comId);
			pstat.executeUpdate();
			obj.remove("name");
			obj.remove("simpleName");
			obj.remove("code");
			obj.remove("concept");
			obj.remove("63");
			obj.remove("66");
			obj.remove("97");
			obj.remove("98");
			obj.remove("99");
			insert = "insert into attribute (entity_id,attr_id,attr_name,attr_type,attr_value) values (?,?,?,?,?)";
			pstat = conn.prepareStatement(insert);
			for (String key : obj.keySet()) {
				String value = obj.getString(key);
				if (!value.equals("") && !(key.equals("68") || key.equals("93")|| key.equals("94")|| key.equals("95"))) {
					pstat.setLong(1, comId);
					pstat.setInt(2, Integer.valueOf(key));
					if (nameMap.get(key) == null) {
						System.out.println(key);
					} else {
						pstat.setString(3, nameMap.get(key));
					}
					pstat.setString(4, "0");
					pstat.setString(5, value);
					pstat.addBatch();
				}
			}
			pstat.executeBatch();
			String dataInsert = "insert into attribute (entity_id,attr_id,attr_name,attr_type,attr_value,data_value) values (?,?,?,?,?,?)";
			PreparedStatement dstat = conn.prepareStatement(dataInsert);
			for (String key : obj.keySet()) {
				String value = obj.getString(key);
				if (!value.equals("") && (key.equals("68") || key.equals("93")|| key.equals("94")|| key.equals("95"))) {
					dstat.setLong(1, comId);
					dstat.setInt(2, Integer.valueOf(key));
					dstat.setString(3, nameMap.get(key));
					dstat.setString(4, "0");
					dstat.setString(5, value);
					double d = 0;
					try {
						d = Double.valueOf(value);
					} catch (Exception e) {
						System.out.println(value);
						e.printStackTrace();
					}
					dstat.setDouble(6, d);
					dstat.addBatch();
				}
			}
			dstat.executeBatch();
			System.out.println(comName + "finish");
		}
		conn.close();
		br.close();
	}
	
	public static void stockCrawler() throws Exception {
		FileWriter dw = new FileWriter("data/stock/delist.txt");
		FileWriter nw = new FileWriter("data/stock/newlist.txt");
		int deList = 0;
		int newList = 0;
		Set<String> stockIdSet = getStockId();
		System.out.println(stockIdSet.size());
		Set<String> currentIdSet = getCurrentStockId();
		Set<String> newIdSet = new HashSet<String>();
		System.out.println(currentIdSet.size());
		for (String id : currentIdSet) {
			if (!stockIdSet.contains(id)) {
				deList++;
				dw.write(id + "\r\n");
				dw.flush();
			}
		}
		for (String id : stockIdSet) {
			if (!currentIdSet.contains(id)) {
				newList++;
				newIdSet.add(id);
				nw.write(id + "\r\n");
				nw.flush();
			}
		}
		dw.close();
		nw.close();
		parser(newIdSet);
		System.out.println("finish");
		System.out.println("新增\t"+newList);
		System.out.println("退市\t"+deList);
	}
	
	public static void parser(Set<String> stockIdSet) throws Exception {
		FileWriter cw = new FileWriter("data/stock/com_info.txt");
		Set<String> comSet = getCurrentCom();
		HttpReader reader = new StaticHttpReader();
		FileWriter fw = null;
		FileWriter hw = null;
		try {
			fw = new FileWriter("data/stock/new_id.txt");
			hw = new FileWriter("data/stock/has_com.txt");
			for (String id : stockIdSet) {
				String prefix = getPrefix(id);
				String infoUrl = originUrl + prefix + id + ".html";
				String source = reader.readSource(infoUrl);
				Map<String,String> infoMap = getInfo(source);
				String comName = infoMap.get("name");
				if (!comSet.contains(comName)) {
					infoMap.put("code", id);
					String info = JSON.toJSONString(infoMap);
					cw.write(info + "\r\n");
					cw.flush();
					fw.write(id + "\t" + infoUrl + "\r\n");
					fw.flush();
				} else {
					System.out.println(comName);
					hw.write(id + "\t" + infoUrl + "\r\n");
					hw.flush();
				}
			}
			cw.close();
			fw.close();
			hw.close();
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static Map<String,String> getInfo(String source) throws Exception {
		Map<String,String> infoMap = new HashMap<String,String>();
		Document doc = Jsoup.parse(source);
		String name = doc.select("td.zx_data2").get(0).text();
		infoMap.put("name", name);
		String englishName = doc.select("td.zx_data2").get(1).text();
		infoMap.put("61", englishName);
		String address = doc.select("td.zx_data2").get(2).text();
		infoMap.put("69", address);
		String simpleName = doc.select("td.zx_data2").get(3).text();
		infoMap.put("simpleName", simpleName);
		String money = doc.select("td.zx_data2").get(6).text().replace(",", "");
		infoMap.put("68", money);
		String tel =  doc.select("td.zx_data2").get(9).text();
		infoMap.put("72", tel);	
		String tax =  doc.select("td.zx_data2").get(10).text();
		infoMap.put("72", tax);
		String website = doc.select("td.zx_data2").get(11).text();
		infoMap.put("90", website);
		String holdTime =  doc.select("td.zx_data2").get(12).text();
		infoMap.put("91", holdTime);
		String zgTime =  doc.select("td.zx_data2").get(13).text();
		infoMap.put("92", zgTime);
		String gs = doc.select("td.zx_data2").get(14).text().replace(",", "");
		infoMap.put("93", gs);
		String price = doc.select("td.zx_data2").get(15).text();
		infoMap.put("94", price);
		String pe = doc.select("td.zx_data2").get(16).text();
		infoMap.put("95", pe);
		String way = doc.select("td.zx_data2").get(17).text();
		infoMap.put("96", way);
		String fr = doc.select("td.zx_data2").get(4).text();
		infoMap.put("63", fr);
		String dm = doc.select("td.zx_data2").get(5).text();
		infoMap.put("66", dm);
		String mainSale = doc.select("td.zx_data2").get(18).text();
		infoMap.put("97", mainSale);
		String recommend = doc.select("td.zx_data2").get(19).text();
		infoMap.put("98", recommend);
		String secure = doc.select("td.zx_data2").get(20).text();
		infoMap.put("99", secure);
		return infoMap;
	}
	
	public static String getPrefix(String id) {
		String prefix = "";
		try {
			String flag = id.substring(0,1);
			if (flag.equals("6") || flag.equals("9")) {
				prefix = "shmb";
			} else if (flag.equals("3")){
				prefix = "szcn";
			} else if (id.substring(0,3).equals("002")) {
				prefix = "szsme";
			} else {
				prefix = "szmb";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return prefix;
	}
	
	public static Set<String> getStockId() {
		Set<String> stockIdSet = new HashSet<String>();
		String input = "";
		BufferedReader br = null;
		String source = "";
		try {
			br = BufferedReaderUtil.getBuffer("data/stock/stock.txt");
			while ((input = br.readLine())!=null) {
				source = input;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] sourceArr = source.split("\\|");
		for (String com : sourceArr) {
			String[] idArr = com.split("-");
			stockIdSet.add(idArr[1]);
		}
		return stockIdSet;
	}
	
	public static Set<String> getCurrentCom() throws Exception {
		FileWriter fw = new FileWriter("data/stock/current_com.txt");
		Set<String> set = new HashSet<String>();
		String query = "select name from basic_info where meaning_tag = ?";
		Connection connection = getConnection();
		PreparedStatement qstat = connection.prepareStatement(query);
		qstat.setString(1, "上市公司");
		ResultSet rs = qstat.executeQuery();
		while (rs.next()) {
			String name = rs.getString("name");
			set.add(name);
			fw.write(name + "\r\n");
			fw.flush();
		}
		query = "select synonym from basic_info where type = ?";
		PreparedStatement astat = connection.prepareStatement(query);
		astat.setInt(1, 1);
		rs = astat.executeQuery();
		while (rs.next()) {
			String stock = rs.getString("synonym");
			if (stock.contains("_-_")) {
				String[] split = stock.split("_-_");
				set.add(split[0]); 
				System.out.println(split[0]);
				fw.write(split[0]+"\r\n");
				fw.flush();
			}
		}
		fw.close();
		return set;
	}
	
	public static Set<String> getCurrentStockId() throws Exception {
		FileWriter fw = new FileWriter("data/stock/current_id.txt");
		Set<String> set = new HashSet<String>();
		String query = "select synonym from basic_info where type = ?";
		Connection connection = getConnection();
		PreparedStatement qstat = connection.prepareStatement(query);
		qstat.setInt(1, 1);
		ResultSet rs = qstat.executeQuery();
		while (rs.next()) {
			String stock = rs.getString("synonym");
			if (stock.contains("_-_")) {
				String[] split = stock.split("_-_");
				fw.write(split[1]+"\r\n");
				fw.flush();
//				System.out.println("com" + split[0] + "\t" + split[1]);
				set.add(split[1]); 
			}
		}
		query = "select synonym from basic_info where meaning_tag = ?";
		qstat = connection.prepareStatement(query);
		qstat.setString(1, "股票");
		rs = qstat.executeQuery();
		while (rs.next()) {
			String stock = rs.getString("synonym");
			if (stock.contains("_-_")) {
				String[] split = stock.split("_-_");
				fw.write(split[1]+"\r\n");
				fw.flush();
				set.add(split[1]); 
			} else {
				fw.write(stock + "\r\n");
				fw.flush();
				set.add(stock);
			}
		}
		fw.close();
		return set;
	}
	
	public static Long getId() throws Exception {
		long id = 0;
		String query = "select MAX(id) from basic_info";
		Connection conn = getConnection();
		PreparedStatement pstat = conn.prepareStatement(query);
		ResultSet rs = pstat.executeQuery();
		while (rs.next()) {
			id = rs.getLong(1);
		}
		conn.close();
		return id;
	}
	
	public static Connection getConnection() throws Exception {
		Class.forName("com.mysql.jdbc.Driver");
		Connection conn = DriverManager.getConnection(ConstResource.MYSQL_URL,ConstResource.MYSQL_USER,ConstResource.MYSQL_PASSWORD);
		System.out.println("connection successfully");
		return conn;
	}
}
