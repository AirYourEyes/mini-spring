package org.geekbang.time;

import org.geekbang.test.AService;
import org.geekbang.test.BaseBaseService;
import org.geekbang.test.BaseService;

public class ClassPathXmlApplicationContextTest {
    public static void main(String[] args) throws Exception {
        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("beans.xml");
        AService aService = ((AService) classPathXmlApplicationContext.getBean("aService"));
        aService.sayHello();

        BaseService baseService = ((BaseService) classPathXmlApplicationContext.getBean("baseService"));
        baseService.sayHello();

        BaseBaseService baseBaseService = ((BaseBaseService) classPathXmlApplicationContext.getBean("baseBaseService"));
        baseBaseService.sayHello();
    }
}
