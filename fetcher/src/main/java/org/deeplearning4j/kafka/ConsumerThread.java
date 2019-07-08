package org.deeplearning4j.kafka;

import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import lombok.var;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.deeplearning4j.classes.WebContent;

import java.time.Duration;
import java.util.ArrayList;
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
        properties.put("acks", "all");
        properties.put("retries", 0);

        val consumer = new KafkaConsumer<Integer, String>(properties);
        val producer = new KafkaProducer<Integer, WebContent>(properties);

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
                        val wc = WebContent.builder()
                                .content(res)
                                .sourceURL(u)
                                .build();

                        producer.send(new ProducerRecord<>(TOPIC_OUT, wc.hashCode(), wc));
                    } catch (Exception e) {
                        log.error("Failed to fetch URL: [" + u + "]", e);
                    }
                });
            }

        }
    }
}
