package com.hiekn.util;

public interface IReadHttpFileUtil {

	/**
	 * 根据网页URL和编码读取网页源(一般方式)
	 * @param urlString
	 * @param encode
	 * @return
	 */
	public String readPageSourceCommon(String urlString, String encode);
	
	/**
	 * 
	 * 根据网页URL和编码读取网页源码(Ajax)
	 * @param urlString
	 * @param encode
	 * @return
	 * @author wzh
	 * @version 创建时间：2014-8-12 上午10:45:39
	 * Email：wangzh@hiekn.com
	 */
	public String readPageSourceAjax(String urlString, String encode);
}
