package com.aabid.aisandbox.dto;
import jakarta.validation.constraints.*;
public record RunAgentRequest(@NotBlank @Size(max=2000) String instruction){}
