package org.fkjava.workflow.service;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class WorkflowServiceImpl implements WorkflowService {

    @Autowired
    private RepositoryService repositoryService;

    @Override
    public Page<ProcessDefinition> findDefinitions(String keyword, int pageNumber, int pageSize) {
        // 调用流程引擎的API查询相关的数据，然后转换为Page对象
        ProcessDefinitionQuery query = this.repositoryService.createProcessDefinitionQuery();
        if (!StringUtils.isEmpty(keyword)) {
            query.processDefinitionNameLike("%" + keyword + "%");
        }
        // 永远查询最后一个版本的流程定义
        query.latestVersion();

        // 对结果进行排序
        query.orderByProcessDefinitionName().asc();

        // 查询总记录数
        long count = query.count();

        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        // 分页查询流程定义
        List<ProcessDefinition> definitions = query.listPage((int) pageable.getOffset(), pageable.getPageSize());

        Page<ProcessDefinition> page = new PageImpl<>(definitions, pageable, count);
        return page;
    }
}
