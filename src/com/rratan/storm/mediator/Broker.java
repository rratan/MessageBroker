package com.rratan.storm.mediator;

import com.rratan.storm.common.Topic;
import com.rratan.storm.common.TopicPartition;
import com.rratan.storm.consumer.ConsumerGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Broker {
    private List<Topic> topics;
    private ConcurrentHashMap<String , ConsumerGroup> consumerGrp;

    public Broker(){
        this.topics = new ArrayList<>();

    }

    public ConsumerGroup createGrp(String gid){
       return consumerGrp.computeIfAbsent(gid, (k)->{
            return new ConsumerGroup(k);
        });

    }

    public ConsumerGroup getCG(String gid) {
        return consumerGrp.get(gid);
    }

    public boolean createTopic(String topic, int partition){
        boolean ops = false;
        synchronized (topics){
            ops = topics.add(new Topic(topic,partition));
        }
        return ops;
    }

    public Topic getTopic(String topicName){
        for(Topic t: topics){
            if(t.getTopicName().equals(topicName)){
                return t;
            }
        }
        return null;
    }

    public List<TopicPartition> getPartitions(String topic){
        for(Topic t: topics){
            if(t.getTopicName().equals(topic)){
                return t.getPartitionList();
            }
        }
        System.out.println("Topic "+ topic + "is not subscribed.");
        return new ArrayList<>();
    }
}
