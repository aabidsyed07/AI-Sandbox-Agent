package com.aabid.aisandbox.service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.*;
@Service
public class AiPlanner {
 private final String key,model; private final RestClient client;
 public AiPlanner(@Value("${app.ai.base-url}")String url,@Value("${app.ai.api-key:}")String key,@Value("${app.ai.model}")String model){
  this.key=key;this.model=model;client=RestClient.builder().baseUrl(url).build();
 }
 public Plan plan(String instruction){
  if(key==null||key.isBlank())return demo("DEMO");
  try{
   Map<String,Object> body=Map.of("model",model,"temperature",0.2,"messages",List.of(
    Map.of("role","system","content","Return five short safe numbered planning steps. Never suggest arbitrary shell execution."),
    Map.of("role","user","content",instruction)));
   Map<?,?> response=client.post().uri("/v1/chat/completions").contentType(MediaType.APPLICATION_JSON)
    .header("Authorization","Bearer "+key).body(body).retrieve().body(Map.class);
   Object choices=response==null?null:response.get("choices");
   if(choices instanceof List<?> list&&!list.isEmpty()&&list.get(0) instanceof Map<?,?> c&&c.get("message") instanceof Map<?,?> m&&m.get("content") instanceof String s){
    List<String> steps=Arrays.stream(s.split("\\R")).map(x->x.replaceFirst("^\\s*\\d+[.)-]?\\s*","").trim()).filter(x->!x.isBlank()).limit(8).toList();
    if(!steps.isEmpty())return new Plan("LLM",steps);
   }
  }catch(Exception ignored){return demo("DEMO_FALLBACK");}
  return demo("DEMO_FALLBACK");
 }
 private Plan demo(String mode){return new Plan(mode,List.of("Understand the requested outcome","Break the work into verifiable steps","Identify required tools and safety constraints","Execute approved operations only","Verify the result and report what happened"));}
 public record Plan(String mode,List<String> steps){}
}
