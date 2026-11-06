package com.aabid.aisandbox.service;
import com.aabid.aisandbox.dto.AgentRunResponse;
import com.aabid.aisandbox.dto.RunAgentRequest;
import com.aabid.aisandbox.model.AgentRunEntity;
import com.aabid.aisandbox.repository.AgentRunRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.util.*;
@Service
public class AgentService {
 private final AiPlanner planner;private final AgentRunRepository repo;private final ObjectMapper mapper;
 public AgentService(AiPlanner p,AgentRunRepository r,ObjectMapper m){planner=p;repo=r;mapper=m;}
 public AgentRunResponse run(RunAgentRequest req){
  AiPlanner.Plan p=planner.plan(req.instruction());String json;
  try{json=mapper.writeValueAsString(p.steps());}catch(JsonProcessingException e){throw new IllegalStateException(e);}
  AgentRunEntity saved=repo.save(new AgentRunEntity(req.instruction(),"PLANNED",p.mode(),json));
  return new AgentRunResponse(saved.getId(),saved.getStatus(),saved.getPlanningMode(),saved.getInstruction(),p.steps(),saved.getCreatedAt());
 }
 public AgentRunResponse get(UUID id){
  AgentRunEntity r=repo.findById(id).orElseThrow(()->new IllegalArgumentException("Agent run not found: "+id));
  try{List<String> steps=mapper.readValue(r.getPlanJson(),mapper.getTypeFactory().constructCollectionType(List.class,String.class));
   return new AgentRunResponse(r.getId(),r.getStatus(),r.getPlanningMode(),r.getInstruction(),steps,r.getCreatedAt());
  }catch(JsonProcessingException e){throw new IllegalStateException("Stored plan is invalid",e);}
 }
 public List<AgentRunResponse> list(){return repo.findAll().stream().map(r->get(r.getId())).toList();}
}
