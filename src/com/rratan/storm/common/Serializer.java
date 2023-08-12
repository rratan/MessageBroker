package com.rratan.storm.common;

public interface Serializer<T> {

    byte[] serialize(T inp);
}
