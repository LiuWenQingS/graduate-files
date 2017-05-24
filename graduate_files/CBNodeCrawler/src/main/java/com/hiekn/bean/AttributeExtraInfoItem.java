package com.hiekn.bean;
/**
 * @author xiaohuqi E-mail:xiaohuqi@126.com
 * @version 2015-6-24 上午10:02:49
 * 
 */
public class AttributeExtraInfoItem implements java.io.Serializable {
	private static final long serialVersionUID = -3383785867548196236L;

	private int seqNo;
	private String name;
	private int dataType;
	
	public int getDataType() {
		return dataType;
	}
	public void setDataType(int dataType) {
		this.dataType = dataType;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSeqNo() {
		return seqNo;
	}
	public void setSeqNo(int seqNo) {
		this.seqNo = seqNo;
	}
}
