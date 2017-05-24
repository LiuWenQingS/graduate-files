package com.hiekn.keso;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hiekn.util.BufferedReaderUtil;
import com.hiekn.util.KgObjectGenerator;
import com.hiekn.util.StringTimeUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class KesoImport {
	
	static MongoClient client = new MongoClient("192.168.1.31",27017);
	static DB db = client.getDB("kg_keso");
	
	static DBCollection extCollection = db.getCollection("attribute_object_ext");
	static DBCollection basicInfoCollection = db.getCollection("basic_info");
	static DBCollection attObjCollection = db.getCollection("attribute_object");
	static DBCollection attExtCollection = db.getCollection("attribute_object_ext");
	static DBCollection attStrCollection = db.getCollection("attribute_string");
	static DBCollection attSumCollection = db.getCollection("attribute_summary");
	static DBCollection conInsCollection = db.getCollection("concept_instance");
	static DBCollection entIdCollection = db.getCollection("entity_id");
	static DBCollection entAbsCollection = db.getCollection("entity_abstract");
	static DBCollection parSonCollection = db.getCollection("parent_son");
	static BasicDBObject basicInfo = new BasicDBObject();
	static BasicDBObject attObj = new BasicDBObject();
	static BasicDBObject attStr = new BasicDBObject();
	static BasicDBObject attExt = new BasicDBObject();
	static BasicDBObject attSum = new BasicDBObject();
	static BasicDBObject conIns = new BasicDBObject();
	static BasicDBObject entId = new BasicDBObject();
	static BasicDBObject entAbs = new BasicDBObject();
	static BasicDBObject parSon = new BasicDBObject();
	
	public static void main(String[] args) {
		try {
			long start = System.currentTimeMillis();
			createBasic();
			createParSon();
			createIndex();
			cnkiDataImport();
			kesoDataImport();
			kesoAllDataImport();
			patentDataImport();
			peoSchRelationImport();
			schoolAbsImport();
			insAbsImport();
			long finish = System.currentTimeMillis() - start;
			System.out.println(finish);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void insAbsImport() throws IOException {
		String input = "";
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/bit_ins_abs.txt");
		KgObjectGenerator generator = new KgObjectGenerator();
		while ((input = br.readLine())!=null) {
			JSONObject obj = JSONObject.parseObject(input);
			String name = obj.getString("ins");
			String abs = obj.getString("abs");
			if (!abs.equals("")) {
				long id = hasEntity(name);
				if (id != -1) {
					System.out.println(id + abs);
					entAbs = generator.createAbsObject(id, abs);
					entAbsCollection.insert(entAbs);
				}
			}
		}
		br.close();
	}
	
	
	public static void schoolAbsImport() throws IOException {
		String input = "";
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/ins_1.txt");
		KgObjectGenerator generator = new KgObjectGenerator();
		while ((input = br.readLine())!=null) {
			JSONObject obj = JSONObject.parseObject(input);
			String name = obj.getString("name");
			String abs = obj.getString("abs");
			if (!abs.equals("")) {
				long id = hasEntity(name);
				if (id != -1) {
					System.out.println(id + abs);
					entAbs = generator.createAbsObject(id, abs);
					entAbsCollection.insert(entAbs);
				}
			}
		}
		br.close();
	}
	
	public static void peoSchRelationImport() throws IOException {
		String input = "";
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/peo_sch_rel_new_all.txt");
		KgObjectGenerator generator = new KgObjectGenerator();
		long id = getId();
		while ((input = br.readLine())!=null) {
//			System.out.println(count++);
			String[] relArr = input.split("\t");
			String peo = relArr[0];
			long peoId = hasEntity(peo);
			if (peoId != -1) {
				String institute = relArr[1];
				if (institute.equals("北京理工大学") && relArr.length ==2) continue;
				if (institute.equals("院校未知")) {
					System.out.println(peo + "院校未知");
					continue;
				}
				long instituteId = hasEntity(institute);
				if (instituteId == -1) {
					//新增机构
					instituteId = ++id;
					long instituteType;
					if (institute.contains("学院") || institute.contains("大学")) {
						instituteType = 12L;
					} else if (institute.contains("公司")){
						instituteType = 11L;
					} else {
						instituteType = 15L;
					}
					basicInfo = generator.createBasicObject(instituteId, institute, "");
					basicInfoCollection.insert(basicInfo);
					entId = generator.createEidObject(institute, instituteId, instituteType, 1);
					entIdCollection.insert(entId);
					conIns = generator.createConObject(instituteId, instituteType);
					conInsCollection.insert(conIns);
					//任职关系
					attObj = generator.createObjObject(peoId, 208, instituteId, 2L, instituteType);
					attObjCollection.insert(attObj);
				} else {
					//原有机构id
					long type = getEntityType(instituteId);
					//任职关系
					if (relArr.length == 4) {
						String pos = relArr[2].replace("职位", "");
						String xy = relArr[3];
						long xyId = hasEntity(xy);
						if (xyId == -1) {
							xyId = ++id;
							basicInfo = generator.createBasicObject(xyId, xy, "");
							basicInfoCollection.insert(basicInfo);
							entId = generator.createEidObject(xy, xyId, 13L, 1);
							entIdCollection.insert(entId);
							conIns = generator.createConObject(xyId, 13L);
							conInsCollection.insert(conIns);
							attObj = generator.createObjObject(xyId, 1301, instituteId, 13L, 12L);
							attObjCollection.insert(attObj);
						}
						if (!pos.equals("")) {
							attObj = generator.createObjObject(peoId, 208, xyId, 
									2L, type,"attr_ext_208_1",pos);
							attObjCollection.insert(attObj);
						} else {
							attObj = generator.createObjObject(peoId, 208, xyId, 2L, type);
							attObjCollection.insert(attObj);
						}
					} else if (relArr.length == 2) {
						attObj = generator.createObjObject(peoId, 208, instituteId, 2L, type);
						attObjCollection.insert(attObj);
					}
				}
			} else {
				System.out.println(peo + "没有人物");
			}
		}
		br.close();
	}
	
	public static long getEntityType(long id) {
		long type = 0L;
		DBCollection collection = db.getCollection("entity_id");
		BasicDBObject searchObj = new BasicDBObject();
		searchObj.append("id", id);
		DBObject obj = collection.findOne(searchObj);
		type = (Long) obj.get("concept_id");
		return type;
	}
	
	public static void patentDataImport() throws IOException {
		String input = "";
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/bit_patent.txt");
		int count = 0;
		KgObjectGenerator generator = new KgObjectGenerator();
		long id = getId();
		Set<String> patentSet = new HashSet<String>();
		while ((input = br.readLine())!=null) {
			patentSet.add(input);
		}
		for (String string : patentSet) {
			System.out.println(count++);
			JSONObject obj = JSONObject.parseObject(string);
			JSONArray holdArr = obj.getJSONArray("holder");
			if (holdArr.size() > 2) continue;
			String patentName = obj.getString("name");
			long patentId = ++id;
			basicInfo = generator.createBasicObject(patentId, patentName, "");
			basicInfoCollection.insert(basicInfo);
			entId = generator.createEidObject(patentName, patentId, 3L, 1);
			entIdCollection.insert(entId);
			conIns = generator.createConObject(patentId, 3L);
			conInsCollection.insert(conIns);
			//专利简介
			String abs = obj.getString("abstracts");
			if (!abs.equals("")) {
				entAbs = generator.createAbsObject(patentId, abs);
				entAbsCollection.insert(entAbs);
			}
			//专利类型
			String type = "";
			if (obj.containsKey("type")) {
				type = obj.getString("type");
				attStr = generator.createStrObject(patentId, 304, type);
				attStrCollection.insert(attStr);
				attSum = generator.createSumObject(patentId, 304);
				attSumCollection.insert(attSum);
			}
			//专利申请时间
			String appTime = "";
			if (obj.containsKey("dateApplication")) {
				appTime = StringTimeUtils.formatStringTime(obj.getString("dateApplication")).substring(0,10);
				attStr = generator.createStrObject(patentId, 303, appTime);
				attStrCollection.insert(attStr);
				attSum = generator.createSumObject(patentId, 303);
				attSumCollection.insert(attSum);
			}
			//专利公开时间
			String publishTime = "";
			if (obj.containsKey("datePublication")) {
				publishTime = StringTimeUtils.formatStringTime(obj.getString("datePublication")).substring(0,10);
				attStr = generator.createStrObject(patentId, 316, publishTime);
				attStrCollection.insert(attStr);
				attSum = generator.createSumObject(patentId, 316);
				attSumCollection.insert(attSum);
			}
			//专利申请人
			JSONArray holderArr = obj.getJSONArray("holder");
			for (Object holObj : holderArr) {
				String holder = holObj.toString();
				long holderId = hasEntity(holder);
				if (holderId != -1) {
					//申请人专利关系
					long holderType = getEntityType(holderId);
					attObj = generator.createObjObject(patentId, 315, holderId, 3L, holderType);
					attObjCollection.insert(attObj);
				}
			}
			//专利作者
			JSONArray authorArr = obj.getJSONArray("inventor");
			for (Object autObj : authorArr) {
				String author = autObj.toString();
				long authorId = hasEntity(author);
				if (authorId == -1) {
					authorId = ++id;
					basicInfo = generator.createBasicObject(authorId, author, "");
					basicInfoCollection.insert(basicInfo);
					entId = generator.createEidObject(author, authorId, 2L, 1);
					entIdCollection.insert(entId);
					conIns = generator.createConObject(authorId, 2L);
					conInsCollection.insert(conIns);
					//专利人物关系
					if (publishTime.equals("") && appTime.equals("")) {
						attObj = generator.createObjObject(patentId, 306, authorId, 3L, 2L);
					} else if (publishTime.equals("")) {
						attObj = generator.createObjObject(patentId, 306, authorId, 3L, 2L, "attr_ext_306_1", appTime);
					} else if (appTime.equals("")) {
						attObj = generator.createObjObject(patentId, 306, authorId, 3L, 2L, "attr_ext_306_2", publishTime);
					} else {
						Map<String,Object> extValueMap = new HashMap<String,Object>();
						extValueMap.put("attr_ext_306_1", appTime);
						extValueMap.put("attr_ext_306_2", publishTime);
						attObj = generator.createObjObject(patentId, 306, authorId, 3L, 2L, extValueMap);
					}
					attObjCollection.insert(attObj);
					
				} else {
					//原有人物
					if (publishTime.equals("") && appTime.equals("")) {
						attObj = generator.createObjObject(patentId, 306, authorId, 3L, 2L);
					} else if (publishTime.equals("")) {
						attObj = generator.createObjObject(patentId, 306, authorId, 3L, 2L, "attr_ext_306_1", appTime);
					} else if (appTime.equals("")) {
						attObj = generator.createObjObject(patentId, 306, authorId, 3L, 2L, "attr_ext_306_2", publishTime);
					} else {
						Map<String,Object> extValueMap = new HashMap<String,Object>();
						extValueMap.put("attr_ext_306_1", appTime);
						extValueMap.put("attr_ext_306_2", publishTime);
						attObj = generator.createObjObject(patentId, 306, authorId, 3L, 2L, extValueMap);
					}
					attObjCollection.insert(attObj);
				}
			}
		}
		
		br.close();
	}
	
	public static void kesoDataImport() throws IOException {
		String input = "";
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/keso_new.txt");
		int count = 0;
		KgObjectGenerator generator = new KgObjectGenerator();
		long id = getId();
		while ((input = br.readLine())!=null) {
			System.out.println(count++);
			JSONObject obj = JSONObject.parseObject(input);
			if (obj.containsKey("name")) {
				String name = obj.getString("name");
				String pos = "";
				if (obj.containsKey("pos")) {
					pos = obj.getString("pos");
				}
				JSONArray paperArr = obj.getJSONArray("paper");
				//kesou中该人物有任职则该人物为北京理工大学人物
				if (!pos.equals("")) {
					if (!name.equals("")) {
						long authorId = hasEntity(name);
						if (authorId == -1) {
							//新增人物
							authorId = ++id;
							basicInfo = generator.createBasicObject(authorId, name, "");
							basicInfoCollection.insert(basicInfo);
							entId = generator.createEidObject(name, authorId, 2L, 1);
							entIdCollection.insert(entId);
							conIns = generator.createConObject(authorId, 2L);
							conInsCollection.insert(conIns);
						}
//						String pos = obj.getString("pos");
//						if (pos.equals("")) {
//							attObj = generator.createObjObject(authorId, 208, schoolId, 2L, 12L);
//							attObjCollection.insert(attObj);
//						} else {
//							attObj = generator.createObjObject(authorId, 208, schoolId, 2L, 12L, "attr_ext_208_1", pos);
//							attObjCollection.insert(attObj);
//						}
						//著作处理
						for (Object object : paperArr) {
							JSONObject pObj = (JSONObject) object;
							String paperName = pObj.getString("title_cn_s").trim();
							long paperId = hasEntity(paperName);
							if (paperId == -1) {
								//新增著作
								paperId = ++id;
								basicInfo = generator.createBasicObject(paperId, paperName, "");
								basicInfoCollection.insert(basicInfo);
								entId = generator.createEidObject(paperName, paperId, 9L, 1);
								entIdCollection.insert(entId);
								conIns = generator.createConObject(paperId, 9L);
								conInsCollection.insert(conIns);
								//著作时间
								String publishTime = pObj.getString("year_if");
								if (publishTime.length() == 4) {
									publishTime = publishTime + "-01-01";
								}
								attStr = generator.createStrObject(paperId, 906, publishTime);
								attStrCollection.insert(attStr);
								attSum = generator.createSumObject(paperId, 906);
								attSumCollection.insert(attSum);
								//著作关键词
								JSONArray keywordArr = pObj.getJSONArray("keywords");
								for (Object kwObj : keywordArr) {
									String keyword = kwObj.toString();
									if (!keyword.equals("")) {
										long keywordId = hasEntity(keyword);
										if (keywordId == -1) {
											//新增关键词
											keywordId = ++id;
											basicInfo = generator.createBasicObject(keywordId, keyword, 0,"");
											basicInfoCollection.insert(basicInfo);
											entId = generator.createEidObject(keyword, keywordId, 14L, 0);
											entIdCollection.insert(entId);
											conIns = generator.createConObject(keywordId, 14L);
											conInsCollection.insert(conIns);
											//关键词与著作关系
											attObj = generator.createObjObject(paperId, 907, keywordId, 9L, 14L);
											attObjCollection.insert(attObj);
										} else {
											attObj = generator.createObjObject(paperId, 907, keywordId, 9L, 14L);
											attObjCollection.insert(attObj);
										}
									}
								}
								//著作作者
								String[] authorArr = pObj.getString("authors_ims").split(",");
								for (String author : authorArr) {
									if (author.equals("name")) continue;
									authorId = hasEntity(author);
									if (authorId == -1) {
										//新增人物
//										authorId = ++id;
//										basicInfo = generator.createBasicObject(authorId, author, "");
//										basicInfoCollection.insert(basicInfo);
//										entId = generator.createEidObject(author, authorId, 2L, 1);
//										entIdCollection.insert(entId);
//										conIns = generator.createConObject(paperId, 2L);
//										conInsCollection.insert(conIns);
//										//著作人物关系
//										attObj = generator.createObjObject(paperId, 905, authorId, 9L, 2L);
//										attObjCollection.insert(attObj);
									} else {
										//原有人物
										//著作人物关系
										if (publishTime.equals("")) {
											attObj = generator.createObjObject(paperId, 905, authorId, 9L, 2L);
										} else {
											attObj = generator.createObjObject(paperId, 905, authorId, 9L, 2L,"attr_ext_905_1",publishTime);
										}
										attObjCollection.insert(attObj);
									}
								}
							} 
						}
					}
				} else {
					//不是北京理工的人物
//					if (otherPeopleMap.containsKey(name)) {
//						long authorId = hasEntity(name);
//						String institute = otherPeopleMap.get(name);
//						long instituteId = hasEntity(institute);
//						if (instituteId != -1) {
//							attObj = generator.createObjObject(authorId, 208, instituteId, 2L, 12L, "attr_ext_208_1", institute);
//							attObjCollection.insert(attObj);
//						} else {
//							instituteId = ++id;
//							basicInfo = generator.createBasicObject(instituteId, institute, "");
//							basicInfoCollection.insert(basicInfo);
//							entId = generator.createEidObject(institute, instituteId, 12L, 1);
//							entIdCollection.insert(entId);
//							conIns = generator.createConObject(instituteId, 12L);
//							conInsCollection.insert(conIns);
//							attObj = generator.createObjObject(authorId, 208, instituteId, 2L, 12L, "attr_ext_208_1", institute);
//							attObjCollection.insert(attObj);
//							System.out.println(name + institute + instituteId);
//						}
//					}
				}
			}
		}
		br.close();
	}
	
	public static void kesoAllDataImport() throws IOException {
		String input = "";
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/keso_new_all.txt");
		int count = 0;
		KgObjectGenerator generator = new KgObjectGenerator();
		long id = getId();
		while ((input = br.readLine())!=null) {
			System.out.println(count++);
			JSONObject obj = JSONObject.parseObject(input);
			if (obj.containsKey("name")) {
				String name = obj.getString("name");
				JSONArray paperArr = obj.getJSONArray("paper");
				//都是已确认的北理人物
				if (!name.equals("")) {
					long authorId = hasEntity(name);
					if (authorId == -1) {
						//新增人物
						authorId = ++id;
						basicInfo = generator.createBasicObject(authorId, name, "");
						basicInfoCollection.insert(basicInfo);
						entId = generator.createEidObject(name, authorId, 2L, 1);
						entIdCollection.insert(entId);
						conIns = generator.createConObject(authorId, 2L);
						conInsCollection.insert(conIns);
					}
//					String pos = obj.getString("pos");
//					if (pos.equals("")) {
//						attObj = generator.createObjObject(authorId, 208, schoolId, 2L, 12L);
//						attObjCollection.insert(attObj);
//					} else {
//						attObj = generator.createObjObject(authorId, 208, schoolId, 2L, 12L, "attr_ext_208_1", pos);
//						attObjCollection.insert(attObj);
//					}
					//著作处理
					for (Object object : paperArr) {
						JSONObject pObj = (JSONObject) object;
						String paperName = pObj.getString("title_cn_s").trim();
						long paperId = hasEntity(paperName);
						if (paperId == -1) {
							//新增著作
							paperId = ++id;
							basicInfo = generator.createBasicObject(paperId, paperName, "");
							basicInfoCollection.insert(basicInfo);
							entId = generator.createEidObject(paperName, paperId, 9L, 1);
							entIdCollection.insert(entId);
							conIns = generator.createConObject(paperId, 9L);
							conInsCollection.insert(conIns);
							//著作时间
							String publishTime = pObj.getString("year_if");
							if (publishTime.length() == 4) {
								publishTime = publishTime + "-01-01";
							}
							attStr = generator.createStrObject(paperId, 906, publishTime);
							attStrCollection.insert(attStr);
							attSum = generator.createSumObject(paperId, 906);
							attSumCollection.insert(attSum);
							//著作关键词
							JSONArray keywordArr = pObj.getJSONArray("keywords");
							for (Object kwObj : keywordArr) {
								String keyword = kwObj.toString();
								if (!keyword.equals("")) {
									long keywordId = hasEntity(keyword);
									if (keywordId == -1) {
										//新增关键词
										keywordId = ++id;
										basicInfo = generator.createBasicObject(keywordId, keyword, 0,"");
										basicInfoCollection.insert(basicInfo);
										entId = generator.createEidObject(keyword, keywordId, 14L, 0);
										entIdCollection.insert(entId);
										conIns = generator.createConObject(keywordId, 14L);
										conInsCollection.insert(conIns);
										//关键词与著作关系
										attObj = generator.createObjObject(paperId, 907, keywordId, 9L, 14L);
										attObjCollection.insert(attObj);
									} else {
										attObj = generator.createObjObject(paperId, 907, keywordId, 9L, 14L);
										attObjCollection.insert(attObj);
									}
								}
							}
							//著作作者
							String[] authorArr = pObj.getString("authors_ims").split(",");
							for (String author : authorArr) {
								if (author.equals("name")) continue;
								authorId = hasEntity(author);
								if (authorId == -1) {
									//新增人物
//									authorId = ++id;
//									basicInfo = generator.createBasicObject(authorId, author, "");
//									basicInfoCollection.insert(basicInfo);
//									entId = generator.createEidObject(author, authorId, 2L, 1);
//									entIdCollection.insert(entId);
//									conIns = generator.createConObject(paperId, 2L);
//									conInsCollection.insert(conIns);
//									//著作人物关系
//									attObj = generator.createObjObject(paperId, 905, authorId, 9L, 2L);
//									attObjCollection.insert(attObj);
								} else {
									//原有人物
									//著作人物关系
									if (!publishTime.equals("")) {
										attObj = generator.createObjObject(paperId, 905, authorId, 9L, 2L, "attr_ext_905_1", publishTime);
									} else {
										attObj = generator.createObjObject(paperId, 905, authorId, 9L, 2L);
									}
									attObjCollection.insert(attObj);
								}
							}
						} 
					}
				}
			}
		}
		br.close();
	}
	
	public static void cnkiDataImport() throws IOException {
		String input = "";
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/bit_p.txt");
		int count = 0;
		KgObjectGenerator generator = new KgObjectGenerator();
		long id = 1000000;
		long schoolId = ++id;
		basicInfo = generator.createBasicObject(schoolId, "北京理工大学", "");
		basicInfoCollection.insert(basicInfo);
		entId = generator.createEidObject("北京理工大学", schoolId, 12L, 1);
		entIdCollection.insert(entId);
		conIns = generator.createConObject(schoolId, 12L);
		conInsCollection.insert(conIns);
		while((input = br.readLine())!=null) {
			System.out.println(count++);
			JSONObject obj = JSONObject.parseObject(input);
			JSONArray insArr = obj.getJSONArray("institute");
			if (insArr.size() > 2) continue;
			String paperName = "";
			if (obj.containsKey("title")) {
				paperName = obj.getString("title");
			}
			
			if (!paperName.equals("")) {
				//新增著作实体
				long paperId = ++id;
				basicInfo = generator.createBasicObject(paperId, paperName, "");
				basicInfoCollection.insert(basicInfo);
				entId = generator.createEidObject(paperName, paperId, 9L, 1);
				entIdCollection.insert(entId);
				conIns = generator.createConObject(paperId, 9L);
				conInsCollection.insert(conIns);
				//著作简介
				String abs = ""; 
				if (obj.containsKey("abstracts")) {
					abs = obj.getString("abstracts");
					if (!abs.equals("")) {
						entAbs = generator.createAbsObject(paperId, abs);
						entAbsCollection.insert(entAbs);
					}
				}
				//著作类型
				String type = "";
				if (obj.containsKey("source")) {
					type = obj.getString("source");
					attStr = generator.createStrObject(paperId, 904, type);
					attStrCollection.insert(attStr);
					attSum = generator.createSumObject(paperId, 904);
					attSumCollection.insert(attSum);
				}
				//著作时间
				String publishTime = "";
				if (obj.containsKey("publishTime")) {
					try {
						publishTime = StringTimeUtils.formatStringTime(obj.getString("publishTime")).substring(0,10);
					} catch (Exception e) {
						System.out.println(paperId + "time error");
					}
					if (!publishTime.equals("")) {
						attStr = generator.createStrObject(paperId, 906, publishTime);
						attStrCollection.insert(attStr);
						attSum = generator.createSumObject(paperId, 906);
						attSumCollection.insert(attSum);
					}
				}
				//著作作者
				if (obj.containsKey("author")) {
					JSONArray authorArr = obj.getJSONArray("author");
					for (Object authorObj : authorArr) {
						String author = authorObj.toString().trim();
						long authorId = hasEntity(author);
						if (authorId == -1) {
							//新增人物
							authorId = ++id;
							basicInfo = generator.createBasicObject(authorId, author, "");
							basicInfoCollection.insert(basicInfo);
							entId = generator.createEidObject(author, authorId, 2L, 1);
							entIdCollection.insert(entId);
							conIns = generator.createConObject(authorId, 2L);
							conInsCollection.insert(conIns);
							//著作人物关系
							if (!publishTime.equals("")) {
								attObj = generator.createObjObject(paperId, 905, authorId, 9L, 2L, "attr_ext_905_1", publishTime);
							} else {
								attObj = generator.createObjObject(paperId, 905, authorId, 9L, 2L);
							}
							attObjCollection.insert(attObj);
						} else {
							//原有人物与著作关系
							if (!publishTime.equals("")) {
								attObj = generator.createObjObject(paperId, 905, authorId, 9L, 2L, "attr_ext_905_1", publishTime);
							} else {
								attObj = generator.createObjObject(paperId, 905, authorId, 9L, 2L);
							}
							attObjCollection.insert(attObj);
						}
					}
				}
				
				//著作关键词
				if (obj.containsKey("keywords")) {
					JSONArray keywordArr = obj.getJSONArray("keywords");
					for (Object keywordObj : keywordArr) {
						String keyword = keywordObj.toString().trim();
						long keywordId = hasEntity(keyword);
						if (keywordId == -1) {
							//新增关键词实体
							keywordId = ++id;
							basicInfo = generator.createBasicObject(keywordId, keyword, 0,"");
							basicInfoCollection.insert(basicInfo);
							entId = generator.createEidObject(keyword, keywordId, 14L, 0);
							entIdCollection.insert(entId);
							conIns = generator.createConObject(keywordId, 14L);
							conInsCollection.insert(conIns);
							//关键词与著作关系
							attObj = generator.createObjObject(paperId, 907, keywordId, 9L, 14L);
							attObjCollection.insert(attObj);
						} else {
							//原有关键词
							attObj = generator.createObjObject(paperId, 907, keywordId, 9L, 14L);
							attObjCollection.insert(attObj);
						}
					}
				}
			}
		}
		br.close();
	}
	
	
	
	public static Long getId() {
		long id = 0;
		DBCollection biDbc = db.getCollection("basic_info");
		DBCursor biCursor = biDbc.find().sort(new BasicDBObject("_id", -1)).limit(1);
		if(biCursor.hasNext()){
			id = Long.parseLong((biCursor.next().get("_id").toString()));
		}
		biCursor.close();
		return id;
	}
	
	public static void createBasic() throws Exception {
		DBCollection basicCollection = db.getCollection("basic_info");
		DBCollection entIdCollection = db.getCollection("entity_id");
		KgObjectGenerator generator = new KgObjectGenerator();
		List<DBObject> basicInfoList = new ArrayList<DBObject>();
		List<DBObject> entIdList = new ArrayList<DBObject>();
		DBObject entId = new BasicDBObject();
		DBObject obj1 = new BasicDBObject("_id",0L)
										.append("name", "知识图谱")
										.append("type", 0);
		basicInfoList.add(obj1);
		entId = generator.createEidObject("知识图谱", 0L, 0L, 0);
		entIdList.add(entId);
		DBObject obj2 = new BasicDBObject("_id",1L)
										.append("name", "公司")
										.append("type", 0);
		basicInfoList.add(obj2);
		entId = generator.createEidObject("公司", 1L, 10L, 0);
		entIdList.add(entId);
		DBObject obj3 = new BasicDBObject("_id",2L)
										.append("name", "人物")
										.append("type", 0);
		basicInfoList.add(obj3);
		entId = generator.createEidObject("人物", 2L, 0L, 0);
		entIdList.add(entId);
		DBObject obj4 = new BasicDBObject("_id",3L)
										.append("name", "专利")
										.append("type", 0);
		basicInfoList.add(obj4);
		entId = generator.createEidObject("专利", 3L, 0L, 0);
		entIdList.add(entId);
		DBObject obj5 = new BasicDBObject("_id",9L)
										.append("name", "著作")
										.append("type", 0);
		basicInfoList.add(obj5);
		entId = generator.createEidObject("著作", 9L, 0L, 0);
		entIdList.add(entId);
		DBObject obj6 = new BasicDBObject("_id",10L)
										.append("name", "机构")
										.append("type", 0);
		basicInfoList.add(obj6);
		entId = generator.createEidObject("机构", 10L, 0L, 0);
		entIdList.add(entId);
		DBObject obj7 = new BasicDBObject("_id",11L)
										.append("name", "企业")
										.append("type", 0);
		basicInfoList.add(obj7);
		entId = generator.createEidObject("企业", 11L, 1L, 0);
		entIdList.add(entId);
		DBObject obj8 = new BasicDBObject("_id",12L)
										.append("name", "学校")
										.append("type", 0);
		basicInfoList.add(obj8);
		entId = generator.createEidObject("学校", 12L, 10L, 0);
		entIdList.add(entId);
		DBObject obj15 = new BasicDBObject("_id",13L)
		.append("name", "学院")
		.append("type", 0);
		basicInfoList.add(obj15);
		entId = generator.createEidObject("学院", 13L, 13L, 0);
		entIdList.add(entId);
		DBObject obj9 = new BasicDBObject("_id",14L)
										.append("name", "标签")
										.append("type", 0);
		basicInfoList.add(obj9);
		entId = generator.createEidObject("标签", 14L, 0L, 0);
		entIdList.add(entId);
		DBObject obj10 = new BasicDBObject("_id",15L)
		.append("name", "其他机构")
		.append("type", 0);
		basicInfoList.add(obj10);
		entId = generator.createEidObject("其他机构", 15L, 10L, 0);
		entIdList.add(entId);
		DBObject obj11 = new BasicDBObject("_id",20L)
										.append("name", "事件")
										.append("type", 0);
		basicInfoList.add(obj11);
		entId = generator.createEidObject("事件", 20L, 0L, 0);
		entIdList.add(entId);
		DBObject obj12 = new BasicDBObject("_id",21L)
										.append("name", "任职事件")
										.append("type", 0);
		basicInfoList.add(obj12);
		entId = generator.createEidObject("任职事件", 21L, 20L, 0);
		entIdList.add(entId);
		DBObject obj13 = new BasicDBObject("_id",22L)
										.append("name", "个人履历")
										.append("type", 0);
		basicInfoList.add(obj13);
		entId = generator.createEidObject("个人履历", 22L, 20L, 0);
		entIdList.add(entId);
		basicCollection.insert(basicInfoList);
		entIdCollection.insert(entIdList);
		System.out.println("insert finish");
	}
	
	public static void createIndex() {
		extCollection.createIndex(new BasicDBObject("_id",1), "_id_");
		extCollection.createIndex(new BasicDBObject("triple_id",1), "triple_id_1");
		extCollection.createIndex(new BasicDBObject("ext_id",1), "ext_id_1");
		basicInfoCollection.createIndex(new BasicDBObject("_id",1),"_id_");
		basicInfoCollection.createIndex(new BasicDBObject("name",1),"name_1");
		basicInfoCollection.createIndex(new BasicDBObject("type",1),"type_1");
		attObjCollection.createIndex(new BasicDBObject("_id",1), "_id_");
		attObjCollection.createIndex(new BasicDBObject("entity_id",1), "entity_id_1");
		attObjCollection.createIndex(new BasicDBObject("attr_id",1), "attr_id_1");
		attObjCollection.createIndex(new BasicDBObject("attr_value",1), "attr_value_1");
		attObjCollection.createIndex(new BasicDBObject("entity_type",1), "entity_type_1");
		attObjCollection.createIndex(new BasicDBObject("attr_value_type",1), "attr_value_type_1");
		attStrCollection.createIndex(new BasicDBObject("_id",1),"_id_");
		attStrCollection.createIndex(new BasicDBObject("entity_id",1),"entity_id_1");
		attStrCollection.createIndex(new BasicDBObject("attr_id",1),"attr_id_1");
		attSumCollection.createIndex(new BasicDBObject("_id",1),"_id_");
		attSumCollection.createIndex(new BasicDBObject("entity_id",1),"entity_id_1");
		attSumCollection.createIndex(new BasicDBObject("attr_id",1),"attr_id_1");
		conInsCollection.createIndex(new BasicDBObject("_id",1),"_id_");
		conInsCollection.createIndex(new BasicDBObject("concept_id",1),"concept_id_1");
		conInsCollection.createIndex(new BasicDBObject("ins_id",1),"ins_id_1");
		entAbsCollection.createIndex(new BasicDBObject("_id",1),"_id_");
		entAbsCollection.createIndex(new BasicDBObject("id",1),"id_1");
		entIdCollection.createIndex(new BasicDBObject("_id",1),"_id_");
		entIdCollection.createIndex(new BasicDBObject("id",1),"id_1");
		entIdCollection.createIndex(new BasicDBObject("concept_id",1),"concept_id_1");
	}
	
	public static void createParSon() throws Exception {
		DBCollection parSonCollection = db.getCollection("parent_son");
		KgObjectGenerator generator = new KgObjectGenerator();
		List<DBObject> parSonList = new ArrayList<DBObject>();
		DBObject parSon = new BasicDBObject();
		
		parSon = generator.createParSonObject(0L, 14L);
		parSonList.add(parSon);
		parSon = generator.createParSonObject(0L, 3L);
		parSonList.add(parSon);
		parSon = generator.createParSonObject(0L, 10L);
		parSonList.add(parSon);
		parSon = generator.createParSonObject(0L, 2L);
		parSonList.add(parSon);
		parSon = generator.createParSonObject(0L, 9L);
		parSonList.add(parSon);
		parSon = generator.createParSonObject(0L, 20L);
		parSonList.add(parSon);
		parSon = generator.createParSonObject(10L, 1L);
		parSonList.add(parSon);
		parSon = generator.createParSonObject(10L, 12L);
		parSonList.add(parSon);
		parSon = generator.createParSonObject(10L, 15L);
		parSonList.add(parSon);
		parSon = generator.createParSonObject(12L, 13L);
		parSonList.add(parSon);
		parSon = generator.createParSonObject(1L, 11L);
		parSonList.add(parSon);
		parSon = generator.createParSonObject(20L, 21L);
		parSonList.add(parSon);
		parSon = generator.createParSonObject(20L, 22L);
		parSonList.add(parSon);
		
		parSonCollection.insert(parSonList);
		System.out.println("parent insert success");
	}
	
	private static long hasEntity(String entityName) {
		String entityNameA = entityName.replace("（", "(").replace("）", ")");
		long id = -1;
		DBCollection collection = db.getCollection("basic_info");
		BasicDBObject searchObj = new BasicDBObject();
		searchObj.append("name", entityNameA.trim());
		DBCursor cursor = collection.find(searchObj);
		try {
			while (cursor.hasNext()) {
				DBObject obj = cursor.next();
				id = (Long) obj.get("_id");
				return id;
			}
		} catch (Exception e) {
			cursor.close();
		} finally {
			cursor.close();
		}
		
		searchObj.append("name", entityName);
		cursor = collection.find(searchObj);
		try {
			while (cursor.hasNext()) {
				DBObject obj = cursor.next();
				id = (Long) obj.get("_id");
				return id;
			}
		} catch (Exception e) {
			cursor.close();
		} finally {
			cursor.close();
		}
		return id;
	}
	
	public static Map<String,String> getOtherMap() throws IOException {
		Map<String,String> map = new HashMap<String,String>();
		BufferedReader br = BufferedReaderUtil.getBuffer("data/cnki/bit_p.txt");
		String input = "";
		while ((input = br.readLine())!=null) {
			JSONObject obj = JSONObject.parseObject(input);
			JSONArray instArr = obj.getJSONArray("institute");
			String othIns = "";
			if (instArr.size() == 2) {
				if (instArr.get(0).toString().contains("北京理工")) {
					othIns = instArr.getString(1);
				} else {
					othIns = instArr.getString(0);
				}
				JSONArray authArr = obj.getJSONArray("author");
				for (Object author : authArr) {
					map.put(author.toString(), othIns);
				}
			}
		}
		br.close();
		return map;
	}
}
