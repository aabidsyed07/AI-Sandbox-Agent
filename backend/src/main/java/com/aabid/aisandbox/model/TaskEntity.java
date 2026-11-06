package com.aabid.aisandbox.model;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="tasks")
public class TaskEntity {
 @Id private UUID id;
 @Column(nullable=false,length=160) private String title;
 @Column(length=2000) private String description;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=20) private TaskStatus status;
 @Column(nullable=false) private Instant createdAt;
 @Column(nullable=false) private Instant updatedAt;
 protected TaskEntity(){}
 public TaskEntity(String title,String description){this.id=UUID.randomUUID();this.title=title;this.description=description;this.status=TaskStatus.PENDING;this.createdAt=Instant.now();this.updatedAt=createdAt;}
 public void setStatus(TaskStatus s){status=s;updatedAt=Instant.now();}
 public UUID getId(){return id;} public String getTitle(){return title;} public String getDescription(){return description;}
 public TaskStatus getStatus(){return status;} public Instant getCreatedAt(){return createdAt;} public Instant getUpdatedAt(){return updatedAt;}
}
