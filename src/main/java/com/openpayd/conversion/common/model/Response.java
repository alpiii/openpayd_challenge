package com.openpayd.conversion.common.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

@Getter
@JsonIgnoreProperties({"httpStatus"})
public class Response {
    private HttpStatus httpStatus;

    private Map<String, Object> data;

    private Response(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        data = new HashMap<>();
    }

    public static Response error(HttpStatus status) {
        return new Response(status).add("error", status);
    }

    public static Response success() {
        return new Response(HttpStatus.OK);
    }

    public Response add(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public Response message(Object value) {
        this.data.put("message", value);
        return this;
    }

    public ResponseEntity<Response> build() {
        return new ResponseEntity<>(this, this.httpStatus);
    }
}
