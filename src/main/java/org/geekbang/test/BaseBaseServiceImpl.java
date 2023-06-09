package org.geekbang.test;

public class BaseBaseServiceImpl implements BaseBaseService {
    private AService aService;

    @Override
    public void sayHello() {
        System.out.println("hello from base base service");
    }

    public AService getAService() {
        return aService;
    }

    public void setAService(AService aService) {
        this.aService = aService;
    }
}
