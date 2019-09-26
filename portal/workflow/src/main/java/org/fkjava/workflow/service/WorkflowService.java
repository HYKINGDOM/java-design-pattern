package org.fkjava.workflow.service;

import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.data.domain.Page;

public interface WorkflowService {
    Page<ProcessDefinition> findDefinitions(String keyword, int pageNumber, int pageSize);
}
