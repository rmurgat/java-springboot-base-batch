package com.hanygen.helloworldbatch.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldStepExecutionListener implements StepExecutionListener {
    @Override
    public void beforeStep(StepExecution stepExecution) {
        System.out.println("this is from before step execution " + stepExecution.getJobExecution().getExecutionContext());
        System.out.println("Inside Step " + stepExecution.getJobExecution().getJobParameters());   // printing job parameter before Step
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        System.out.println("this is from after step execution " + stepExecution.getJobExecution().getExecutionContext());
        return null;
    }
}
