package com.aabid.aisandbox.model;
import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;
@Entity @Table(name="agent_runs")
public class AgentRunEntity {
 @Id private UUID id;
 @Column(nullable=false,length=2000) private String instruction;
 @Column(nullable=false,length=30) private String status;
 @Column(nullable=false,length=40) private String planningMode;
 @Column(nullable=false,length=8000) private String planJson;
 @Column(nullable=false) private Instant createdAt;
 protected AgentRunEntity(){}
 public AgentRunEntity(String i,String s,String m,String j){id=UUID.randomUUID();instruction=i;status=s;planningMode=m;planJson=j;createdAt=Instant.now();}
 public UUID getId(){return id;} public String getInstruction(){return instruction;} public String getStatus(){return status;}
 public String getPlanningMode(){return planningMode;} public String getPlanJson(){return planJson;} public Instant getCreatedAt(){return createdAt;}
}
