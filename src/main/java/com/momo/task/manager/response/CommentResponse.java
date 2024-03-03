package com.momo.task.manager.response;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentResponse<T> implements Serializable {

    private Integer statusCode;
    private Object message;
    private T data;


    @Override
    public String toString() {
        Gson gson = new Gson();
        return "CommentResponse{" +
                "statusCode=" + statusCode +
                ", message=" + message +
                ", data=" + gson.toJson(data) +
                '}';
    }
}
