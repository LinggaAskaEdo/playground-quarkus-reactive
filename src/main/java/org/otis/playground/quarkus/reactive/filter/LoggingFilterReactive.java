package org.otis.playground.quarkus.reactive.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vertx.core.http.HttpServerRequest;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.server.ServerRequestFilter;
import org.jboss.resteasy.reactive.server.ServerResponseFilter;
import org.otis.playground.quarkus.reactive.model.dto.ResponseDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

@Provider
public class LoggingFilterReactive {
    private static final Logger LOGGER = Logger.getLogger(LoggingFilterReactive.class.getName());

    @Context
    HttpServerRequest serverRequest;

    @Inject
    ObjectMapper objectMapper;

    @ServerRequestFilter
    public void requestFilter(ContainerRequestContext requestContext) {
        final String method = requestContext.getMethod();
        final String address = serverRequest.remoteAddress().host();
        final String port = String.valueOf(serverRequest.remoteAddress().port());
        final String path = requestContext.getUriInfo().getPath();
        final String param = getRequestParam(serverRequest);

        LOGGER.infof("event=START, httpMethod=%s, remoteHost=%s, remotePort=%s, requestPath=%s, requestParam=%s", method, address, port, path, param);
    }

    private String getRequestParam(HttpServerRequest serverRequest) {
        List<Map.Entry<String, String>> requestParamNames = serverRequest.params().entries();
        StringJoiner joiner = new StringJoiner(",", "[", "]");

        for (Map.Entry<String, String> data : requestParamNames) {
            String requestParamName = data.getKey();
            String requestParamValue = data.getValue();
            joiner.add(requestParamName + "=" + requestParamValue);
        }

        return joiner.toString();
    }

    @ServerResponseFilter
    public void responseFilter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) throws IOException {
        final String method = serverRequest.method().toString();
        final String address = serverRequest.remoteAddress().host();
        final String port = String.valueOf(serverRequest.remoteAddress().port());
        final String path = serverRequest.path();
        final int status = responseContext.getStatus();
        final String body = getResponseBody(responseContext.getEntity());

        LOGGER.infof("event=END, httpMethod=%s, remoteHost=%s, remotePort=%s, requestPath=%s, responseStatus=%s, responseBody=%s", method, address, port, path, status, body);
    }

    private String getResponseBody(Object entity) throws JsonProcessingException {
        ResponseDTO responseDTO = objectMapper.readValue(objectMapper.writeValueAsString(entity), ResponseDTO.class);

        return objectMapper.writeValueAsString(responseDTO);
    }
}
