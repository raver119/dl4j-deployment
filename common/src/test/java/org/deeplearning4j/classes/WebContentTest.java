package org.deeplearning4j.classes;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.junit.Test;

import static org.junit.Assert.*;

@Slf4j
public class WebContentTest {
    @Test
    public void testSerDe_1() {
        val content = WebContent.builder()
                .sourceURL("url")
                .content("alpha")
                .build();


        val serializer = new WebContent.Serializer();
        val deserializer = new WebContent.Deserializer();

        val serialized = serializer.serialize("topic", content);
        val deserialized = deserializer.deserialize("topic", serialized);

        assertEquals(content, deserialized);
        assertEquals(content.getContent(), deserialized.getContent());
        assertEquals(content.getSourceURL(), deserialized.getSourceURL());
    }
}