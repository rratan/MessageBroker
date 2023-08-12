package com.rratan.storm.common;

import java.util.Arrays;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class TopicPartition {

    private AtomicInteger  offset = new AtomicInteger(0);

    private final int partition;
    private String topicName;
    private CopyOnWriteArrayList<ByteBlock> log;

    public TopicPartition(String topicName, int partition){
        this.partition = partition;
        this.log = new CopyOnWriteArrayList<>();
        this.topicName = topicName;
    }

    public  boolean add(byte[] inp){
        int currOffset = offset.getAndAdd(1);
        ByteBlock data = new ByteBlock(Arrays.copyOf(inp, inp.length), currOffset, System.currentTimeMillis());
        return  log.add(data);

    }

    public byte[] getData(int i){
        return log.get(i).array;
    }
    public int getPartition() {
        return partition;
    }

    public String getTopicName() {
        return topicName;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    class ByteBlock {
        byte[] array;
        int offset;
        long timeStamp;
        public ByteBlock(byte[] array, int offset, long timeStamp){
            this.offset = offset;
            this.timeStamp = timeStamp;
            this.array = array;
        }
    }
}
