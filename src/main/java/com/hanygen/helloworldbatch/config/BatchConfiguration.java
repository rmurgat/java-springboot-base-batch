package com.hanygen.helloworldbatch.config;

import com.hanygen.helloworldbatch.listener.HelloWorldJobExecutionListener;
import com.hanygen.helloworldbatch.listener.HelloWorldStepExecutionListener;
import com.hanygen.helloworldbatch.model.Product;
import com.hanygen.helloworldbatch.processor.InMemoryItemProcessor;
import com.hanygen.helloworldbatch.reader.InMemoryReader;
import com.hanygen.helloworldbatch.writer.ConsoleItemWrite;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;

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
    private InMemoryItemProcessor inMemoryItemProcessor;

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

    @Bean
    public InMemoryReader reader() {
        return new InMemoryReader();
    }

    @Bean
    public FlatFileItemReader flatFileItemReader() {
        FlatFileItemReader reader = new FlatFileItemReader();

        //step 1: let reader know where is the file
        reader.setResource(new FileSystemResource("input/product.csv"));

        //step 2: create line mapper
        reader.setLineMapper(
                new DefaultLineMapper<Product>() {
                    {
                        setLineTokenizer (new DelimitedLineTokenizer() {
                        {
                            setNames(new String[]{"productId", "productName", "ProductDesc", "price", "unit"});
                        }
                        });
                        setFieldSetMapper(new BeanWrapperFieldSetMapper<Product>(){
                            {
                                setTargetType(Product.class);
                            }
                        });
                    }
                }
        );

        //step 3: tell reader to skip the header
        reader.setLinesToSkip(1);
        return reader;
    }

    public Step step2Chunk() {
        return steps.get("Step2Chunk")
                .<Integer,Integer>chunk(3)
                .reader(flatFileItemReader())
                .writer(new ConsoleItemWrite())
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
                .next(step2Chunk())   // adding step 2 chunk
                .build();
    }


}

