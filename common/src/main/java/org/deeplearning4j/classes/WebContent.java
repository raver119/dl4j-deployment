package org.deeplearning4j.classes;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WebContent {
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
}
