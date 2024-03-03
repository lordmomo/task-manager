package com.momo.task.manager.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.momo.task.manager.model.Comment;

import java.io.IOException;

public class CustomCommentSerializer extends JsonSerializer<Comment> {
    @Override
    public void serialize(Comment comment, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeNumberField("commentId", comment.getCommentId());
        jsonGenerator.writeObjectField("userId", comment.getUserId());
        jsonGenerator.writeStringField("message", comment.getMessage());
        jsonGenerator.writeStringField("messagePostDate", comment.getMessagePostDate().toString());
        // Assuming fileData is a byte array
        if (comment.getFileData() != null) {
            jsonGenerator.writeBinaryField("fileData", comment.getFileData());
        } else {
            jsonGenerator.writeNullField("fileData");
        }
        jsonGenerator.writeEndObject();
    }
}
