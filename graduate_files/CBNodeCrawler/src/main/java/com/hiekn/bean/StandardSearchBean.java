package com.hiekn.bean;

public class StandardSearchBean extends SearchBean {
	private String dbPrefix = "CISD";
	private String dbCatalog = "%e6%a0%87%e5%87%86%e6%95%b0%e6%8d%ae%e6%80%bb%e5%ba%93";
	private String configFile = "CISD.xml";
	private String dbOpt = "CISD";
	private String dbValue = "%E4%B8%AD%E5%9B%BD%E4%B8%93%E5%88%A9%E6%95%B0%E6%8D%AE%E5%BA%93%2C%E5%9B%BD%E5%A4%96%E4%B8%93%E5%88%A9%E6%95%B0%E6%8D%AE%E5%BA%93";
	private String searchPrefix = "TI";
	private String name = "standard";
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
