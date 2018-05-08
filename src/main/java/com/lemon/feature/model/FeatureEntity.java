package com.lemon.feature.model;

public class FeatureEntity {

    private String field;
    private String value;
    private String boost;

    public void setField(String field) {
        this.field = field;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setBoost(String boost) {
        this.boost = boost;
    }

    public String getValue() {
        return value;
    }

    public String getField() {
        return field;
    }

    public String getBoost() {
        return boost;
    }

    @Override
    public String toString() {
        return "FeatureEntity [field=" + field + ", value=" + value + ", boost=" + boost + "]";
    }

}
