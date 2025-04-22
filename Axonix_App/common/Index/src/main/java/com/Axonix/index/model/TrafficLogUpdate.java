package com.Axonix.index.model;

public class TrafficLogUpdate {
    private TrafficLog record;
    private TrafficLogExample example;

    public TrafficLogUpdate(TrafficLog record, TrafficLogExample example) {
        this.record = record;
        this.example = example;
    }

    public TrafficLog getRecord() {
        return record;
    }

    public void setRecord(TrafficLog record) {
        this.record = record;
    }

    public TrafficLogExample getExample() {
        return example;
    }

    public void setExample(TrafficLogExample example) {
        this.example = example;
    }
}