package com.rratan.storm.consumer;

public class ConsumerRecord<T> {
    T data;
    int partition;
    int offset;
    String topic;

    public ConsumerRecord(T data, int partition, int offset, String topic) {
        this.data  = data;
        this.partition = partition;
        this.offset = offset;
        this.topic = topic;
    }
}
