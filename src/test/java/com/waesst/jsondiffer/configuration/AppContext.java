package com.waesst.jsondiffer.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackages = {"com.waesst.jsondiffer.persistence"})
@Import(HibernateConfiguration.class)
public class AppContext {

}
