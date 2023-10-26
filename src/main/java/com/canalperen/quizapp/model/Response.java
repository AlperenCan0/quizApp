package com.canalperen.quizapp.model;

import lombok.Data;

@Data
public class Response {
    private Integer id;
    private String response;
    public Response() {};

    public Response(int id, String response) {
        this.id = id;
        this.response = response;
    }
}
