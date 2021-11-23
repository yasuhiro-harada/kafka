package com.example.tmf;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;

import java.lang.Thread;
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
        Duration duration = Duration.ofMillis(1000);

        while (received.size() <= 100) {
            Thread.sleep(10);
            ConsumerRecords<String, String> records = null;
            records = consumer.poll(duration);
            if(records.count() <= 0){
                continue;
            }
            records.forEach(received::add);
            received = sort(received);
            for(int i = 0; i < received.size(); i++){
                System.out.println("key : " + received.get(i).key());
                System.out.println("value : " + received.get(i).value());
            }
            received.clear();
        }
        consumer.close();
        return RepeatStatus.FINISHED;
    }

    private List<ConsumerRecord<String, String>> sort(List<ConsumerRecord<String, String>> fromList)
    {
        if(fromList.size() <= 1){
            return fromList;
        }
        List<ConsumerRecord<String, String>> toList = new ArrayList<>();
        return nestedSort(fromList, toList);
    }
    private List<ConsumerRecord<String, String>> nestedSort(List<ConsumerRecord<String, String>> fromList, List<ConsumerRecord<String, String>> toList)
    {
        long timestamp = fromList.get(0).timestamp();
        int minIndex = 0;
        for(int i = 1; i < fromList.size(); i++){
            if(timestamp > fromList.get(i).timestamp()){
                timestamp = fromList.get(i).timestamp();
                minIndex = i;
            }
        }
        toList.add(fromList.get(minIndex));
        fromList.remove(minIndex);
        if(fromList.size() > 0){
            nestedSort(fromList, toList);
        }
        return toList;
    }
}
