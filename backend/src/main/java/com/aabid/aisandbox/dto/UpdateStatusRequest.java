package com.aabid.aisandbox.dto;
import com.aabid.aisandbox.model.TaskStatus;
import jakarta.validation.constraints.NotNull;
public record UpdateStatusRequest(@NotNull TaskStatus status){}
