package com.hiekn.keso;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bson.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.hiekn.util.BufferedReaderUtil;
import com.hiekn.util.HttpReader;
import com.hiekn.util.StaticHttpReader;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;

public class CnkiParser {
	public static void main(String[] args) {
		try {
//			paperProcess();
//			getBitPeople();
//			patentProcess();
//			getType();
//			kesoProcess();
//			getAllImportPeople();
//			getPeoSchoolRelation();
//			kesoWanfangInfo();
//			getAllImportInstitute();
			statics();
//			staticsPro();
//			getPeoSchoolY();
//			IntegrateYx();
//			getXy();
//			subIns();
//			IntegrateNew();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void subIns() throws IOException {
		String input = "";
		Set<String> ins = getIns();
		FileWriter fw = new FileWriter("data/cnki/peo_sch_rel_yx.txt");
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/peo_sch_rel_yx_all.txt");
		while ((input = br.readLine())!=null) {
			for (String string : ins) {
				if (input.contains(string)) {
					
					if (input.contains("北京理工大学教育研究院")) {
						fw.write(input + "\r\n");
						fw.flush();
					} else {
						input = input.substring(0,input.lastIndexOf("学院")+2);
						if (input.length() > 20 ) {
							System.out.println(input);
						}
						fw.write(input + "\r\n");
						fw.flush();
					}
				}
			}
		}
		fw.close();
	}
	
	public static Set<String> getIns() {
		Set<String> set = new HashSet<String>();
		set.add("北京理工大学宇航学院");
		set.add("北京理工大学机电学院");
		set.add("北京理工大学机械与车辆学院");
		set.add("北京理工大学光电学院");
		set.add("北京理工大学信息与电子学院");
		set.add("北京理工大学自动化学院");
		set.add("北京理工大学计算机学院");
		set.add("北京理工大学软件学院");
		set.add("北京理工大学材料学院");
		set.add("北京理工大学化学与化工学院");
		set.add("北京理工大学生命学院");
		set.add("北京理工大学数学与统计学院");
		set.add("北京理工大学物理学院");
		set.add("北京理工大学人文与社会科学学院");
		set.add("北京理工大学马克思主义学院");
		set.add("北京理工大学法学院");
		set.add("北京理工大学外国语学院");
		set.add("北京理工大学设计与艺术学院");
		set.add("北京理工大学教育研究院");
		return set;
	}
	
	
	public static void getXy() throws IOException {
		Set<String> set = new HashSet<String>();
		FileWriter fw = new FileWriter("data/cnki/xy.txt");
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/peo_sch_rel_yx.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			set.add(input.split("\t")[1]);
		}
		for (String string : set) {
			fw.write(string + "\r\n");
			fw.flush();
		}
		fw.close();
	}
	
