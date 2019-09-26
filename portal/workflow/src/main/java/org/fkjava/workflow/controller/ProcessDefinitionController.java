package org.fkjava.workflow.controller;

import org.activiti.engine.repository.ProcessDefinition;
import org.fkjava.workflow.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/process/definition")
public class ProcessDefinitionController {

    @Autowired
    private WorkflowService workflowService;

    @GetMapping
    public Page<ProcessDefinition> search(
            @RequestParam(name = "kw", required = false) String keyword,
            @RequestParam(name = "pn", defaultValue = "0") int pageNumber,
            @RequestParam(name = "ps", defaultValue = "20") int pageSize
    ) {
        return this.workflowService.findDefinitions(keyword, pageNumber, pageSize);
    }
}
