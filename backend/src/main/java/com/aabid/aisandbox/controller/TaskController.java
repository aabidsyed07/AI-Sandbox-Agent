package com.aabid.aisandbox.controller;
import com.aabid.aisandbox.dto.*;import com.aabid.aisandbox.model.*;import com.aabid.aisandbox.service.TaskService;
import jakarta.validation.Valid;import org.springframework.web.bind.annotation.*;import java.util.*;import java.util.UUID;
@RestController @RequestMapping("/api/tasks") public class TaskController{
 private final TaskService service;public TaskController(TaskService s){service=s;}
 @GetMapping public List<TaskEntity> list(){return service.list();}
 @PostMapping public TaskEntity create(@Valid @RequestBody CreateTaskRequest r){return service.create(r);}
 @GetMapping("/{id}") public TaskEntity get(@PathVariable UUID id){return service.get(id);}
 @PatchMapping("/{id}/status") public TaskEntity status(@PathVariable UUID id,@Valid @RequestBody UpdateStatusRequest r){return service.updateStatus(id,r.status());}
 @DeleteMapping("/{id}") public void delete(@PathVariable UUID id){service.delete(id);}
}
