package com.destiner.social_reader.model.structs.listeners.articles_load;

import com.vk.sdk.api.VKError;

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

    /**
     * An adapter from VKError to the inner Error model.
     * @param errorCode VKError code
     * @return adapted error
     */
    public static RequestError getError(int errorCode) {
        switch (errorCode) {
            case VKError.VK_REQUEST_HTTP_FAILED:
                return new RequestError(Code.NO_CONNECTION);
        }
        return null;
    };
}
