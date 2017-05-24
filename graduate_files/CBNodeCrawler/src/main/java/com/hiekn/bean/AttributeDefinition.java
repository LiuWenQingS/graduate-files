package com.hiekn.bean;

import java.util.List;

/**
 * @author xiaohuqi E-mail:xiaohuqi@126.com
 * @version 2014-6-11
 * 
 */
public class AttributeDefinition implements java.io.Serializable {
	private static final long serialVersionUID = 8347376159343324248L;
	
	/**
	 * id
	 */
	private int id;
	
	/**
	 * 名称
	 */
	private String name; 
	
	/**
	 * 别名
	 */
	private String alias;
	
	/**
	 * 类型
	 */
	private String type;
	
	/**
	 * 定义域
	 */
	private String domain;
	
	/**
	 * 值域
	 */
	private String range;
	
	/**
	 * 数据类型
	 */
	private int dataType;
	
	/**
	 * 单位
	 */
	private String dataUnit;
	
	/**
	 * 是否具备唯一值
	 */
	private int isFunctional;
	
	/**
	 * 额外信息
	 */
	private List<AttributeExtraInfoItem> extraInfoList;
	private int tableAlone;
	private int joinSeqNo;
	
	/**
	 * 特定大于操作描述符
	 */
	private String gtRangeOperator;
	
	/**
	 * 特定小于操作描述符
	 */
	private String ltRangeOperator;
	
	/**
	 * 特定模糊操作描述符
	 */
	private String fuzzyOperator;
	
	/**
	 * 特定最大值操作描述符
	 */
	private String gtMostOperator;
	
	/**
	 * 特定最小值操作描述符
	 */
	private String ltMostOperator;
	
	/**
	 * 特定独立最大值操作描述符
	 */
	private String gtSingleMostOperator;
	
	/**
	 * 特定最小值操作描述符
	 */
	private String ltSingleMostOperator;
	
	/**
	 * 特定最值操作描述符的单位
	 */
	private String mostOperatorUnit;
	
	/**
	 * 编辑提示
	 */
	private String editTip;
	
	/**
	 * 显示次序
	 */
	private int seqNo;
	
	/**
	 * 创建人
	 */
	private String creator;
	
	/**
	 * 创建时间
	 */
	private String createTime;
	
	/**
	 * 最后修改人
	 */
	private String modifier;
	
	/**
	 * 最后修改时间
	 */
	private String modifyTime;
	private String status;
	
	public String getAlias() {
		return alias;
	}
	public void setAlias(String alias) {
		this.alias = alias;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getModifier() {
		return modifier;
	}
	public void setModifier(String modifier) {
		this.modifier = modifier;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRange() {
		return range;
	}
	public void setRange(String range) {
		this.range = range;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
	public int getDataType() {
		return dataType;
	}
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	public String getDataUnit() {
		return dataUnit;
	}
	public void setDataUnit(String dataUnit) {
		this.dataUnit = dataUnit;
	}
	public int getIsFunctional() {
		return isFunctional;
	}
	public void setIsFunctional(int isFunctional) {
		this.isFunctional = isFunctional;
	}
	public String getEditTip() {
		return editTip;
	}
	public void setEditTip(String editTip) {
		this.editTip = editTip;
	}
	public String getFuzzyOperator() {
		return fuzzyOperator;
	}
	public void setFuzzyOperator(String fuzzyOperator) {
		this.fuzzyOperator = fuzzyOperator;
	}
	public String getGtMostOperator() {
		return gtMostOperator;
	}
	public void setGtMostOperator(String gtMostOperator) {
		this.gtMostOperator = gtMostOperator;
	}
	public String getGtRangeOperator() {
		return gtRangeOperator;
	}
	public void setGtRangeOperator(String gtRangeOperator) {
		this.gtRangeOperator = gtRangeOperator;
	}
	public String getGtSingleMostOperator() {
		return gtSingleMostOperator;
	}
	public void setGtSingleMostOperator(String gtSingleMostOperator) {
		this.gtSingleMostOperator = gtSingleMostOperator;
	}
	public String getLtMostOperator() {
		return ltMostOperator;
	}
	public void setLtMostOperator(String ltMostOperator) {
		this.ltMostOperator = ltMostOperator;
	}
	public String getLtRangeOperator() {
		return ltRangeOperator;
	}
	public void setLtRangeOperator(String ltRangeOperator) {
		this.ltRangeOperator = ltRangeOperator;
	}
	public String getLtSingleMostOperator() {
		return ltSingleMostOperator;
	}
	public void setLtSingleMostOperator(String ltSingleMostOperator) {
		this.ltSingleMostOperator = ltSingleMostOperator;
	}
	public String getMostOperatorUnit() {
		return mostOperatorUnit;
	}
	public void setMostOperatorUnit(String mostOperatorUnit) {
		this.mostOperatorUnit = mostOperatorUnit;
	}
	public List<AttributeExtraInfoItem> getExtraInfoList() {
		return extraInfoList;
	}
	public void setExtraInfoList(List<AttributeExtraInfoItem> extraInfoList) {
		this.extraInfoList = extraInfoList;
	}
	public int getJoinSeqNo() {
		return joinSeqNo;
	}
	public void setJoinSeqNo(int joinSeqNo) {
		this.joinSeqNo = joinSeqNo;
	}
	public int getTableAlone() {
		return tableAlone;
	}
	public void setTableAlone(int tableAlone) {
		this.tableAlone = tableAlone;
	}
}
