package com.momo.task.manager.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.momo.task.manager.model.Label;
import com.momo.task.manager.model.Task;

import java.io.IOException;
import java.util.Set;

public class CustomTaskSerializer extends JsonSerializer<Task> {
    @Override
    public void serialize(Task task, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        // Customize the serialization process here
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("taskName", task.getTaskName());
        jsonGenerator.writeStringField("description", task.getDescription());
        jsonGenerator.writeStringField("type", task.getType());
        jsonGenerator.writeObjectField("status", task.getStatus().getStatusId());
        jsonGenerator.writeStringField("startDate", task.getStartDate().toString());
        jsonGenerator.writeStringField("endDate", task.getEndDate().toString());
        jsonGenerator.writeObjectField("project", task.getProject().getProjectId());
        jsonGenerator.writeObjectField("assigneeId", task.getAssigneeId().getUserId());
        jsonGenerator.writeObjectField("reporterId", task.getReporterId().getUserId());
        jsonGenerator.writeObjectField("stageId", task.getStageId().getId());
        jsonGenerator.writeBooleanField("updatedFlag", task.isUpdatedFlg());
        jsonGenerator.writeObjectField("updatedDate", task.getUpdatedDate());
        jsonGenerator.writeObjectField("updatedStageDate", task.getUpdatedStageDate());
        jsonGenerator.writeObjectField("updatedStatusDate", task.getUpdatedStatusDate());

        jsonGenerator.writeEndObject();
    }
}
