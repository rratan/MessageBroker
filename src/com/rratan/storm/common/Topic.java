package com.rratan.storm.common;

import java.util.ArrayList;
import java.util.List;

public class Topic {
    private final List<TopicPartition> partitionList;
    private final String topicName;
    private final int partitionCnt;

    public Topic(String topicName, int partitionCnt){
        this.topicName = topicName;
        this.partitionCnt = partitionCnt;
        this.partitionList = new ArrayList<>(partitionCnt);
        createPartition(partitionCnt);

    }
    private void createPartition(int partitionCnt){

        for(int i = 0 ; i <partitionCnt; i++ ){
            partitionList.add(new TopicPartition(this.topicName,i));
        }
    }

    public List<TopicPartition> getPartitionList() {
        return partitionList;
    }

    public String getTopicName() {
        return topicName;
    }




}
