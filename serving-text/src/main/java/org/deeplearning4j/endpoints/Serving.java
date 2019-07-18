package org.deeplearning4j.endpoints;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.deeplearning4j.classes.WebContent;
import org.deeplearning4j.serving.ModelHolder;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("serving")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class Serving {

    /**
     * This endpoint takes JSON representation of WebContent, passes into to
     * @param content
     * @return
     */
    @POST
    @Path("text")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response processAddress(@NonNull WebContent content) {
        val topic = ModelHolder.getInstance().evaluateTopic(content.getContent());

        return Response.ok(topic).build();
    }
}
