package org.deeplearning4j.endpoints;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.deeplearning4j.classes.WebContent;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Properties;

/**
 * These REST API endpoints should be POST methods obviously, but since that's example deployment - we'll go with GET, since it's easier to send without any additional tooling
 */
@Path("rest")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class Rest {

    private final static String TOPIC_URL = "addresses";
    private final static String TOPIC_CONTENT = "content";
    private final static String BOOTSTRAP_SERVERS = "10.5.0.5:9092";

    protected Properties getProperties() {
        val properties = new Properties();
        properties.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        properties.put("acks", "all");
        properties.put("retries", 0);
        properties.put("group.id", "fetcherPod");

        return properties;
    }

    /**
     * We're putting address into Fetcher queue here.
     * Fetcher will eventually issue HTTP/s request to fetch content out of it
     *
     * @param address
     * @return
     */
    @GET
    @Path("address")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response processAddress(@QueryParam("url") String address) {
        log.info("Sending [{}] to the Fetcher queue", address);
        val properties = getProperties();

        val producer = new KafkaProducer<Integer, String>(properties, new IntegerSerializer(), new StringSerializer());
        val record = new ProducerRecord<Integer, String>(TOPIC_URL, address.hashCode(), address);

        producer.send(record, new Callback() {
            @Override
            public void onCompletion(RecordMetadata recordMetadata, Exception e) {
                log.info("Completion callback");
            }
        });

        return Response.ok("URL Received: [" + address + "]").build();
    }

    /**
     * We're bypassing Fetcher here, by putting text content directly into Processor queue
     *
     * @param text
     * @return
     */
    @GET
    @Path("textContent")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response processText(@QueryParam("txt") String text) {
        log.info("Sending [{}] to the Processing queue", text);
        val properties = getProperties();

        val content = WebContent.builder().content(text).build();

        val producer = new KafkaProducer<Integer, WebContent>(properties, new IntegerSerializer(), new WebContent.Serializer());
        val record = new ProducerRecord<Integer, WebContent>(TOPIC_CONTENT, content.hashCode(), content);

        producer.send(record);

        return Response.ok().build();
    }


    @GET
    @Path("test")
    public Response getTest() {
        return Response.ok().build();
    }
}