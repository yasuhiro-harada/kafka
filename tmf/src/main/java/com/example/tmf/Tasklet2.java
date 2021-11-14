package com.example.tmf;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;


@Component
@StepScope
public class Tasklet2 implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contoribution, ChunkContext cuhnkcontext) throws Exception{
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, "my-consumer");
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        KafkaConsumer<String, String> consumer =
                new KafkaConsumer<>(properties, new StringDeserializer(), new StringDeserializer());

        List<ConsumerRecord<String, String>> received = new ArrayList<>();

        consumer.subscribe(Arrays.asList("topic"));

        while (received.size() < 10) {
            Duration duration = Duration.ofMillis(1000);
            ConsumerRecords<String, String> records = consumer.poll(duration);

            records.forEach(received::add);
            System.out.println("consume : value");
        }
        consumer.close();
        return RepeatStatus.FINISHED;
    }
}