	public static void IntegrateNew() throws IOException {
		Map<String,String> yxMap = new HashMap<String,String>();
		FileWriter fw = new FileWriter("data/cnki/peo_sch_rel_new_all.txt");
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/peo_sch_rel_new.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			String result = "";
			String[] rA = input.split("\t");
			for (int i = 1; i < rA.length; i++) {
				result = result + "\t" + rA[i];
			}
			yxMap.put(input.split("\t")[0], result);
		}
		br = BufferedReaderUtil.getBuffer("data/cnki/bit_people_all.txt");
		while ((input = br.readLine())!=null) {
			String[] split = input.split("\t");
			String name = split[1];
			if (split.length == 3) {
				String result = "";
				String pos = split[2];
				result = "\t" + "北京理工大学" + "\t" + "职位"+ pos + "\t北京理工大学" + split[0];
				yxMap.put(name, result);
			} else if (split.length == 4) {
				String pos = split[2] + " " + split[3];
				String result = "";
				result = "\t" + "北京理工大学" + "\t" + "职位"+ pos + "\t北京理工大学" + split[0];
				yxMap.put(name, result);
			}
		}
		for (Entry<String,String> entry : yxMap.entrySet()) {
			fw.write(entry.getKey() + entry.getValue() + "\r\n");
			fw.flush();
		}
		fw.close();
	}
	
	
	public static void IntegrateYx() throws IOException {
		Map<String,String> yxMap = new HashMap<String,String>();
		FileWriter fw = new FileWriter("data/cnki/peo_sch_rel_new.txt");
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/peo_sch_rel_yx.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			yxMap.put(input.split("\t")[0], input.split("\t")[1]);
		}
		br = BufferedReaderUtil.getBuffer("data/cnki/peo_sch_rel.txt");
		while ((input = br.readLine())!=null) {
			if (yxMap.containsKey(input.split("\t")[0])) {
				if (input.split("\t").length == 2) {
					fw.write(input + "\t\t" + yxMap.get(input.split("\t")[0]) + "\r\n");
					fw.flush();
				} else {
					fw.write(input + "\t" + yxMap.get(input.split("\t")[0]) + "\r\n");
					fw.flush();
				}
			} else {
				fw.write(input + "\r\n");
				fw.flush();
			}
		}
		fw.close();
	}
	
	public static void staticsPro() throws IOException {
		int count = 0;
		double pro = 0;
		double subPro = 0;
		double all = 707;
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/peo_sch_rel_has_job.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			String[] split = input.split("\t")[2].replace("职位", "").split(" ");
			for (String string : split) {
				if (string.equals("副教授")) {
					subPro++;
				} else if (string.equals("教授")) {
					pro++;
				}
			}
		}
		double sub = subPro / all;
		System.out.println(sub);
		System.out.println(pro/all);
		br.close();
	}
	
	public static void statics() throws IOException {
		int count = 0;
		int all = 0;
		double i = 0;
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/peo_sch_rel_new_all.txt");
		FileWriter fw = new FileWriter("data/cnki/peo_sch_rel_stat.txt");
		FileWriter dw = new FileWriter("data/cnki/peo_sch_rel_has_job.txt");
		Set<String> set = new HashSet<String>();
		String input = "";
		while ((input = br.readLine())!=null) {
			String[] split = input.split("\t");
			if (split.length > 2) {
				count++;
				dw.write(input+"\r\n");
				dw.flush();
				String[] pos = split[2].replace("职位","").split(" ");
				for (String string : pos) {
					set.add(string);
				}
			}
		}
		for (String string : set) {
			fw.write(string + "\r\n");
			fw.flush();
		}
		System.out.println(count);
		br.close();
		fw.close();
		dw.close();
	}
	
	public static void getPeoSchoolY() throws IOException {
		String input = "";
		Set<String> bitPSet = getBitPeople();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/bit_p.txt");
		FileWriter fw = new FileWriter("data/cnki/peo_sch_rel_yx.txt");
		//cnki data
		while ((input = br.readLine())!=null) {
			JSONObject obj = JSONObject.parseObject(input);
			JSONArray instArr = obj.getJSONArray("institute");
			String ins = "";
			if (instArr.size() == 2) {
				if (instArr.get(0).toString().contains("北京理工") && instArr.get(1).toString().contains("北京理工")) {
					
				} else if (instArr.get(0).toString().contains("北京理工")) {
					ins = instArr.getString(0);
				} else {
					ins = instArr.getString(1);
				}
			} else if (instArr.size() == 1) {
				if (instArr.getString(0).contains("北京理工")) {
					ins = instArr.getString(0);
				}
			}
			JSONArray authArr = obj.getJSONArray("author");
			for (Object author : authArr) {
				if (bitPSet.contains(author.toString()) && !ins.equals("") && !ins.equals("北京理工大学")) {
					fw.write(author.toString() + "\t" + ins + "\r\n");
					fw.flush();
				}
			}
		}
		fw.close();
	}
	
	public static void getPeoSchoolRelation() throws IOException {
		String input = "";
		Map<String,String> map = new HashMap<String,String>();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/bit_p.txt");
		FileWriter fw = new FileWriter("data/cnki/peo_sch_rel.txt");
		FileWriter iw = new FileWriter("data/cnki/info.txt");
		//cnki data
		while ((input = br.readLine())!=null) {
			JSONObject obj = JSONObject.parseObject(input);
			JSONArray instArr = obj.getJSONArray("institute");
			String ins = "";
			if (instArr.size() == 2) {
				if (instArr.get(0).toString().contains("北京理工") && instArr.get(1).toString().contains("北京理工")) {
					ins = "北京理工大学";
				} else if (instArr.get(0).toString().contains("北京理工")) {
					ins = instArr.getString(1);
				} else {
					ins = instArr.getString(0);
				}
			} else if (instArr.size() == 1) {
				if (instArr.getString(0).contains("北京理工")) {
					ins = "北京理工大学";
				}
			}
			if (!ins.equals("")) {
				JSONArray authArr = obj.getJSONArray("author");
				for (Object author : authArr) {
					if (ins.contains("大学")) {
						ins = ins.substring(0,ins.lastIndexOf("大学")+2);
					}
					map.put(author.toString(), ins);
				}
			}
		}
		br = BufferedReaderUtil.getBuffer("data/cnki/keso_new.txt");
		while ((input = br.readLine())!=null) {
			JSONObject obj = JSONObject.parseObject(input);
			String pos = "";
			if (obj.containsKey("pos")) {
				pos = obj.getString("pos");
				if (!pos.equals("")) {
					String name = obj.getString("name");
					map.put(name, "北京理工大学\t" + "职位" +pos);
				}
			}
		}
		br = BufferedReaderUtil.getBuffer("data/cnki/bit_patent.txt");
		while ((input = br.readLine())!=null) {
			JSONObject obj = JSONObject.parseObject(input);
			JSONArray holdArr = obj.getJSONArray("holder");
			if (holdArr.size() == 1) {
				JSONArray inventArr = obj.getJSONArray("inventor");
				for (Object invObj : inventArr) {
					String inventor = invObj.toString();
					if (map.containsKey(inventor)) {
						if (!map.get(inventor).contains("职位")) {
							map.put(inventor, "北京理工大学");
						}
					} else {
						map.put(inventor, "北京理工大学");
					}
				}
			} else if (holdArr.size() == 2){
				iw.write(input + "\r\n");
				iw.flush();
				JSONArray inventArr = obj.getJSONArray("inventor");
				String othIns = "";
				if (holdArr.getString(0).contains("北京理工")) {
					othIns = holdArr.getString(1);
					if (othIns.contains("大学")) {
						othIns = othIns.substring(0,othIns.lastIndexOf("大学")+2);
					}
				} else {
					othIns = holdArr.getString(0);
					if (othIns.contains("大学")) {
						othIns = othIns.substring(0,othIns.lastIndexOf("大学")+2);
					}
				}
				for (Object invObj : inventArr) {
					String inventor = invObj.toString();
					if (map.containsKey(inventor)) {
						
					} else {
						map.put(inventor, othIns);
					}
				}
			} else {
				iw.write(input + "\r\n");
				iw.flush();
			}
		}
		br = BufferedReaderUtil.getBuffer("data/cnki/import_people.txt");
		while ((input = br.readLine())!=null) {
			if (!map.containsKey(input)) {
				System.out.println(input);
			}
		}
		for (Entry<String,String> entry : map.entrySet()) {
			fw.write(entry.getKey() + "\t" + entry.getValue() + "\r\n");
			fw.flush();
		}
		fw.close();
		iw.close();
		br.close();
	}
	
	public static Set<String> getBitPeople() throws IOException {
		String input = "";
		Set<String> set = new HashSet<String>();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/peo_sch_rel.txt");
		int count = 0;
		while ((input = br.readLine())!=null) {
			System.out.println(count++);
			String[] split = input.split("\t");
			if (split[1].contains("北京理工")) {
				set.add(split[0]);
			}
		}
		return set;
	}
	
	
	public static void kesoWanfangInfo() throws IOException, InterruptedException {
		HttpReader reader = new StaticHttpReader();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/keso_new.txt");
		FileWriter fw = new FileWriter("data/cnki/keso_new_wf.txt");
		String input = "";
		int count = 0;
		while ((input = br.readLine())!=null) {
			System.out.println(count++);
			JSONObject obj = JSONObject.parseObject(input);
			List<JSONObject> paperList = new ArrayList<JSONObject>();
			JSONArray paperArr = obj.getJSONArray("paper");
			for (Object object : paperArr) {
				JSONObject pObj = (JSONObject) object;
				String url = pObj.getString("url_s");
				String source = reader.readSource(url);
				org.jsoup.nodes.Document doc = Jsoup.parse(source);
				List<String> insList = new ArrayList<String>();
				Elements ele = doc.select("div.row");
				for (Element element : ele) {
					if (element.text().contains("作者单位")) {
						String insStr = element.select("span.text").html();
						String[] insArr = insStr.split("<span>");
						for (String ins : insArr) {
							System.out.println(ins);
							insList.add(ins.replace("</span>", ""));
						}
						break;
					}
				}
				TimeUnit.SECONDS.sleep(3);
				pObj.put("ins", insList);
				paperList.add(pObj);
			}
			obj.put("paper", paperList);
			fw.write(JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect) + "\r\n");
			fw.flush();
		}
		
		fw.close();
		br.close();
		reader.close();
	}
	

	public static void kesoProcess() throws IOException {
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/keso_all.txt");
		FileWriter fw = new FileWriter("data/cnki/keso_new_all.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			JSONObject obj = JSONObject.parseObject(input);
			String pos = "";
			if (obj.containsKey("school")) {
				pos = getPos(obj.getString("school"));
			}
			obj.put("pos", pos);
			obj.put("school", "北京理工大学");
			List<JSONObject> paperList = new ArrayList<JSONObject>();
			JSONArray arr = obj.getJSONArray("paper");
			for (Object object : arr) {
				try {
					JSONArray innerArr = (JSONArray) object;
					for (Object object2 : innerArr) {
						JSONObject innerObj = (JSONObject) object2;
						paperList.add(innerObj);
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			obj.put("paper", paperList);
			fw.write(JSON.toJSONString(obj, SerializerFeature.DisableCircularReferenceDetect) + "\r\n");
			fw.flush();
		}
		br.close();
		fw.close();
	}
	
	public static String getPos(String input) {
		String regex = "(?<=职位：).*?(?=相关)";
		String pos = "";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(input);
		while (m.find()) {
			pos = m.group().trim();
		}
		return pos;
	}
	
	/**
	 * 获得专利的类型
	 * @throws IOException
	 */
	public static void getType() throws IOException {
		Set<String> typeSet = new HashSet<String>();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/bit_patent.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			JSONObject obj = JSONObject.parseObject(input);
			typeSet.add(obj.getString("type"));
		}
		for (String string : typeSet) {
			System.out.println(string);
		}
	}
	
	
	public static void patentProcess() throws Exception {
		String input = "";
		int count = 0;
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/bit_people_all.txt");
		FileWriter fw = new FileWriter("data/cnki/bit_patent_all.txt");
		Document searchDoc = new Document();
		MongoClient client = new MongoClient("202.127.3.194",19129);
		MongoCollection<Document> collection = client.getDatabase("patent_kg_b")
														.getCollection("patent_info");
		while ((input = br.readLine())!=null) {
			System.out.println(input);
			searchDoc.append("holder", "北京理工大学").append("inventor", input.split("\t")[1]);
			MongoCursor<Document> cursor = collection.find(searchDoc).iterator();
			try {
				while (cursor.hasNext()) {
					System.out.println(count++);
					Document doc = cursor.next();
					fw.write(doc.toJson() + "\r\n");
					fw.flush();
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
		br.close();
		fw.close();
		client.close(); 
	}
	
	public static void getAllImportPeople() throws Exception {
		FileWriter fw = new FileWriter("data/cnki/import_people.txt");
		Document searchDoc = new Document();
		MongoClient client = new MongoClient("192.168.1.31",27017);
		MongoCollection<Document> collection = client.getDatabase("kg_keso")
				.getCollection("entity_id");
		searchDoc.append("concept_id", 2L);
		MongoCursor<Document> cursor = collection.find(searchDoc).iterator();
		try {
			while (cursor.hasNext()) {
				Document doc = cursor.next();
				fw.write(doc.getString("name")+ "\r\n");
				fw.flush();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		fw.close();
		client.close(); 
	}
	public static void getAllImportInstitute() throws Exception {
		FileWriter fw = new FileWriter("data/cnki/import_ins.txt");
		Document searchDoc = new Document();
		MongoClient client = new MongoClient("192.168.1.31",27017);
		MongoCollection<Document> collection = client.getDatabase("kg_keso")
				.getCollection("entity_id");
		searchDoc.append("concept_id", 12L);
		MongoCursor<Document> cursor = collection.find(searchDoc).iterator();
		try {
			while (cursor.hasNext()) {
				Document doc = cursor.next();
				fw.write(doc.getString("name")+ "\r\n");
				fw.flush();
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		fw.close();
		client.close(); 
	}
	
	public static void paperProcess() throws Exception {
		Set<String> set = new HashSet<String>();
		Set<String> peoSet = new HashSet<String>();
		FileWriter fw = new FileWriter("data/cnki/bit_p.txt");
		FileWriter peoW = new FileWriter("data/cnki/bit_people.txt");
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/bit.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			set.add(input);
		}
		System.out.println(set.size());
		for (String string : set) {
			JSONObject obj = JSON.parseObject(string);
			List<String> peopleList = new ArrayList<String>();
			List<String> instituteList = new ArrayList<String>();
			String source = obj.getString("source");
			if (source.equals("硕士") || source.equals("博士")) continue; 
			if (obj.containsKey("author")) {
				String peoStr = obj.getString("author").replace("【作者】 ", "");
				String[] peoArr = peoStr.split("；");
				for (String peo : peoArr) {
					peopleList.add(peo.trim());
					if (peoSet.contains(peo.trim())) {
						System.out.println(peo.trim());
					}
					peoSet.add(peo.trim());
				}
			}
			if (obj.containsKey("institute")) {
				String insStr = obj.getString("institute").replace("【机构】", "");
				String[] insArr = insStr.split("；");
				for (String ins : insArr) {
					instituteList.add(ins.trim());
				}
			}
			obj.put("author", peopleList);
			obj.put("institute", instituteList);
			fw.write(obj.toJSONString() + "\r\n");
			fw.flush();
		}
		
		for (String peo : peoSet) {
			peoW.write(peo + "\r\n");
			peoW.flush();
		}
		fw.close();
		peoW.close();
		br.close();
	}
}
