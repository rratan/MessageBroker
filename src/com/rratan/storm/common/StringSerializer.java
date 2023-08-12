package com.rratan.storm.common;

import java.nio.charset.StandardCharsets;

public class StringSerializer implements Serializer<String>{

    @Override
    public byte[] serialize(String inp) {
        return inp.getBytes(StandardCharsets.UTF_8);
    }
}
