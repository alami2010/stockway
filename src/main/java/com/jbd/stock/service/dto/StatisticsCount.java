package com.jbd.stock.service.dto;

public class StatisticsCount {

    private String value;
    private Long count;

    public StatisticsCount(String value, Long cnt) {
        this.value = value;
        this.count = cnt;
    }

    public StatisticsCount() {}

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }
}
