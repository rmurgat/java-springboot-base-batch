package com.hanygen.helloworldbatch.config;

import com.hanygen.helloworldbatch.listener.HelloWorldJobExecutionListener;
import com.hanygen.helloworldbatch.listener.HelloWorldStepExecutionListener;
import com.hanygen.helloworldbatch.processor.FlatFileDelimetedProcessor;
import com.hanygen.helloworldbatch.writer.ConsoleItemWrite;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.hanygen.helloworldbatch.reader.*;

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

    @Autowired
    private FlatFileDelimetedItemReader flatFileDelimetedItemReader;
    @Autowired
    private FlatFileDelimetedProcessor flatFileDelimetedProcessor;

    public Step stepReadFile() {
        return steps.get("Step2Chunk")
                .<Integer,Integer>chunk(3)
                .reader(flatFileDelimetedItemReader.create())
                .processor(new FlatFileDelimetedProcessor())
                .writer(new ConsoleItemWrite())
                .build();
    }

    /**
     * El JOB para el BATCH
     * @return Job
     */
    @Bean
    public Job helloworldJob() {
        return jobs.get("helloworldJob")
                .listener(helloWorldJobExecutionListener)
                .start(stepReadFile())   // adding step to read csv file
                .build();
    }


}

