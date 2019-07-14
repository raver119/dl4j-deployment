package org.deeplearning4j.classes;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.val;
import org.apache.kafka.common.serialization.Serializer;

import java.io.*;

@Data
@Builder
public class WebContent implements Serializable {
    private static final long serialVersionUID = 1;

    /**
     * Content type for entity
     */
    private ContentType type;

    /**
     * Base64-encoded content
     */
    private String content;

    /**
     * URL of the source
     */
    private String sourceURL;

    /**
     * For text content this field will store detected sentiment type
     */
    private Sentiment sentiment;

    /**
     * For image content this field will store detected image class
     */
    private ObjectGroup objectGroup;



    public static class Serializer implements org.apache.kafka.common.serialization.Serializer<WebContent> {
        @Override
        public void close() {
            //
        }

        @Override
        public byte[] serialize(String s, @NonNull WebContent webContent) {
            try {
                val baos = new ByteArrayOutputStream();
                val oos = new ObjectOutputStream(baos);
                oos.writeObject(webContent);

                return baos.toByteArray();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static class Deserializer implements org.apache.kafka.common.serialization.Deserializer<WebContent> {
        @Override
        public void close() {
            //
        }

        @Override
        public WebContent deserialize(String s, byte[] bytes) {
            try {
                val bais = new ByteArrayInputStream(bytes);
                val ois = new ObjectInputStream(bais);

                return (WebContent) ois.readObject();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
