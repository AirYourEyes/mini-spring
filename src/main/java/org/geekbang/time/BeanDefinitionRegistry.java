package org.geekbang.time;

public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String name, BeanDefinition beanDefinition) throws BeansException;

    void removeBeanDefinition(String name);

    BeanDefinition getBeanDefinition(String name);

    boolean contaninsBeanDefinition(String name);

}
