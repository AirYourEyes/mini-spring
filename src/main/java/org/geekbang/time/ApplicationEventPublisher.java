package org.geekbang.time;

public interface ApplicationEventPublisher {

    void publishEvent(ApplicationEvent event);

}
