package com.hiekn.bean;

import java.util.ArrayList;

public class CompanyBean {

	/**
	 * 企信宝唯一id
	 */
	private String eid;
	
	/**
	 * 公司名称
	 */
	private String name;
	
	/**
	 * 联系方式
	 */
	private String contactInfo;
	
	/**
	 * 地址
	 */
	private String address;
	
	/**
	 * 统一社会信用代码
	 */
	private String creditCode;
	
	/**
	 * 注册号
	 */
	private String registrationMark;
	
	/**
	 * 组织机构代码
	 */
	private String organizationCode;
	
	/**
	 * 公司类型
	 */
	private String type;
	
	/**
	 * 经营状态
	 */
	private String businessStatus;
	
	/**
	 * 法定代表人
	 */
	private String legalRepresentative;
	
	/**
	 * 成立日期
	 */
	private String dateFounded;
	
	/**
	 * 营业期限
	 */
	private String businessTerm;
	
	/**
	 * 注册资本
	 */
	private String registeredCapital;
	
	/**
	 * 发照日期
	 */
	private String dateIssued;
	
	/**
	 * 登记机关
	 */
	private String registrationAuthority;
	
	/**
	 * 经营范围
	 */
	private String businessScope;
	
	/**
	 * 主要人员
	 */
	private ArrayList<String> mainPeople;
	
	public ArrayList<String> getMainPeople() {
		return mainPeople;
	}

	public void setMainPeople(ArrayList<String> mainPeople) {
		this.mainPeople = mainPeople;
	}

	public String getEid() {
		return eid;
	}

	public void setEid(String eid) {
		this.eid = eid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactInfo() {
		return contactInfo;
	}

	public void setContactInfo(String contactInfo) {
		this.contactInfo = contactInfo;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCreditCode() {
		return creditCode;
	}

	public void setCreditCode(String creditCode) {
		this.creditCode = creditCode;
	}

	public String getRegistrationMark() {
		return registrationMark;
	}

	public void setRegistrationMark(String registrationMark) {
		this.registrationMark = registrationMark;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBusinessStatus() {
		return businessStatus;
	}

	public void setBusinessStatus(String bussinessStatus) {
		this.businessStatus = bussinessStatus;
	}

	public String getLegalRepresentative() {
		return legalRepresentative;
	}

	public void setLegalRepresentative(String legalRepresentative) {
		this.legalRepresentative = legalRepresentative;
	}

	public String getDateFounded() {
		return dateFounded;
	}

	public void setDateFounded(String dateFounded) {
		this.dateFounded = dateFounded;
	}

	public String getBusinessTerm() {
		return businessTerm;
	}

	public void setBusinessTerm(String businessTerm) {
		this.businessTerm = businessTerm;
	}

	public String getRegisteredCapital() {
		return registeredCapital;
	}

	public void setRegisteredCapital(String registeredCapital) {
		this.registeredCapital = registeredCapital;
	}

	public String getDateIssued() {
		return dateIssued;
	}

	public void setDateIssued(String dateIssued) {
		this.dateIssued = dateIssued;
	}

	public String getRegistrationAuthority() {
		return registrationAuthority;
	}

	public void setRegistrationAuthority(String registrationAuthority) {
		this.registrationAuthority = registrationAuthority;
	}

	public String getBusinessScope() {
		return businessScope;
	}

	public void setBusinessScope(String businessScope) {
		this.businessScope = businessScope;
	}
	
}
