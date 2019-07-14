package org.deeplearning4j;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.deeplearning4j.kafka.ProcessorConsumerThread;

/**
 * This application reads web content from Apache Kafka queue and does one of two things
 * 1) if that's text content - it applies sentiment classification to it
 * 2) if that's image content - it applies image classification to it
 */
@Slf4j
public class ProcessorApp
{
    public static void main( String[] args ) throws Exception{
        log.info("Starting Processor app");

        val consumer = new ProcessorConsumerThread();
        consumer.start();
        consumer.join();
    }
}
