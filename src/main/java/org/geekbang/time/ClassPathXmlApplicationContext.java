package org.geekbang.time;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClassPathXmlApplicationContext {
    private List<BeanDefinition> beanDefinitions = new ArrayList<>();
    private Map<String, Object> singletons = new HashMap<>();

    public ClassPathXmlApplicationContext(String fileName) throws Exception {
        this.readXml(fileName);
        this.instanceBeans();
    }

    private void instanceBeans() throws Exception  {
        for (BeanDefinition beanDefinition : beanDefinitions) {
            singletons.put(beanDefinition.getId(), Class.forName(beanDefinition.getClassName()).newInstance());
        }
    }

    private void readXml(String fileName) throws Exception {
        SAXReader saxReader = new SAXReader();
        URL xmlPath = this.getClass().getClassLoader().getResource(fileName);
        Document document = saxReader.read(xmlPath);
        Element rootElement = document.getRootElement();
        for (Element element : (List<Element>)rootElement.elements()) {
            String id = element.attributeValue("id");
            String className = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(id, className);
            beanDefinitions.add(beanDefinition);
        }
    }

    public Object getBean(String beanName) {
        return singletons.get(beanName);
    }

}
