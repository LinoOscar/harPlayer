package com.harPlayer.core.utils.har;

import java.util.ArrayList;
import java.util.Date;

import com.harPlayer.utils.UrlRemedyUtils;


public class HarEntry {
	
	int pageRef;
	Date startedDateTime;
	String startedDateTimeFormated;
	int time;
	
	Date pageStartedDateTime;
	String pageStartedDateTimeFormated;
	String pageTitle;
	int pageDOMLoaded;
	int pageContentLoaded;
	int pageMaxMs;
	
	String requestMethod;
	String requesUrl;
	String requesUrlShort;
	String requesUrlMiddle;
	String requesUrlGetPart;
	String requestUrlAssembly;
	String requestURLLocalHost;
	String requesHttpVersion;	
	ArrayList<Cookie> requestCookies;
	ArrayList<Header> requestHeaders;
	ArrayList<Param>  requestQueryString;
	String 	requestPostDataMymeType;
	ArrayList<Param>  requestPostData;
	
	int  requestHeaderSize;
	int  requestBodySize;
	
	int responseStatus;
	String responseStatusText;
	String responseHttpVersion;
	ArrayList<Cookie> responseCookies;
	ArrayList<Header> responseHeaders;
	
	int responseContentSize;
	String responseContentMimeType;
	String responseContentMimeTypeShort;
	String responseContentText;
	
	String responseRedirectionUrl;
	int responseHeadersSize;
	int responseBodySize;
	
	String cache;
	int timingsStart;
	int timingsSend;
	int timingsWait;
	int timingsReceive;
	
	String newRequestColor;
	
