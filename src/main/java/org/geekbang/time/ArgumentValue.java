package org.geekbang.time;

public class ArgumentValue {
    private Object value;
    private String type;
    private String name;
    private boolean isRef;

    public ArgumentValue(Object value, String type, boolean isRef) {
        this.value = value;
        this.type = type;
        this.isRef = isRef;
    }

    public ArgumentValue(Object value, String type, String name, boolean isRef) {
        this.value = value;
        this.type = type;
        this.name = name;
        this.isRef = isRef;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRef() {
        return isRef;
    }

    public void setRef(boolean ref) {
        isRef = ref;
    }
}
