package com.aabid.aisandbox.service;
import com.aabid.aisandbox.dto.CreateTaskRequest;
import com.aabid.aisandbox.model.*;
import com.aabid.aisandbox.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
@Service
public class TaskService {
 private final TaskRepository repo; public TaskService(TaskRepository r){repo=r;}
 public List<TaskEntity> list(){return repo.findAll();}
 public TaskEntity get(UUID id){return repo.findById(id).orElseThrow(()->new IllegalArgumentException("Task not found: "+id));}
 public TaskEntity create(CreateTaskRequest r){return repo.save(new TaskEntity(r.title().trim(),r.description()));}
 @Transactional public TaskEntity updateStatus(UUID id,TaskStatus s){TaskEntity t=get(id);t.setStatus(s);return t;}
 public void delete(UUID id){if(!repo.existsById(id))throw new IllegalArgumentException("Task not found: "+id);repo.deleteById(id);}
}
