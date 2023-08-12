package com.rratan.storm.producer;

import com.rratan.storm.common.Serializer;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

public class ProducerWorker<T> {
   private ExecutorService ex;
   private final Serializer<T> serializer;
   private int numOfCores;

    ProducerWorker(int numOfCores, Serializer<T> serializer,  LinkedBlockingDeque<ProduceRecord> available){
        this.ex =  Executors.newFixedThreadPool(numOfCores);
        this.numOfCores = numOfCores;
        this.serializer = serializer;
        startWorkers(available);
    }

    public void startWorkers(LinkedBlockingDeque<ProduceRecord> available){
        int i = 0;
        while(i< numOfCores){
            ex.submit(new MessageHandler(available,serializer));
        }
    }

    public void signal(){
        ex.notifyAll();
    }


    public class MessageHandler implements Runnable {
        private LinkedBlockingDeque<ProduceRecord> available;
        private final Serializer<T> serializer;

        public MessageHandler(  LinkedBlockingDeque<ProduceRecord> available, Serializer<T> serializer){
            this.available = available;
            this.serializer = serializer;
        }

        @Override
        public void run() {
            while(true){
                try{
                ProduceRecord pe = available.poll();
                if(pe != null){
                    System.out.println("Producing data to "+ pe.getTp().getTopicName()+"::"+pe.getTp().getPartition());
                    byte[] data = serializer.serialize((T) pe.getData());
                    pe.getTp().add(data);
                }
                Thread.sleep(100);
            }catch (InterruptedException e){
                    e.printStackTrace();
                }

        }
        }
    }
}
