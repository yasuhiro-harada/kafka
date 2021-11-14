package com.example.tmf;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

@Component
@StepScope
public class Tasklet1 implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contoribution, ChunkContext cuhnkcontext) throws Exception{
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");

        try (KafkaProducer<String, String> producer =
                     new KafkaProducer<>(properties, new StringSerializer(), new StringSerializer())) {

            IntStream
                    .rangeClosed(1, 10)
                    .forEach(i -> {
                        try {
                            producer.send(new ProducerRecord<>("topic", "key" + i, "value" + i)).get();
                            System.out.println("produce : value" + i);
                        } catch (InterruptedException | ExecutionException e) {
                            throw new RuntimeException(e);
                        }
                    });

        }
        return RepeatStatus.FINISHED;
    }
}
