package com.garbuz.playwrite.util;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.garbuz.playwrite.page.PageFactory;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class PlaywrightRule implements TestRule {
	
	private static final Logger LOG = LoggerFactory.getLogger(PlaywrightRule.class);
	
    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;
    private PageFactory factory;
    private TestLog testLog;

    @Override
    public Statement apply(Statement base, Description description) {
        return new Statement() {
        	
        	void init() {
                playwright = Playwright.create();
                browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
                context = browser.newContext();
                page = context.newPage();
                testLog = new TestLog();
                factory = PageFactory.build(playwright, browser, context, page, testLog);
        	}
        	
        	void afterTest() {
                context.close();
                browser.close();
                playwright.close();
                testLog.clear();
        	}
            @Override
            public void evaluate() throws Throwable {
            	init();
                try {
                    base.evaluate(); // run the actual test
                } catch(Throwable t) {
                	LOG.error(System.lineSeparator() + testLog.getMessages() + System.lineSeparator());
                	//Take screenshot
                	//Email the log content
                	//Close everything
                	throw t;
                } finally {
                	afterTest();

                }
            }
        };
    }

    public PageFactory getFactory() { 
    	return factory; 
    }
}

