package ru.itmo.server.src.containers;

import ru.itmo.common.connection.Request;
import ru.itmo.common.connection.Response;

public class requestResponse {
    private final Request request;
    private final Response response;

    public requestResponse(Request request,Response response){
        this.request = request;
        this.response = response;
    }

    public Request getRequest() {
        return request;
    }

    public Response getResponse() {
        return response;
    }
}
