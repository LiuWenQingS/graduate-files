package com.hiekn.keso;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.alibaba.fastjson.JSONObject;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class ExportES {
	static MongoClient client = new MongoClient("192.168.1.31",27017);
	static DB db = client.getDB("kg_keso");

	public static void main(String[] args) {
		try {
//			exportPeople();
//			exportPatent();
//			exportPaper();
//			exportSchool();
//			exportIns();
//			exportEntPrompt("ins", 13L);
//			exportEntPrompt("person", 2L);
//			exportEntPrompt("paper", 9L);
//			exportEntPrompt("patent", 3L);
//			exportEntPrompt("school", 12L);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exportIns() throws IOException {
		int count = 0;
		FileWriter fw = new FileWriter("data/cnki/es/ins_info.txt");
		DBCursor cursor = db.getCollection("entity_id").find(new BasicDBObject("concept_id",13L).append("type", 1));
		try {
			while (cursor.hasNext()) {
				System.out.println(count++);
				JSONObject rO = new JSONObject();
				DBObject obj = cursor.next();
				long id = (Long) obj.get("id"); 
				String name = obj.get("name").toString();
				String abs = getAbs(id);
				rO.put("name", name);
				rO.put("id", id);
				rO.put("abs", abs);
				fw.write(rO.toJSONString() + "\r\n");
				fw.flush();
			}
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exportEntPrompt(String name,long type) throws IOException {
		int count = 0;
		FileWriter fw = new FileWriter("data/cnki/es/"+name+"_prompt.txt");
		DBCursor cursor = db.getCollection("entity_id").find(new BasicDBObject("concept_id",type).append("type", 1));
		try {
			while (cursor.hasNext()) {
				System.out.println(count++);
				JSONObject rO = new JSONObject();
				DBObject obj = cursor.next();
				long id = (Long) obj.get("id"); 
				String ename = obj.get("name").toString();
				rO.put("name", ename);
				rO.put("id", id);
				fw.write(rO.toJSONString() + "\r\n");
				fw.flush();
			}
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void exportPeople() throws IOException {
		int count = 0;
		FileWriter fw = new FileWriter("data/cnki/es/peo_info.txt");
		DBCursor cursor = db.getCollection("entity_id").find(new BasicDBObject("concept_id",2L));
		try {
			while (cursor.hasNext()) {
				System.out.println(count++);
				JSONObject rO = new JSONObject();
				DBObject obj = cursor.next();
				long peoId = (Long) obj.get("id"); 
				String peoName = obj.get("name").toString();
				long insId = getRelaionId(peoId);
				if (insId != 0) {
					String pos = getRelaionPos(peoId);
					String insName = getEntityName(insId);
					long insType = KesoImport.getEntityType(insId);
					if (insType == 13) {
						rO.put("ins", insName);
						rO.put("insId", insId);
					} else if (insType == 12) {
						rO.put("uni", insName);
						rO.put("uniId", insId);
					} else if (insType == 15){
						rO.put("com", insName);
						rO.put("comId", insId);
					}
					rO.put("pos", pos);
				}
				rO.put("name", peoName);
				rO.put("id", peoId);
				
				fw.write(rO.toJSONString() + "\r\n");
				fw.flush();
			}
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exportPatent() throws IOException {
		int count = 0;
		FileWriter fw = new FileWriter("data/cnki/es/patent_info.txt");
		DBCursor cursor = db.getCollection("entity_id").find(new BasicDBObject("concept_id",3L));
		try {
			while (cursor.hasNext()) {
				System.out.println(count++);
				JSONObject rO = new JSONObject();
				DBObject obj = cursor.next();
				long patentId = (Long) obj.get("id"); 
				String patentName = obj.get("name").toString();
				String patentType = getStr(patentId, 304);
				String aT = getStr(patentId, 303);
				String pT = getStr(patentId, 316);
				String abs = getAbs(patentId);
				List<JSONObject> holderList = getHolderList(patentId);
				rO.put("name", patentName);
				rO.put("id", patentId);
				rO.put("abs", abs);
				rO.put("type", patentType);
				rO.put("aT", aT);
				rO.put("pT", pT);
				rO.put("holderList", holderList);
				fw.write(rO.toJSONString() + "\r\n");
				fw.flush();
			}
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static List<JSONObject> getHolderList(long id) {
		List<JSONObject> holderList = new ArrayList<JSONObject>();
		DBCursor cursor = db.getCollection("attribute_object").find(new BasicDBObject("attr_id",315).append("attr_value_type", 12).append("entity_id",id));
		try {
			while (cursor.hasNext()) {
				JSONObject rO = new JSONObject();
				DBObject obj = cursor.next(); 
				long rid = (Long) obj.get("attr_value");
				String name = getEntityName(rid);
				rO.put("name", name);
				rO.put("id", rid);
				holderList.add(rO);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return holderList;
	}
	
	public static void exportPaper() throws IOException {
		int count = 0;
		FileWriter fw = new FileWriter("data/cnki/es/paper_info.txt");
		DBCursor cursor = db.getCollection("entity_id").find(new BasicDBObject("concept_id",9L));
		try {
			while (cursor.hasNext()) {
				System.out.println(count++);
				JSONObject rO = new JSONObject();
				DBObject obj = cursor.next();
				long paperId = (Long) obj.get("id"); 
				String paperName = obj.get("name").toString();
				String abs = getAbs(paperId);
				String type = getStr(paperId, 904);
				String time = getStr(paperId, 906);
				if (time.length() == 4) {
					time = time + "-01-01";
				}
				List<String> tagList = new ArrayList<String>();
				DBCursor tagCursor = db.getCollection("attribute_object").find(new BasicDBObject("entity_id",paperId).append("attr_id", 907));
				try {
					while (tagCursor.hasNext()) {
						long tagId = (Long) tagCursor.next().get("attr_value");
						String tagName = getEntityName(tagId);
						tagList.add(tagName);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				List<JSONObject> schList = new ArrayList<JSONObject>();
				Set<Long> schIdSet = new HashSet<Long>();
				DBCursor peoCursor = db.getCollection("attribute_object").find(new BasicDBObject("attr_id",905).append("entity_id", paperId));
				try {
					while (peoCursor.hasNext()) {
						DBObject pO = peoCursor.next();
						long pid = (Long) pO.get("attr_value");
						DBCursor schCursor = db.getCollection("attribute_object").find(new BasicDBObject("attr_id",208).append("entity_id", pid));
						while (schCursor.hasNext()) {
							long schId = (Long) schCursor.next().get("attr_value");
							long schType = KesoImport.getEntityType(schId);
							if (schType == 13) {
								schIdSet.add(1000001L);
							} else {
								schIdSet.add(schId);
							}
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
				for (Long schId : schIdSet) {
					String name = getEntityName(schId);
					JSONObject sO = new JSONObject();
					sO.put("name", name);
					sO.put("id", schId);
					schList.add(sO);
				}
				rO.put("name", paperName);
				rO.put("id", paperId);
				rO.put("abs", abs);
				rO.put("tag", tagList);
				rO.put("type", type);
				rO.put("time", time);
				rO.put("sch", schList);
				fw.write(rO.toJSONString() + "\r\n");
				fw.flush();
			}
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void exportSchool() throws IOException {
		int count = 0;
		FileWriter fw = new FileWriter("data/cnki/es/school_info.txt");
		DBCursor cursor = db.getCollection("entity_id").find(new BasicDBObject("concept_id",12L));
		try {
			while (cursor.hasNext()) {
				System.out.println(count++);
				JSONObject rO = new JSONObject();
				DBObject obj = cursor.next();
				long schoolId = (Long) obj.get("id"); 
				String schoolName = obj.get("name").toString();
				String abs = getAbs(schoolId);
				rO.put("name", schoolName);
				rO.put("id", schoolId);
				rO.put("abs", abs);
				fw.write(rO.toJSONString() + "\r\n");
				fw.flush();
			}
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getAbs(long id) {
		DBObject obj = db.getCollection("entity_abstract").findOne(new BasicDBObject("id",id));
		String abs = "";
		try {
			abs = obj.get("abstract").toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return abs;
	}
	
	public static String getStr(long id,int attrId) {
		DBObject obj = db.getCollection("attribute_string").findOne(new BasicDBObject("entity_id",id)
		.append("attr_id", attrId));
		String attrValue = "";
		try {
			attrValue = obj.get("attr_value").toString();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return attrValue;
	}
	
	public static String getEntityName(long id) {
		String name = "";
		DBObject obj = db.getCollection("entity_id").findOne(new BasicDBObject("id",id));
		name = obj.get("name").toString();
		return name;
	}
	
	public static long getRelaionId(long id) {
		DBObject obj = db.getCollection("attribute_object").findOne(new BasicDBObject("entity_id",id).append("attr_id", 208));
		long rid = 0;
		if (obj != null) {
			rid = (Long) obj.get("attr_value");
		}
		return rid;
	}
	
	public static String getRelaionPos(long id) {
		System.out.println("rel " + id);
		DBObject obj = db.getCollection("attribute_object").findOne(new BasicDBObject("entity_id",id).append("attr_id", 208));
		String pos = "";
		if (obj != null) {
			if (obj.containsField("attr_ext_208_1")) {
				pos = obj.get("attr_ext_208_1").toString();
			}
		}
		return pos;
	}
}
