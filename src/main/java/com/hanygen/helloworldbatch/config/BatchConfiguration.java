package com.hanygen.helloworldbatch.config;

import com.hanygen.helloworldbatch.listener.BatchSkipListener;
import com.hanygen.helloworldbatch.listener.HelloWorldJobExecutionListener;
import com.hanygen.helloworldbatch.listener.HelloWorldStepExecutionListener;
import com.hanygen.helloworldbatch.processor.FlatFileDelimetedProcessor;
import com.hanygen.helloworldbatch.writer.ConsoleItemWrite;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.step.skip.AlwaysSkipItemSkipPolicy;
import org.springframework.batch.item.file.FlatFileParseException;
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

    /**
     * This step have a simple Skip General Policy treatment of Exceptions
     * @return Step
     */
    public Step stepAddingSkip() {
        return steps.get("stepAddingSkip")
                .<Integer,Integer>chunk(3)
                .reader(flatFileDelimetedItemReader.create())
                .processor(new FlatFileDelimetedProcessor())
                .writer(new ConsoleItemWrite())
                .faultTolerant()
                .skip(FlatFileParseException.class)
                //.skipLimit(2)                     rmt to set the number-limit of errors tolerated
                .skipPolicy(new AlwaysSkipItemSkipPolicy())   //rmt to tolerate all number of errors
                .build();
    }

    /**
     * This step have an Skip Listener Class to have the treatment of Exceptions
     * @return Step
     */
    public Step stepAddingSkipListener() {
        return steps.get("stepAddingSkipListener")
                .<Integer,Integer>chunk(3)
                .reader(flatFileDelimetedItemReader.create())
                .processor(new FlatFileDelimetedProcessor())
                .writer(new ConsoleItemWrite())
                .faultTolerant()
                .skip(RuntimeException.class)         //rmt - with SkipListener it is not required
                .skipLimit(10)                               //rmt - to set the number-limit of errors tolerated
                //.skipPolicy(new AlwaysSkipItemSkipPolicy())   //rmt - to tolerate all number of errors
                .listener(new BatchSkipListener())
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
                .start(stepAddingSkip())         // rmt - adding step to test skip
                .next(stepAddingSkipListener())    // rmt - adding step to test skip listener
                .build();
    }

}

