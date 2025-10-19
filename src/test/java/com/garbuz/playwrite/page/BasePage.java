package com.garbuz.playwrite.page;

import com.garbuz.playwrite.util.TestLog;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public abstract class BasePage {

	protected Playwright playwright;
	protected Browser browser;
	protected BrowserContext context;
	protected Page page;
	protected TestLog testLog;

	public void setPlaywright(Playwright playwright) {
		this.playwright = playwright;
	}

	public void setBrowser(Browser browser) {
		this.browser = browser;
	}

	public void setContext(BrowserContext context) {
		this.context = context;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public TestLog getTestLog() {
		return testLog;
	}

	public void setTestLog(TestLog testLog) {
		this.testLog = testLog;
	}
	
	public final void log(final String messageToLog) {
		if(getTestLog() != null) {
			getTestLog().log(getClass().getSimpleName() + ":" + messageToLog);
		}
	}

	public final String getTitle() {
		final String title = this.page.title();
		log("Page title is :" + title);
		return title;
	}

}
