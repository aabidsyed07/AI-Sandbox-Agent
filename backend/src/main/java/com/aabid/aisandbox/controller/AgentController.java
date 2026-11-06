package com.aabid.aisandbox.controller;
import com.aabid.aisandbox.dto.*;import com.aabid.aisandbox.service.AgentService;
import jakarta.validation.Valid;import org.springframework.web.bind.annotation.*;import java.util.*;import java.util.UUID;
@RestController @RequestMapping("/api/agent") public class AgentController{
 private final AgentService service;public AgentController(AgentService s){service=s;}
 @PostMapping("/run") public AgentRunResponse run(@Valid @RequestBody RunAgentRequest r){return service.run(r);}
 @GetMapping("/runs") public List<AgentRunResponse> list(){return service.list();}
 @GetMapping("/runs/{id}") public AgentRunResponse get(@PathVariable UUID id){return service.get(id);}
}
