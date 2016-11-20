package com.prod.custSuptMaven.config;
/* class notes: rootContext will initiate all other contexts in MVC model, including servletContext(s) in config package. see pg 328,331,349.
 * bootstrap feeds Root, which then feeds Web and RestContexts(Servlets in short).  those in turn support commons for site package.  Root,Web, 
 * and Rest contain items that all downstream classes use
*/
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Hashtable;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.persistence.SharedCacheMode;
import javax.persistence.ValidationMode;
import javax.sql.DataSource;
//see chap 14, pg 395 for good description of this annotation group and its relation to rootContext. affects @Component, @Service
//@Repository, but not @Controller- which is handled by ServletContext
@Configuration
//following annotations support websocket/chat function and SQL
@EnableScheduling
@EnableAsync(
		mode = AdviceMode.PROXY, proxyTargetClass = false,
        order = Ordered.HIGHEST_PRECEDENCE
)
//SQL add.  Ensures async takes precendence over SQL transaction so transaction doesnt get partially cutoff- see pg 609
@EnableTransactionManagement(
        mode = AdviceMode.PROXY, proxyTargetClass = false,
        order = Ordered.LOWEST_PRECEDENCE
)

@ComponentScan(
        basePackages = "com.prod.custSuptMaven.site",
        excludeFilters = @ComponentScan.Filter({Controller.class, ControllerAdvice.class})
)
//in initial projects this class was vacant but starting chap 13/14 it initiates the commons- see pg 348 vs 375/76 as an example
public class RootContextConfiguration
        implements AsyncConfigurer, SchedulingConfigurer
{
    private static final Logger log = LogManager.getLogger();
    private static final Logger schedulingLogger =
            LogManager.getLogger(log.getName() + ".[scheduling]");
    
    //Internationalization not established in this project.  JWA has some Internationalization commons methods in this location.  See chap 15, if additions are needed.
    
    //this bean used w/ @Inject in servletContext as part of jackson.databind functionality.  introduced in chap 10-websockets and chap 14 pg 377
    // for some details on objectMapper()
    @Bean
    public ObjectMapper objectMapper()
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.findAndRegisterModules();
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.configure(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE,
                false);
        return mapper;
    }
    //bean part of springframework intr chap 12. Marshaller supports XML translation- see pg 376
    @Bean
    public Jaxb2Marshaller jaxb2Marshaller()
    {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setPackagesToScan(new String[] { "com.prod.custSuptMaven.site" });
        return marshaller;
    }
    
    //Database. part of chap 20/21 mySQL and Hibernate ORM commons.  Kept the same exact db name & [tomcatserver]/context.xml- name='CustomerSupport'
    //This the java programmatic way (in Spring) to replace persistence.xml.  see pg 604 for details
    @Bean
    public DataSource customerSupportDataSource()
    {
        //in this case using same mysql db as the customer-support-v15 project.  tomcat context.xml is updated
    	JndiDataSourceLookup lookup = new JndiDataSourceLookup();
        return lookup.getDataSource("jdbc/CustomerSupport");
    }
    //Spring version of ORM EntityManagerFactory.  this is referenced by @Entity annotation for classes in site/entities package
    // need both this method and Tomcat/context.xml mods to be able to connect to db.  see pg 632 for customer support project specifically.
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean()
    {
        Map<String, Object> properties = new Hashtable<>();
        properties.put("javax.persistence.schema-generation.database.action",
                "none");

        HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
        adapter.setDatabasePlatform("org.hibernate.dialect.MySQL5InnoDBDialect");

        LocalContainerEntityManagerFactoryBean factory =
                new LocalContainerEntityManagerFactoryBean();
        factory.setJpaVendorAdapter(adapter);
        factory.setDataSource(this.customerSupportDataSource());
        //set (limit) the package(s) to scan for DB POJO's
        factory.setPackagesToScan("com.prod.custSuptMaven.site.entities");
        factory.setSharedCacheMode(SharedCacheMode.ENABLE_SELECTIVE);
        factory.setValidationMode(ValidationMode.NONE);
        factory.setJpaPropertyMap(properties);
        return factory;
    }
    //supports sql/ORM @Transaction annotations for transaction management.  see pg 609 
    @Bean
    public PlatformTransactionManager jpaTransactionManager()
    {
        return new JpaTransactionManager(
                this.entityManagerFactoryBean().getObject()
        );
    }
    
    //this method and next 2 instantiate thread pool to manage user access to server. see chap 14 pg 405.  used with websocket/chat function
    @Bean
    public ThreadPoolTaskScheduler taskScheduler()
    {
        log.info("Setting up thread pool task scheduler with 20 threads.");
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(20);
        scheduler.setThreadNamePrefix("task-");
        scheduler.setAwaitTerminationSeconds(60);
        scheduler.setWaitForTasksToCompleteOnShutdown(true);
        scheduler.setErrorHandler(t -> schedulingLogger.error(
                "Unknown error occurred while executing task.", t
        ));
        scheduler.setRejectedExecutionHandler(
                (r, e) -> schedulingLogger.error(
                        "Execution of task {} was rejected for unknown reasons.", r
                )
        );
        return scheduler;
    }

    @Override
    public Executor getAsyncExecutor()
    {
        Executor executor = this.taskScheduler();
        log.info("Configuring asynchronous method executor {}.", executor);
        return executor;
    }

    @Override
    public void configureTasks(ScheduledTaskRegistrar registrar)
    {
        TaskScheduler scheduler = this.taskScheduler();
        log.info("Configuring scheduled method executor {}.", scheduler);
        registrar.setTaskScheduler(scheduler);
    }
}
