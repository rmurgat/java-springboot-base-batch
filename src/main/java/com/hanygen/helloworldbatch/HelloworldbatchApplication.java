package com.hanygen.helloworldbatch;

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
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@EnableBatchProcessing
@SpringBootApplication
public class HelloworldbatchApplication {

    @Autowired
    private JobBuilderFactory jobs;
    @Autowired
    private StepBuilderFactory steps;

    /**
     * El programa principal para arrancar el contexto de Springboot
     * @return Job
     */
    public static void main(String[] args) {
        SpringApplication.run(HelloworldbatchApplication.class, args);
    }

    /**
     * El único Step del Job
     * @return Job
     */
    @Bean
    public Step step1() {
        return steps.get("Step1")
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
                System.out.println("hello world");
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
                .start(step1())
                .build();
    }
}
