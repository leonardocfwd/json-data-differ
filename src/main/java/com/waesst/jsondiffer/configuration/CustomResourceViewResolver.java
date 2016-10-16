package com.waesst.jsondiffer.configuration;

import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.io.InputStream;

public class CustomResourceViewResolver extends InternalResourceViewResolver {

    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        String url = getPrefix() + viewName + getSuffix();
        InputStream stream = getServletContext().getResourceAsStream(url);
        if (stream == null) {
            return null;
        } else {
            stream.close();
        }
        return super.buildView(viewName);
    }
}
