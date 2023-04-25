package com.hanygen.helloworldbatch.config;

import com.hanygen.helloworldbatch.listener.HelloWorldJobExecutionListener;
import com.hanygen.helloworldbatch.listener.HelloWorldStepExecutionListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing
@Configuration
public class BatchConfiguration {

    @Autowired
    private JobBuilderFactory jobs;
    @Autowired
    private StepBuilderFactory steps;

    @Autowired
    private HelloWorldJobExecutionListener helloWorldJobExecutionListener;
    @Autowired
    private HelloWorldStepExecutionListener helloWorldStepExecutionListener;

    /**
     * El único Step del Job
     * @return Job
     */
    @Bean
    public Step step1() {
        return steps.get("Step1")
                .listener(helloWorldStepExecutionListener)
                .tasklet(helloworldTasklet())
                .build();
    }

    /**
     * La tarea que deberá ser ejecutada
     * @return Job
     */
    private Tasklet helloworldTasklet() {
        return (new Tasklet() {
            @Override
            public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                System.out.println("This is the JOB purpose:  Hello World");
                return RepeatStatus.FINISHED;
            }
        });
    }

    /**
     * El JOB para el BATCH
     * @return Job
     */
    @Bean
    public Job helloworldJob() {
        return jobs.get("helloworldJob")
                .listener(helloWorldJobExecutionListener)
                .start(step1())
                .build();
    }


}

