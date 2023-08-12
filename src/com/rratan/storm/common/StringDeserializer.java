package com.rratan.storm.common;

public class StringDeserializer implements Deserializer<String>{
    @Override
    public String deserialize(byte[] inp) {
        return inp.toString();
    }
}
