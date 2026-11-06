package com.aabid.aisandbox.dto;
import java.time.Instant;import java.util.*; 
public record AgentRunResponse(UUID runId,String status,String planningMode,String instruction,List<String> steps,Instant createdAt){}
