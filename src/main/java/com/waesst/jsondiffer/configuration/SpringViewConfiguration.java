package com.waesst.jsondiffer.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@EnableWebMvc
@Configuration
public class SpringViewConfiguration extends WebMvcConfigurerAdapter {

    private static final String VIEW_DIR_HTML = "/resources/app/";
    private static final String VIEW_EXTN_HTML = ".html";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public ViewResolver htmlViewResolver() {
        InternalResourceViewResolver viewResolver= new CustomResourceViewResolver();
        viewResolver.setPrefix(VIEW_DIR_HTML);
        viewResolver.setSuffix(VIEW_EXTN_HTML);
        return viewResolver;
    }
}
