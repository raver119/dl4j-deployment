package org.deeplearning4j.endpoints;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Properties;


@Path("addresses")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Slf4j
public class Addresses {

    private final static String TOPIC = "addresses";
    private final static String BOOTSTRAP_SERVERS = "10.5.0.5:9092";

    @GET
    @Path("asString")
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response processAddress(@QueryParam("address") String address) {
        val properties = new Properties();
        properties.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        properties.put("acks", "all");
        properties.put("retries", 0);

        val producer = new KafkaProducer<Integer, String>(properties);
        val record = new ProducerRecord<Integer, String>(TOPIC, Integer.valueOf(address.hashCode()), address);

        producer.send(record);

        return Response.ok().build();
    }
}
;