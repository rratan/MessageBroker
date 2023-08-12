package com.rratan.storm.consumer;

import com.rratan.storm.common.Deserializer;
import com.rratan.storm.common.Topic;
import com.rratan.storm.common.TopicPartition;
import com.rratan.storm.mediator.Broker;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class StormConsumer<T> {
    private final Deserializer<T> deserializer;
    private  final Broker broker;
    private final String groupId;

    private Topic topic;

    private List<TopicPartition> assigned;
    private ConsumerGroup cn;



    private final String consumerId;

    public StormConsumer(Deserializer<T> deserializer, Broker broker,String groupId, String topicName) {
        this.deserializer = deserializer;
        this.broker = broker;
        this.groupId = groupId;
        this.topic = broker.getTopic(topicName);
        this.assigned = new ArrayList<>();
        this.consumerId = UUID.randomUUID().toString();
        this.cn  = this.broker.createGrp(groupId);
        this.cn.join(this);

    }


    public void  setAssigned(List<TopicPartition> tpLis){
        this.assigned = tpLis;
    }

    public Topic getTopic(){
        return this.topic;
    }

    public String getConsumerId() {
        return consumerId;
    }

    public List<ConsumerRecord<T>> consume() {
        List<ConsumerRecord<T>> ans = new ArrayList<>();
        for(TopicPartition tp: assigned){
            String key =  this.cn.partitionToString(tp);
            AtomicInteger currOffset = this.cn.getPartitionOffset(key);
            int offSetToFetch = currOffset.intValue() +1 ;
            byte[] data = tp.getData(offSetToFetch);
            ConsumerRecord<T> r = new ConsumerRecord<>(deserializer.deserialize(data),tp.getPartition(),offSetToFetch,tp.getTopicName());
            ans.add(r);
        }
        return ans;
    }


}
