package com.experian.comp.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;

public interface ConsumerCallback {
    void afterReceive(ConsumerRecord<String, String> records);
}
