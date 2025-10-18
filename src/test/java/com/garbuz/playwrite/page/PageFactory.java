package com.garbuz.playwrite.page;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PageFactory {

    private static final Logger LOG = LoggerFactory.getLogger(PageFactory.class);
    private static final Map<Class<? extends BasePage>, BasePage> pages = new HashMap<>();
    private static volatile boolean initialized = false;

    private PageFactory() {
        // prevent instantiation
    }

    private static synchronized void initialize() {
        if (initialized) return;

        String packageName = PageFactory.class.getPackageName();
        String path = packageName.replace('.', '/');

        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            URL packageUrl = classLoader.getResource(path);

            if (packageUrl == null) {
                LOG.warn("No classes found for package: {}", packageName);
                return;
            }

            File directory = new File(packageUrl.toURI());
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".class"));

            if (files == null) {
                LOG.warn("No class files found in: {}", directory.getAbsolutePath());
                return;
            }

            for (File file : files) {
                String className = file.getName().replace(".class", "");
                String fullClassName = packageName + "." + className;

                try {
                    Class<?> clazz = Class.forName(fullClassName);

                    if (BasePage.class.isAssignableFrom(clazz) && !clazz.equals(BasePage.class)) {
                        @SuppressWarnings("unchecked")
                        Class<? extends BasePage> pageClass = (Class<? extends BasePage>) clazz;
                        BasePage page = pageClass.getDeclaredConstructor().newInstance();
                        pages.put(pageClass, page);
                        LOG.info("Registered page: {}", fullClassName);
                    }

                } catch (Throwable t) {
                    LOG.error("Failed to instantiate page: {}", fullClassName, t);
                }
            }

            initialized = true;

        } catch (Exception e) {
            LOG.error("Failed to scan package: {}", packageName, e);
        }
    }

    public static <T extends BasePage> T lookup(Class<T> clazz) {
        if (!initialized) {
            initialize();
        }
        return clazz.cast(pages.get(clazz));
    }
}