package com.rratan.storm.consumer;

import com.rratan.storm.common.TopicPartition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsumerGroup {
    // group ID of the grp
    private final String groupId;


    private final ConcurrentHashMap<String, AtomicInteger> offsetMp;

    private List<StormConsumer> inGroup;



    public ConsumerGroup(String gid){
        this.groupId = gid;
        this.offsetMp = new ConcurrentHashMap<>();
        this.inGroup = new ArrayList<>();
    }

    public void join(StormConsumer se){
        boolean found = false;
        for(StormConsumer s : inGroup){
            if(s.equals(se)){
                found = true;
                break;
            }
        }
        if(!found){
            inGroup.add(se);
        }
        triggerRebalance(inGroup);
    }

    public void triggerRebalance(List<StormConsumer> lis){
        // stop all consumers
        HashMap<String, List<TopicPartition> > mp = new HashMap<>();
        List<TopicPartition> available = new ArrayList<>();
        for(StormConsumer s: lis){
            available.addAll(s.getTopic().getPartitionList());
        }
        int n = lis.size(), i = 0 , m = available.size();
        while(i <  m ){
            int  j = i%n;
            String key = lis.get(j).getConsumerId();
            if(mp.get(key)==null){
                mp.put(key, new ArrayList<>());
            }
            mp.get(key).add(available.get(i));
            i++;
        }

        for(StormConsumer sc: lis){
            sc.setAssigned(mp.get(sc.getConsumerId()));
        }

    }



    public String partitionToString(TopicPartition tp){
        return tp.getTopicName()+"_"+tp.getPartition();
    }

    public AtomicInteger getPartitionOffset(String s){
        return offsetMp.computeIfAbsent(s, (k)->{
            return  new AtomicInteger(0);
        });
    }

}
