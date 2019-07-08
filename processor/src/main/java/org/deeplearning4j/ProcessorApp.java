package org.deeplearning4j;

/**
 * This application reads web content from Apache Kafka queue and does one of two things
 * 1) if that's text content - it applies sentiment classification to it
 * 2) if that's image content - it applies image classification to it
 */
public class ProcessorApp
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
    }
}
