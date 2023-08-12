package com.rratan.storm.common;

public interface Deserializer <T>{
    T deserialize(byte[] inp);
}
