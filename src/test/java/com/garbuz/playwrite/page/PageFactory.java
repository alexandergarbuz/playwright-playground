package com.garbuz.playwrite.page;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.garbuz.playwrite.util.TestLog;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class PageFactory {
    private static final Logger LOG = LoggerFactory.getLogger(PageFactory.class);

    private Playwright playwright;
    private Browser browser;
    private BrowserContext context;
    private Page page;
    private TestLog testLog;

    private final Map<Class<? extends BasePage>, BasePage> pages = new HashMap<>();

    // 🔧 Setters
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

	public static PageFactory build(Playwright playwright, Browser browser, BrowserContext context, Page page, TestLog testLog) {
        PageFactory factory = new PageFactory();
        factory.setPlaywright(playwright);
        factory.setBrowser(browser);
        factory.setContext(context);
        factory.setPage(page);
        factory.setTestLog(testLog);
        
        factory.initialize();
        return factory;
    }
    @SuppressWarnings({ "unchecked" })
	private void initialize() {
        String packageName = this.getClass().getPackageName();
        String path = packageName.replace('.', '/');
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            URL packageUrl = loader.getResource(path);
            if (packageUrl == null) return;

            File directory = new File(packageUrl.toURI());
            File[] files = directory.listFiles((d, name) -> name.endsWith(".class"));
            if (files == null) return;

            for (File f : files) {
                String className = f.getName().replace(".class", "");
                String fullName = packageName + "." + className;
                try {
                    Class<?> clazz = Class.forName(fullName);
                    if (!BasePage.class.isAssignableFrom(clazz) || clazz.equals(BasePage.class)) continue;

                    BasePage pageObj = (BasePage) clazz.getDeclaredConstructor().newInstance();
                    pageObj.setPlaywright(playwright);
                    pageObj.setBrowser(browser);
                    pageObj.setContext(context);
                    pageObj.setPage(page);
                    pageObj.setTestLog(testLog);

                    pages.put((Class<? extends BasePage>) clazz, pageObj);
                    LOG.info("Loaded page: {}", fullName);
                } catch (Throwable t) {
                    LOG.warn("Skipping class {} due to error: {}", fullName, t.toString());
                }
            }
        } catch (Exception e) {
            LOG.error("Failed to initialize PageFactory", e);
        }
    }

    public <T extends BasePage> T lookup(Class<T> clazz) {
        return clazz.cast(pages.get(clazz));
    }
}
