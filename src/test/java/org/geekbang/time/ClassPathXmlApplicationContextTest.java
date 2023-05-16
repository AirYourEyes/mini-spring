package org.geekbang.time;

import org.geekbang.test.AService;

public class ClassPathXmlApplicationContextTest {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("beans.xml");
        AService aService = ((AService) classPathXmlApplicationContext.getBean("aService"));
        aService.sayHello();
    }
}
