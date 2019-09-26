package org.fkjava.workflow.config;

//import org.springframework.boot.SpringBootConfiguration;

import org.activiti.engine.*;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.activiti.spring.SpringProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
//@SpringBootConfiguration
public class WorkflowConfig {

    // 基于Spring集成的流程引擎配置对象
    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration(
            DataSource dataSource,
            PlatformTransactionManager transactionManager
    ) {
        SpringProcessEngineConfiguration conf = new SpringProcessEngineConfiguration();
        conf.setDataSource(dataSource);
        conf.setTransactionManager(transactionManager);
        conf.setDatabaseSchemaUpdate("true");
        return conf;
    }

    // 产生流程引擎实例的工厂
    @Bean
    public ProcessEngineFactoryBean processEngineFactoryBean(SpringProcessEngineConfiguration conf) {
        ProcessEngineFactoryBean bean = new ProcessEngineFactoryBean();
        bean.setProcessEngineConfiguration(conf);
        return bean;
    }

    // 产生流程引擎里面的核心服务：存储服务、运行时服务、表单服务、用户身份服务、任务服务、历史数据服务。
    // 这些服务，都是框架级别的服务，我们在使用的时候，直接当做DAO使用即可。
    // 大部分的时候，不需要用户身份服务
    @Bean
    public RepositoryService repositoryService(ProcessEngine engine) {
        return engine.getRepositoryService();
    }

    @Bean
    public RuntimeService runtimeService(ProcessEngine engine) {
        return engine.getRuntimeService();
    }

    @Bean
    public FormService formService(ProcessEngine engine) {
        return engine.getFormService();
    }

    @Bean
    public TaskService taskService(ProcessEngine engine) {
        return engine.getTaskService();
    }

    @Bean
    public HistoryService historyService(ProcessEngine engine) {
        return engine.getHistoryService();
    }
}
