package com.hiekn.util;

import java.util.Map;
import java.util.Map.Entry;

import com.mongodb.BasicDBObject;

public class KgObjectGenerator {
	public BasicDBObject createBasicObject(long id,String name,String meaningtag) {
		BasicDBObject basicInfo = null;
		name = name.trim();
		name = nameFormat(name);
		if (meaningtag.equals("")) {
			basicInfo = new BasicDBObject("_id",id)
							.append("name", name)
							.append("type", 1);
		} else {
			basicInfo = new BasicDBObject("_id",id)
							.append("name", name)
							.append("type", 1)
							.append("meaning_tag", meaningtag);
		}
		return basicInfo;
	}
	
	public BasicDBObject createBasicObject(long id,String name,int type,String meaningtag) {
		BasicDBObject basicInfo = null;
		if (meaningtag.equals("")) {
			basicInfo = new BasicDBObject("_id",id)
			.append("name", name)
			.append("type", type);
		} else {
			basicInfo = new BasicDBObject("_id",id)
			.append("name", name)
			.append("type", type)
			.append("meaning_tag", meaningtag);
		}
		return basicInfo;
	}
	
	public BasicDBObject createBasicObject(long id,String name,int type,String meaningtag,Map<String,String> sourceMap) {
		BasicDBObject basicInfo = null;
		if (meaningtag.equals("")) {
			basicInfo = new BasicDBObject("_id",id)
			.append("name", name)
			.append("type", type)
			.append("source", sourceMap);
		} else {
			basicInfo = new BasicDBObject("_id",id)
			.append("name", name)
			.append("type", type)
			.append("meaning_tag", meaningtag)
			.append("source",sourceMap);
		}
		return basicInfo;
	}
	
	public BasicDBObject createConObject(long id,long concept) {
		BasicDBObject conIns = new BasicDBObject("concept_id",concept)
									.append("ins_id", id);
		return conIns;
	}
	
	public BasicDBObject createEidObject(String name,long id,long concept,int type) {
		BasicDBObject entityId = new BasicDBObject("name",name)
									.append("id", id)
									.append("concept_id", concept)
									.append("type",type);
		return entityId;
	}
	
	public BasicDBObject createAbsObject(long id,String abs) {
		BasicDBObject absObj = new BasicDBObject("id",id)
									.append("abstract", abs);
		return absObj;
	}
	
	public BasicDBObject createObjObject(long entityId,int attrId,long attrValue,long entityType,long attrValueType) {
		BasicDBObject objObj = new BasicDBObject("entity_id",entityId)
									.append("attr_id", attrId)
									.append("attr_value", attrValue)
									.append("entity_type", entityType)
									.append("attr_value_type",attrValueType);
		return objObj;
	}
	
	public BasicDBObject createObjObject(long entityId,int attrId,long attrValue,long entityType,long attrValueType,String attExt, String attExtValue) {
		BasicDBObject objObj = new BasicDBObject("entity_id",entityId)
		.append("attr_id", attrId)
		.append("attr_value", attrValue)
		.append("entity_type", entityType)
		.append("attr_value_type",attrValueType)
		.append(attExt, attExtValue);
		return objObj;
	}
	
	public BasicDBObject createObjObject(long entityId,int attrId,long attrValue,long entityType,long attrValueType, Map<String,Object> extValueMap) {
		BasicDBObject objObj = new BasicDBObject("entity_id",entityId)
		.append("attr_id", attrId)
		.append("attr_value", attrValue)
		.append("entity_type", entityType)
		.append("attr_value_type",attrValueType);
		for (Entry<String,Object> entry : extValueMap.entrySet()) {
			objObj.append(entry.getKey(), entry.getValue());
		}
		return objObj;
	}
	
	public BasicDBObject createStrObject(long entityId,int attrId,String attrValue) {
		BasicDBObject strObj = new BasicDBObject("entity_id",entityId)
									.append("attr_id", attrId)
									.append("attr_value", attrValue);
		return strObj;
	}
	
	public BasicDBObject createSumObject(long entityId,int attrId) {
		BasicDBObject sumObj = new BasicDBObject("entity_id",entityId)
		.append("attr_id", attrId);
		return sumObj;
	}
	
	public BasicDBObject createExtObject(String tripleId,long extId) {
		BasicDBObject extObj = new BasicDBObject("triple_id",tripleId)
									.append("ext_id", extId);
		return extObj;
	}
	
	public BasicDBObject createParSonObject(long parent,long son) {
		BasicDBObject parSonObj = new BasicDBObject("parent",parent)
		.append("son", son);
		return parSonObj;
	}
	
	public String nameFormat(String targetStr) {
		String suffixTrimStr = "";
		String prefixTrimStr = "";
		if (targetStr.charAt(targetStr.length()-1) == '　') {
			for (int i = 0; i < targetStr.length()-1; i++) {
				suffixTrimStr = suffixTrimStr + targetStr.charAt(i);
			}
		} else {
			suffixTrimStr = targetStr;
		}
		if (!suffixTrimStr.equals("")) {
			if (suffixTrimStr.charAt(0) == '　') {
				for (int i = 1; i < suffixTrimStr.length(); i++) {
					prefixTrimStr = prefixTrimStr + suffixTrimStr.charAt(i);
				}
			} else {
				return suffixTrimStr;
			}
		}
		return prefixTrimStr;
	}
}
