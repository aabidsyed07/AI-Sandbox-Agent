package com.aabid.aisandbox.repository;
import com.aabid.aisandbox.model.AgentRunEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface AgentRunRepository extends JpaRepository<AgentRunEntity,UUID>{}
