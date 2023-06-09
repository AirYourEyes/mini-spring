package org.geekbang.time;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory, BeanDefinitionRegistry {
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private List<String> beanDefinitionNames = new ArrayList<>();
    // 存放毛坯实例
    private Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>();

    public SimpleBeanFactory() {

    }

    @Override
    public Object getBean(String beanName) throws BeansException {
        // 先尝试直接从容器中获取 bean 实例
        Object singleton = this.getSingleton(beanName);
        if (singleton != null) {
            return singleton;
        }

        // 如果没有，则尝试从毛坯实例中获得
        singleton = this.earlySingletonObjects.get(beanName);
        if (singleton != null) {
            return singleton;
        }

        // 如果连毛坯实例都没有，则创建 bean 并注册
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new BeansException("No Bean");
        }

        try {
            singleton = createBean(beanDefinition);
            this.registerSingleton(beanName, singleton);
        } catch (Exception e) {
            throw new BeansException(e);
        }
        return singleton;
    }

    private Object createBean(BeanDefinition beanDefinition) throws Exception {
        Class<?> clazz = Class.forName(beanDefinition.getClassName());

        // 处理构造器参数
        Object beanInstance = doCreateBean(beanDefinition, clazz);
        // 保存毛坯实例，解决循环依赖
        this.earlySingletonObjects.put(beanDefinition.getId(), beanInstance);

        // 处理属性
        handleProperties(beanDefinition, clazz, beanInstance);

        return beanInstance;
    }

    private Object doCreateBean(BeanDefinition beanDefinition, Class<?> clazz) throws Exception {
        ArgumentValues constructorArgumentValues = beanDefinition.getConstructorArgumentValues();
        if (constructorArgumentValues == null || constructorArgumentValues.isEmpty()) {
            return clazz.newInstance();
        }

        int argumentCount = constructorArgumentValues.getArgumentCount();
        Class<?>[] paramTypes = new Class<?>[argumentCount];
        Object[] paramValues = new Object[argumentCount];
        for (int i = 0; i < argumentCount; i++) {
            ArgumentValue argumentValue = constructorArgumentValues.getIndexedArgumentValue(i);
            String type = argumentValue.getType();
            Object value = argumentValue.getValue();
            String name = argumentValue.getName();
            boolean isRef = argumentValue.isRef();
            // 处理引用类型参数
            if (isRef) {
                paramTypes[i] = Class.forName(type);
                paramValues[i] = getBean(name);
            } else { // 处理普通参数
                if ("String".equals(type)) {
                    paramTypes[i] = String.class;
                    paramValues[i] = value;
                } else if ("Integer".equals(type) || "int".equals(type)) {
                    paramTypes[i]= Integer.class;
                    paramValues[i] = Integer.parseInt((String) value);
                }
            }
        }
        return clazz.getConstructor(paramTypes).newInstance(paramValues);
    }

    private void handleProperties(BeanDefinition beanDefinition, Class<?> clazz, Object beanInstance) throws Exception {
        System.out.println("handle properties for bean: " + beanDefinition.getId());
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        if (propertyValues.isEmpty()) {
            return;
        }

        // 给所有属性赋值
        for (PropertyValue propertyValue : propertyValues.getPropertyValueList()) {
            String type = propertyValue.getType();
            String name = propertyValue.getName();
            Object value = propertyValue.getValue();
            boolean isRef = propertyValue.isRef();
            // 参数类型，默认 String 类型
            Class<?> paramType = String.class;
            // 参数值，默认为 String
            Object paramValue = value;
            // 处理普通属性
            if (!isRef) {
                if ("String".equals(type)) {
                    paramType = String.class;
                } else if ("Integer".equals(type)) {
                    paramType = Integer.class;
                } else if ("int".equals(type)) {
                    paramType = int.class;
                }
            } else {// 处理引用类型
                paramType = Class.forName(type);
                paramValue = getBean((String)value);
            }
            String methodName = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
            Method method = clazz.getMethod(methodName, paramType);
            method.invoke(beanInstance, paramValue);
        }
    }

    @Override
    public void registerBeanDefinition(BeanDefinition beanDefinition) throws BeansException {
        this.registerBeanDefinition(beanDefinition.getId(), beanDefinition);
    }

    @Override
    public boolean containsBean(String name) {
        return this.containsSingleton(name);
    }

    @Override
    public void registerBean(String beanName, Object obj) {
        this.registerSingleton(beanName, obj);
    }

    @Override
    public void registerBeanDefinition(String name, BeanDefinition beanDefinition) throws BeansException {
        this.beanDefinitionMap.put(name, beanDefinition);
        this.beanDefinitionNames.add(name);
    }

    @Override
    public void removeBeanDefinition(String name) {
        this.beanDefinitionMap.remove(name);
        this.beanDefinitionNames.remove(name);
        this.removeSingleton(name);
    }

    @Override
    public BeanDefinition getBeanDefinition(String name) {
        return this.beanDefinitionMap.get(name);
    }

    @Override
    public boolean contaninsBeanDefinition(String name) {
        return this.beanDefinitionMap.containsKey(name);
    }

    @Override
    public boolean isSingleton(String name) {
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(name);
        return beanDefinition != null ? beanDefinition.isSingleton() : false;
    }

    @Override
    public boolean isPrototype(String name) {
        BeanDefinition beanDefinition = this.beanDefinitionMap.get(name);
        return beanDefinition != null ? beanDefinition.isPrototype() : false;
    }

    @Override
    public Class<?> getType(String name) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(name);
        return beanDefinition != null ? beanDefinition.getBeanClass() : null;
    }

    public void refresh() throws BeansException {
        /**
         * 容器启动时加载所有的 bean
         */
        for (String beanDefinitionName : beanDefinitionNames) {
            getBean(beanDefinitionName);
        }
    }
}
