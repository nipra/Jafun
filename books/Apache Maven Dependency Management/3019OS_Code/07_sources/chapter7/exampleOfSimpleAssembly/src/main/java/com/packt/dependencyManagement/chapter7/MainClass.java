package com.packt.dependencyManagement.chapter7;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MainClass {
    public static void main(String[] args) {
        final ApplicationContext applicationContext;
        final AnyService anyService;

        applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        anyService = (AnyService) applicationContext.getBean("anyService");
        anyService.anyMethod();
    }
}
