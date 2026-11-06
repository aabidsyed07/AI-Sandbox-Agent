package com.aabid.aisandbox.repository;
import com.aabid.aisandbox.model.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
public interface TaskRepository extends JpaRepository<TaskEntity,UUID>{}
