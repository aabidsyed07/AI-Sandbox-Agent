package com.aabid.aisandbox.dto;
import jakarta.validation.constraints.*;
public record CreateTaskRequest(@NotBlank @Size(max=160) String title,@Size(max=2000) String description){}
