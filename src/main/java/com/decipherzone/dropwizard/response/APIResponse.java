package com.decipherzone.dropwizard.response;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse {
    private int code;
    private String msg;
    private String status;
    private Object data;
    private List<String> errors;

    private APIResponse(final ResponseBuilder builder) {
        this.code = builder.code;
        this.msg = builder.msg;
        this.status = builder.status;
        this.data = builder.data;
        this.errors = builder.errors;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getStatus() {
        return status;
    }

    public Object getData() {
        return data;
    }

    public List<String> getErrors() {
        return errors;
    }

    public static class ResponseBuilder {
        private int code;
        private String status;
        private String msg;
        private Object data;
        private List<String> errors;

        public ResponseBuilder(int code, String status) {
            this.code = code;
            this.status = status;
        }

        public ResponseBuilder setMsg(String msg) {
            this.msg = msg;
            return this;
        }

        public ResponseBuilder setData(Object data) {
            this.data = data;
            return this;
        }

        public ResponseBuilder setErrors(List<String> errors) {
            this.errors = errors;
            return this;
        }

        public APIResponse build() {
            return new APIResponse(this);
        }
    }

}
