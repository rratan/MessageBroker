package com.rratan.storm.producer;

import com.rratan.storm.common.Deserializer;
import com.rratan.storm.common.Topic;
import com.rratan.storm.common.TopicPartition;

public class ProduceRecord<T> {
    public TopicPartition getTp() {
        return tp;
    }

    public T getData() {
        return data;
    }

    private TopicPartition tp;
    private T data;

    public ProduceRecord(TopicPartition tp, T data){
        this.tp = tp;
        this.data = data;

    }

}
