package com.destiner.social_reader.model.structs.listeners.articles_load;

public class Response {
    private RequestError error;
    private Content content;

    public Response(RequestError error) {
        this.error = error;
    }

    public Response(Content content) {
        this.content = content;
    }

    public RequestError getError() {
        return error;
    }

    public Content getContent() {
        return content;
    }
}