	public int getPageRef() {
		return pageRef;
	}
	public void setPageRef(int pageRef) {
		this.pageRef = pageRef;
	}
	public Date getStartedDateTime() {
		return startedDateTime;
	}
	public void setStartedDateTime(Date startedDateTime) {
		this.startedDateTime = startedDateTime;
		setStartedDateTimeFormated(UrlRemedyUtils.transformDateToString(startedDateTime));
	}
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getRequestMethod() {
		return requestMethod;
	}
	public void setRequestMethod(String requestMethod) {
		this.requestMethod = requestMethod;
	}
	public String getRequesUrl() {
		return requesUrl;
	}
	public void setRequesUrl(String requesUrl) {
		this.requesUrl= requesUrl;
	}
	public String getRequesUrlMiddle() {
		return requesUrlMiddle;
	}
	public void setRequesUrlMiddle(String requesUrlMiddle) {
		this.requesUrlMiddle= requesUrlMiddle;
	}	
	public String getRequesUrlShort() {
		return requesUrlShort;
	}
	public void setRequesUrlShort(String requesUrlShort) {
		this.requesUrlShort= requesUrlShort;
	}	
	public String getRequesUrlGetPart() {
		return requesUrlGetPart;
	}
	public void setRequesUrlGetPart(String requesUrlGetPart) {
		this.requesUrlGetPart = requesUrlGetPart;
	}
	public String getRequestUrlAssembly() {
		return requestUrlAssembly;
	}
	public void setRequestUrlAssembly(String requestUrlAssembly) {
		this.requestUrlAssembly = requestUrlAssembly;
	}
	public String getRequestURLLocalHost() {
		return requestURLLocalHost;
	}
	public void setRequestURLLocalHost(String requestURLLocalHost) {
		this.requestURLLocalHost = requestURLLocalHost;
	}
	public String getRequesHttpVersion() {
		return requesHttpVersion;
	}
	public void setRequesHttpVersion(String requesHttpVersion) {
		this.requesHttpVersion = requesHttpVersion;
	}
	public ArrayList<Cookie> getRequestCookies() {
		return requestCookies;
	}
	public void setRequestCookies(ArrayList<Cookie> requestCookies) {
		this.requestCookies = requestCookies;
	}
	public ArrayList<Header> getRequestHeaders() {
		return requestHeaders;
	}
	public void setRequestHeaders(ArrayList<Header> requestHeaders) {
		this.requestHeaders = requestHeaders;
	}
	public ArrayList<Param> getRequestQueryString() {
		return requestQueryString;
	}
	public void setRequestQueryString(ArrayList<Param> requestQueryString) {
		this.requestQueryString = requestQueryString;
	}
	public int getRequestHeaderSize() {
		return requestHeaderSize;
	}
	public void setRequestHeaderSize(int requestHeaderSize) {
		this.requestHeaderSize = requestHeaderSize;
	}
	public int getRequestBodySize() {
		return requestBodySize;
	}
	public void setRequestBodySize(int requestBodySize) {
		this.requestBodySize = requestBodySize;
	}
	public int getResponseStatus() {
		return responseStatus;
	}
	public void setResponseStatus(int responseStatus) {
		this.responseStatus = responseStatus;
	}
	public String getResponseStatusText() {
		return responseStatusText;
	}
	public void setResponseStatusText(String responseStatusText) {
		this.responseStatusText = responseStatusText;
	}
	public String getResponseHttpVersion() {
		return responseHttpVersion;
	}
	public void setResponseHttpVersion(String responseHttpVersion) {
		this.responseHttpVersion = responseHttpVersion;
	}
	public ArrayList<Cookie> getResponseCookies() {
		return responseCookies;
	}
	public void setResponseCookies(ArrayList<Cookie> responseCookies) {
		this.responseCookies = responseCookies;
	}
	public ArrayList<Header> getResponseHeaders() {
		return responseHeaders;
	}
	public void setResponseHeaders(ArrayList<Header> responseHeaders) {
		this.responseHeaders = responseHeaders;
	}
	public int getResponseContentSize() {
		return responseContentSize;
	}
	public void setResponseContentSize(int responseContentSize) {
		this.responseContentSize = responseContentSize;
	}
	public String getResponseContentMimeType() {
		return responseContentMimeType;
	}
	public void setResponseContentMimeType(String responseContentMimeType) {
		this.responseContentMimeType = responseContentMimeType;
	}
	public String getResponseContentMimeTypeShort() {
		return responseContentMimeTypeShort;
	}
	public void setResponseContentMimeTypeShort(String responseContentMimeTypeShort) {
		this.responseContentMimeTypeShort = responseContentMimeTypeShort;
	}	
	public String getResponseContentText() {
		return responseContentText;
	}
	public void setResponseContentText(String responseContentText) {
		this.responseContentText = responseContentText;
	}
	public String getResponseRedirectionUrl() {
		return responseRedirectionUrl;
	}
	public void setResponseRedirectionUrl(String responseRedirectionUrl) {
		this.responseRedirectionUrl = responseRedirectionUrl;
	}
	public int getResponseHeadersSize() {
		return responseHeadersSize;
	}
	public void setResponseHeadersSize(int responseHeadersSize) {
		this.responseHeadersSize = responseHeadersSize;
	}
	public int getResponseBodySize() {
		return responseBodySize;
	}
	public void setResponseBodySize(int responseBodySize) {
		this.responseBodySize = responseBodySize;
	}
	public String getCache() {
		return cache;
	}
	public void setCache(String cache) {
		this.cache = cache;
	}
	public int getTimingsStart() {
		return timingsStart;
	}
	public void setTimingsStart(int timingsStart) {
		this.timingsStart = timingsStart;
	}
	public int getTimingsSend() {
		return timingsSend;
	}
	public void setTimingsSend(int timingsSend) {
		this.timingsSend = timingsSend;
	}
	public int getTimingsWait() {
		return timingsWait;
	}
	public void setTimingsWait(int timingsWait) {
		this.timingsWait = timingsWait;
	}
	public int getTimingsReceive() {
		return timingsReceive;
	}
	public void setTimingsReceive(int timingsReceive) {
		this.timingsReceive = timingsReceive;
	}
	public String getRequestPostDataMymeType() {
		return requestPostDataMymeType;
	}
	public void setRequestPostDataMymeType(String requestPostDataMymeType) {
		this.requestPostDataMymeType = requestPostDataMymeType;
	}
	public ArrayList<Param> getRequestPostData() {
		return requestPostData;
	}
	public void setRequestPostData(ArrayList<Param> requestPostData) {
		this.requestPostData = requestPostData;
	}
	public Date getPageStartedDateTime() {
		return pageStartedDateTime;
	}
	public void setPageStartedDateTime(Date pageStartedDateTime) {
		this.pageStartedDateTime = pageStartedDateTime;
		setPageStartedDateTimeFormated(UrlRemedyUtils.transformDateToString(pageStartedDateTime));
	}
	public String getPageTitle() {
		return pageTitle;
	}
	public void setPageTitle(String pageTitle) {
		this.pageTitle = pageTitle;
	}
	public int getPageDOMLoaded() {
		return pageDOMLoaded;
	}
	public void setPageDOMLoaded(int pageDOMLoaded) {
		this.pageDOMLoaded = pageDOMLoaded;
	}
	public int getPageContentLoaded() {
		return pageContentLoaded;
	}
	public void setPageContentLoaded(int pageContentLoaded) {
		this.pageContentLoaded = pageContentLoaded;
	}
	public int getPageMaxMs() {
		return pageMaxMs;
	}
	public void setPageMaxMs(int pageMaxMs) {
		this.pageMaxMs = pageMaxMs;
	}
	public String getNewRequestColor() {
		return newRequestColor;
	}
	public void setNewRequestColor(String newRequestColor) {
		this.newRequestColor = newRequestColor;
	}
	public String getStartedDateTimeFormated() {
		return startedDateTimeFormated;
	}
	public void setStartedDateTimeFormated(String startedDateTimeFormated) {
		this.startedDateTimeFormated = startedDateTimeFormated;
	}
	public String getPageStartedDateTimeFormated() {
		return pageStartedDateTimeFormated;
	}
	public void setPageStartedDateTimeFormated(String pageStartedDateTimeFormated) {
		this.pageStartedDateTimeFormated = pageStartedDateTimeFormated;
	}
	
	@Override
	public String toString() {
		return "HarEntry [pageRef=" + pageRef + ", startedDateTime="
				+ startedDateTime + ", time=" + time + ", requestMethod="
				+ requestMethod + ", requesUrl=" + requesUrl
				+ ", requesHttpVersion=" + requesHttpVersion
				+ ", requestCookies=" + requestCookies + ", requestHeaders="
				+ requestHeaders + ", requestQueryString=" + requestQueryString
				+ ", requestHeaderSize=" + requestHeaderSize
				+ ", requestBodySize=" + requestBodySize + ", responseStatus="
				+ responseStatus + ", responseStatusText=" + responseStatusText
				+ ", responseHttpVersion=" + responseHttpVersion
				+ ", responseCookies=" + responseCookies + ", responseHeaders="
				+ responseHeaders + ", responseContentSize="
				+ responseContentSize + ", responseContentMimeType="
				+ responseContentMimeType + ", responseContentText="
				+ responseContentText + ", responseRedirectionUrl="
				+ responseRedirectionUrl + ", responseHeadersSize="
				+ responseHeadersSize + ", responseBodySize=" + responseBodySize
				+ ", cache=" + cache + ", timingsSend=" + timingsSend
				+ ", timingsWait=" + timingsWait + ", timingsReceive="
				+ timingsReceive + "]";
	}
	
	
	
	
}
