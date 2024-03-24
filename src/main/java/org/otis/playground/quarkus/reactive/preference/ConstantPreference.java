package org.otis.playground.quarkus.reactive.preference;

public class ConstantPreference {
    public static final String RESPONSE_MESSAGE_OK = "Request was successful";
    public static final String RESPONSE_MESSAGE_INTERNAL_SERVER_ERROR = "There is planned service outage. We should specify response headers or error object with more details on service outage";
    public static final String RESPONSE_MESSAGE_NOT_FOUND = "Resource not found";
    public static final String RESPONSE_MESSAGE_NOT_ALLOWED = "Method not allowed";
    public static final String RESPONSE_MESSAGE_BAD_REQUEST = "Syntax error, e.g. request is missing required parameters/attributes or parameter values are of incorrect type";
}
