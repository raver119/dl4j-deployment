package org.deeplearning4j.kafka;

import kong.unirest.Unirest;
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
public class ConsumerThread extends Thread implements Runnable {
    private final static String TOPIC_IN = "addresses";
    private final static String TOPIC_OUT = "content";
    private final static String BOOTSTRAP_SERVERS = "10.5.0.5:9092";

    public ConsumerThread() {
        super();

        setDaemon(true);
        setName("Fetcher Kafka consumer thread");
    }

    @Override
    public void run() {

        val properties = new Properties();
        properties.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        properties.put("group.id", "fetcherPod");
        properties.put("acks", "all");
        properties.put("retries", 0);

        val consumer = new KafkaConsumer<Integer, String>(properties, new IntegerDeserializer(), new StringDeserializer());
        val producer = new KafkaProducer<Integer, WebContent>(properties, new IntegerSerializer(), new WebContent.Serializer());

        // pew-pew
        consumer.subscribe(Collections.singleton(TOPIC_IN));

        while (true) {
            // fetch addresses from Kafka
            val records = consumer.poll(Duration.ofMillis(5000));

            if (!records.isEmpty()) {
                val iterator = records.iterator();

                // we'll fetch everything from iterator first
                val list = new ArrayList<String>();
                while (iterator.hasNext()) {
                    val record = iterator.next();
                    list.add(record.value());
                }

                // now we'll request web content for every URL we have
                list.parallelStream().forEach(u -> {
                    try {
                        var res = Unirest.get(u).asString().getBody();

                        // Ugly, but works for toy example
                        if (!res.contains("RSS")) {
                            log.info("Not an RSS feed");
                            return;
                        }

                        val wc = WebContent.builder()
                                .type(ContentType.TEXT)
                                .content(res)
                                .sourceURL(u)
                                .build();

                        log.info("Successfully fetched content from [{}]", u);

                        producer.send(new ProducerRecord<>(TOPIC_OUT, wc.hashCode(), wc));
                    } catch (Exception e) {
                        log.error("Failed to fetch URL: [" + u + "]", e);
                    }
                });
            }

        }
    }
}
