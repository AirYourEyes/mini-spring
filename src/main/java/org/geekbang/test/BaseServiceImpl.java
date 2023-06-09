package org.geekbang.test;

public class BaseServiceImpl implements BaseService {
    private BaseBaseService baseBaseService;

    @Override
    public void sayHello() {
        System.out.println("hello from base service");
    }

    public BaseBaseService getBaseBaseService() {
        return baseBaseService;
    }

    public void setBaseBaseService(BaseBaseService baseBaseService) {
        this.baseBaseService = baseBaseService;
    }
}
