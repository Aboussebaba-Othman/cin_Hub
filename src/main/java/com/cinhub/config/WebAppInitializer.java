package com.cinhub.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

/**
 * Initializes the Spring DispatcherServlet and root application context.
 * This replaces the traditional web.xml configuration.
 */
public class WebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    /**
     * Root configuration classes (services, repositories, JPA)
     */
    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{AppConfig.class};  // Changed from RootConfig
    }

    /**
     * Web configuration classes (controllers, Spring MVC)
     */
    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[]{WebConfig.class};
    }

    /**
     * DispatcherServlet mapping
     */
    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}