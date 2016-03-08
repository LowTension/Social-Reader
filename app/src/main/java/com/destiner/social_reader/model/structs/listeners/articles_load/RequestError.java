package com.destiner.social_reader.model.structs.listeners.articles_load;

public class RequestError {
    public enum Code {
        NO_CONNECTION,
        NO_REPLY
    }

    private Code code;

    public RequestError(Code code) {
        this.code = code;
    }

    public Code getCode() {
        return code;
    }
}
