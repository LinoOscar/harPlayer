package com.harPlayer.core.utils.har;

import java.util.Date;

public class Page {
	
	Date startedDateTime;
	String title;
	int DOMLoaded;
	int pageLoaded;
	int maxMs;
	
	public Date getStartedDateTime() {
		return startedDateTime;
	}
	public void setStartedDateTime(Date startedDateTime) {
		this.startedDateTime = startedDateTime;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getDOMLoaded() {
		return DOMLoaded;
	}
	public void setDOMLoaded(int dOMLoaded) {
		DOMLoaded = dOMLoaded;
	}
	public int getPageLoaded() {
		return pageLoaded;
	}
	public void setPageLoaded(int pageLoaded) {
		this.pageLoaded = pageLoaded;
	}
	public int getMaxMs() {
		return maxMs;
	}
	public void setMaxMs(int maxMs) {
		this.maxMs = maxMs;
	}
	@Override
	public String toString() {
		return "Page [startedDateTime=" + startedDateTime + ", title=" + title
				+ ", DOMLoaded=" + DOMLoaded + ", pageLoaded=" + pageLoaded
				+ "]";
	}

	
}
