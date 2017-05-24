package com.hiekn.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.http.Consts;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.GzipDecompressingEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * 
 *
 * @author pzn
 * @since 1.7
 * @version 1.0
 */
public class StaticHttpReader extends CommonHttpReader implements HttpReader {

    CloseableHttpClient httpClient = null;
    RequestConfig defaultRequestConfig = null;
    // 最大尝试次数
    static final int MAX_TRIES = 3;
    // html默认编码
    static final String DEFAULT_CHARSET = "gbk";
    // 默认超时时间
    static final int DEFAULT_CONNECT_TIMEOUT = 50000;
    static final int DEFAULT_SOCKET_TIMEOUT = 50000;
    static final int DEFAULT_CONNECTION_REQUEST_TIMEOUT = 50000;

    String[] userAgents = {"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36"};

    public StaticHttpReader() {
        httpClient = HttpClients.custom()//
                .setUserAgent(userAgents[0])//
                .build();

        defaultRequestConfig = RequestConfig.custom()//
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)// 连接超时时间
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)// 传输超时时间
                .setConnectionRequestTimeout(DEFAULT_CONNECTION_REQUEST_TIMEOUT)// 请求超时时间
                .setCookieSpec(CookieSpecs.DEFAULT)
                .setCircularRedirectsAllowed(false)
                .build();
    }

    public StaticHttpReader(int connectTimeout, int socketTimeout, int connectionRequestTimeout) {
        httpClient = HttpClients.custom()//
                .setUserAgent(userAgents[new Random().nextInt(3)])//
                .build();

        defaultRequestConfig = RequestConfig.custom()//
                .setConnectTimeout(connectTimeout)// 连接超时时间
                .setSocketTimeout(socketTimeout)// 传输超时时间
                .setConnectionRequestTimeout(connectionRequestTimeout)// 请求超时时间
                .setCookieSpec(CookieSpecs.DEFAULT)
                .setCircularRedirectsAllowed(false)
                .build();
    }

    public StaticHttpReader(String hostname, int port) {
        httpClient = HttpClients.custom()//
                .setUserAgent(userAgents[new Random().nextInt(3)])//
                .build();

        defaultRequestConfig = RequestConfig.custom()//
                .setConnectTimeout(DEFAULT_CONNECT_TIMEOUT)// 连接超时时间
                .setSocketTimeout(DEFAULT_SOCKET_TIMEOUT)// 传输超时时间
                .setConnectionRequestTimeout(DEFAULT_CONNECTION_REQUEST_TIMEOUT)// 请求超时时间
                .setProxy(new HttpHost(hostname, port))// 设置代理
                .setCookieSpec(CookieSpecs.DEFAULT)
                .setCircularRedirectsAllowed(false)
                .build();
    }

    public String readSource(String url, String charset, String cookies,
    		String requestType, String params) {
    	String pageSource = "";
    	
    	// trim
    	url = url.trim();
    	
    	//LOGGER.info("request url ... " + url);
    	CloseableHttpResponse httpResponse = null;
    	for (int tryTime = 0; tryTime < MAX_TRIES; tryTime++) {
    		try {
	    		if (HTTP_GET.equals(requestType)) {
	    			if (!StringUtils.isNullOrEmpty(params)) {
	    				if (url.indexOf("?") > -1) url += "&" + params;
	    				else url += "?" + params;
	    			} 
	    			HttpGet httpGet = new HttpGet(url);
//	                httpGet.addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
	                httpGet.addHeader("Accept-Encoding", "gzip, deflate, sdch, br");
	                httpGet.addHeader("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.4");
	                httpGet.addHeader("Connection", "keep-alive");
//	                httpGet.addHeader("Referer", "http://www.tianyancha.com/search/%E6%A1%90%E6%A2%93%E5%88%9B%E5%85%B4%E8%87%AA%E5%8A%A8%E5%8C%96%E8%AE%BE%E5%A4%87%E6%9C%89%E9%99%90%E5%85%AC%E5%8F%B8?checkFrom=searchBox");
	                httpGet.addHeader("Accept", "application/json, text/plain, */*");
	                httpGet.addHeader("X-Requested-With", "XMLHttpRequest");
	                if (!StringUtils.isNullOrEmpty(cookies)) {
	                    httpGet.addHeader("Cookie", cookies);
	                }
	                httpGet.setConfig(defaultRequestConfig);
					httpResponse = httpClient.execute(httpGet);
	    		} else if (HTTP_POST.equals(requestType)) {
	    			HttpPost httpPost = new HttpPost(url);
					httpPost.setConfig(defaultRequestConfig);
					if (!StringUtils.isNullOrEmpty(cookies)) {
						httpPost.addHeader("Cookie", cookies);
					}
					
					if (!StringUtils.isNullOrEmpty(params)) {
						List<NameValuePair> nvps = new ArrayList<NameValuePair>();
						String[] ps = params.split(";");
						for (String param : ps) {
							String[] nv = param.split("=");
							nvps.add(new BasicNameValuePair(nv[0], nv[1]));
						}
						
						httpPost.setEntity(new UrlEncodedFormEntity(nvps, Consts.UTF_8));
					}
					httpResponse = httpClient.execute(httpPost);
	    		} else {
	    			break;
	    		}
	    		
	    		//
	    		// 处理httpResponse 
	    		//
	    		int status = httpResponse.getStatusLine().getStatusCode();
                //LOGGER.info("response code ... " + status);
                if (HttpStatus.SC_OK == status) {
                	//
                    HttpEntity entity = httpResponse.getEntity();
                    if (null != entity) {
                    	// httpclient默认会自动解压gzip 
                    	Header ceHeader= httpResponse.getLastHeader("Content-Encoding");
                    	if (null != ceHeader) {
                    		if (ceHeader.getValue().indexOf("gzip") > -1) {
                    			//
                    			entity = new GzipDecompressingEntity(entity);
                    		}
                    	}
                    	
                    	//
                        if (!StringUtils.isNullOrEmpty(charset)) {
                            pageSource = EntityUtils.toString(entity, charset);
                        } else {// 解析编码
                            ContentType contentType = ContentType.get(entity);
                            String contentCharset = null;
                            if (contentType != null) {
                                if (contentType.getCharset() != null) {
                                    contentCharset = contentType.getCharset().displayName();
                                }
                            }
                            // 服务器没有返回url指定的编码,先使用ISO-8859-1,然后从源码找查找编码
                            if (contentCharset == null) {
                                contentCharset = Consts.ISO_8859_1.displayName();
                                // EntityUtils.toString先在entity中找编码，没找到，使用传入的默认编码。没设置默认编码，则使用ISO_8859_1
                                pageSource = EntityUtils.toString(entity, contentCharset);
                                charset = parseCharset(pageSource).equals("") ? DEFAULT_CHARSET :  parseCharset(pageSource);//解析到的编码
                                //LOGGER.info("detect " + url + " ... charset=" + charset);
                                pageSource = new String(pageSource.getBytes(contentCharset), charset);
                            } else {// 如果服务器返回时有指定编码，则不会出现乱码
                                charset = contentCharset;
                                pageSource = EntityUtils.toString(entity, charset);
                            }
                        }
                        // entity 不为null，结束循环
                        break;
                    }
                }
    		} catch (ClientProtocolException e) {
    			e.printStackTrace();
    			//LOGGER.error(e);
    		} catch (IOException e) {
    			e.printStackTrace();
    			//LOGGER.error(e);
    		} catch (Exception e) {
    			e.printStackTrace();
    			//LOGGER.error(e);
			} finally {
    			if (httpResponse != null) {
                    try {
                        httpResponse.close();
                    } catch (IOException e) {
                        //LOGGER.error("IOException " + e);
                    }
                }
    		}
    	}
    	//LOGGER.info("request result length ... " + pageSource.length());
    	return pageSource;
    }
    
    public void close() {
        if (null != httpClient) {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
                //LOGGER.error("close ... " + e);
            }
            httpClient = null;
        }
    }

}
