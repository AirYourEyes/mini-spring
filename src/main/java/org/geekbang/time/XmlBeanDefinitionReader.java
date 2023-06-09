package org.geekbang.time;

import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class XmlBeanDefinitionReader {
    private BeanFactory beanFactory;

    public XmlBeanDefinitionReader(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void loadBeanDefinitions(Resource resource) throws BeansException {
        while (resource.hasNext()) {
            Element element = (Element)resource.next();
            String id = element.attributeValue("id");
            String className = element.attributeValue("class");
            BeanDefinition beanDefinition = new BeanDefinition(id, className);
            // 处理属性
            List<Element> propertyElements = ((List<Element>) element.elements("property"));
            PropertyValues propertyValues = new PropertyValues();
            List<String> refs = new ArrayList<>();
            for (Element e : propertyElements) {
                String pType = e.attributeValue("type");
                String pName = e.attributeValue("name");
                String pValue = e.attributeValue("value");
                String pRef = e.attributeValue("ref");

                String value = "";
                boolean isRef = false;
                if (pValue != null && !pValue.isEmpty()) {
                    isRef = false;
                    value = pValue;
                } else if (pRef != null && !pRef.isEmpty()) {
                    isRef = true;
                    value = pRef;
                    refs.add(pRef);
                }
                PropertyValue propertyValue = new PropertyValue(pType, pName, value, isRef);
                propertyValues.addPropertyValue(propertyValue);
            }
            beanDefinition.setPropertyValues(propertyValues);

            // 处理构造器参数
            List<Element> constructorElements = element.elements("constructor-arg");
            ArgumentValues argumentValues = new ArgumentValues();
            for (Element constructorElement : constructorElements) {
                String cType = constructorElement.attributeValue("type" );
                String cName = constructorElement.attributeValue("name" );
                String cValue = constructorElement.attributeValue("value" );
                String pRef = constructorElement.attributeValue("ref");
                boolean isRef = false;
                String value = "";
                if (cValue != null && !cValue.isEmpty()) {
                    isRef = false;
                    value = cValue;
                } else if (pRef != null && !pRef.isEmpty()) {
                    isRef = true;
                    value = pRef;
                    refs.add(pRef);
                }
                argumentValues.addGenericArgumentValue(new ArgumentValue(value, cType, cName, isRef));
            }
            beanDefinition.setConstructorArgumentValues(argumentValues);

            String[] refArray = refs.toArray(new String[0]);
            beanDefinition.setDependsOn(refArray);

            beanFactory.registerBeanDefinition(beanDefinition);
        }
    }

}
