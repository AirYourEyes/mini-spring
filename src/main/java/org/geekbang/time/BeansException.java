package org.geekbang.time;

public class BeansException extends Exception {

    public BeansException(Exception exception) {
        super(exception);
    }
    public BeansException(String message) {
        super(message);
    }
}
