package org.deeplearning4j.kafka;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.deeplearning4j.classes.ContentType;
import org.deeplearning4j.classes.WebContent;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

@Slf4j
public class ProcessorConsumerThread extends Thread implements Runnable {
    private final static String TOPIC_IN = "content";
    private final static String TOPIC_OUT = "classified";
    private final static String BOOTSTRAP_SERVERS = "10.5.0.5:9092";

    public ProcessorConsumerThread() {
        super();

        setDaemon(true);
        setName("Processor Kafka consumer thread");
    }

    @Override
    public void run() {

        val properties = new Properties();
        properties.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        properties.put("group.id", "processorPod");
        properties.put("acks", "all");
        properties.put("retries", 0);

        val consumer = new KafkaConsumer<Integer, WebContent>(properties, new IntegerDeserializer(), new WebContent.Deserializer());
        val producer = new KafkaProducer<Integer, WebContent>(properties, new IntegerSerializer(), new WebContent.Serializer());

        // pew-pew
        consumer.subscribe(Collections.singleton(TOPIC_IN));

        while (true) {
            // fetch content from Kafka
            val records = consumer.poll(Duration.ofMillis(5000));

            if (!records.isEmpty()) {
                val iterator = records.iterator();

                // we'll fetch everything from iterator first
                val list = new ArrayList<WebContent>();
                while (iterator.hasNext()) {
                    val record = iterator.next();
                    list.add(record.value());
                }

                // now we'll query neural network for each text we have
                list.forEach(u -> {
                    try {
                        //
                    } catch (Exception e) {
                        log.error("Failed to process URL: [" + u.getSourceURL() + "]", e);
                    }
                });
            }
        }
    }
}
