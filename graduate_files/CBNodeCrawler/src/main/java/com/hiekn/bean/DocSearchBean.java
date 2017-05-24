package com.hiekn.bean;

public class DocSearchBean extends SearchBean {
	private String dbPrefix = "CDMD";
	private String dbCatalog = "%e4%b8%ad%e5%9b%bd%e4%bc%98%e7%a7%80%e5%8d%9a%e7%a1%95%e5%a3%ab%e5%ad%a6%e4%bd%8d%e8%ae%ba%e6%96%87%e5%85%a8%e6%96%87%e6%95%b0%e6%8d%ae%e5%ba%93";
	private String configFile = "CDMD.xml";
	private String dbOpt = "CDMD";
	private String dbValue = "%E4%B8%AD%E5%9B%BD%E5%8D%9A%E5%A3%AB%E5%AD%A6%E4%BD%8D%E8%AE%BA%E6%96%87%E5%85%A8%E6%96%87%E6%95%B0%E6%8D%AE%E5%BA%93%2C%E4%B8%AD%E5%9B%BD%E4%BC%98%E7%A7%80%E7%A1%95%E5%A3%AB%E5%AD%A6%E4%BD%8D%E8%AE%BA%E6%96%87%E5%85%A8%E6%96%87%E6%95%B0%E6%8D%AE%E5%BA%93";
	private String searchPrefix = "SU";
	private String name = "doc";
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSearchPrefix() {
		return searchPrefix;
	}
	public void setSearchPrefix(String searchPrefix) {
		this.searchPrefix = searchPrefix;
	}
	public String getDbPrefix() {
		return dbPrefix;
	}
	public void setDbPrefix(String dbPrefix) {
		this.dbPrefix = dbPrefix;
	}
	public String getDbCatalog() {
		return dbCatalog;
	}
	public void setDbCatalog(String dbCatalog) {
		this.dbCatalog = dbCatalog;
	}
	public String getConfigFile() {
		return configFile;
	}
	public void setConfigFile(String configFile) {
		this.configFile = configFile;
	}
	public String getDbOpt() {
		return dbOpt;
	}
	public void setDbOpt(String dbOpt) {
		this.dbOpt = dbOpt;
	}
	public String getDbValue() {
		return dbValue;
	}
	public void setDbValue(String dbValue) {
		this.dbValue = dbValue;
	}
}
