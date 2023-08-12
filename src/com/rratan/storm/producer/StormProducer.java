package com.rratan.storm.producer;

import com.rratan.storm.common.Deserializer;
import com.rratan.storm.common.Serializer;
import com.rratan.storm.common.TopicPartition;
import com.rratan.storm.mediator.Broker;

import java.util.List;
import java.util.Random;
import java.util.concurrent.LinkedBlockingDeque;

public class StormProducer<T> {
    private final Serializer<T> serializer;
    private final Broker broker;
    private final Random rr;
    private ProducerWorker pw;
    private LinkedBlockingDeque<ProduceRecord> available;

    public StormProducer(Serializer<T> serializer, Broker broker){
        this.serializer = serializer;
        this.broker = broker;
        this.rr = new Random();
        this.pw = new ProducerWorker<T>(Runtime.getRuntime().availableProcessors(),serializer,available);
    }

    public void  produce(T data, String topic){
        List<TopicPartition> partitions = this.broker.getPartitions(topic);

        TopicPartition partitionToProduce = partitions.get(getRandomPartition(partitions.size()));
        available.add(new ProduceRecord(partitionToProduce,data));
        this.pw.signal();
    }

    public int getRandomPartition(int size) {
        return rr.nextInt()%size;
    }
}
