package com.hanygen.helloworldbatch.listener;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.annotation.OnSkipInProcess;
import org.springframework.batch.core.annotation.OnSkipInRead;
import org.springframework.batch.item.file.FlatFileParseException;

import java.io.FileOutputStream;
import java.io.IOException;

public class BatchSkipListener {
    private String skipErrorReaderFileName = "error/read_skipped";
    private String skipErrorProcessorFileName = "error/process_skipped";

    @OnSkipInRead
    public void onSkipRead(Throwable t) {
        if (t instanceof FlatFileParseException) {
            FlatFileParseException ffep = (FlatFileParseException) t;
            onSkip(ffep.getInput(), skipErrorReaderFileName);
        }
    }

    @OnSkipInProcess
    public void onSkipProccess(Object item, Throwable t) {
        if (t instanceof RuntimeException) {
            onSkip(item, skipErrorProcessorFileName);
        }
    }

    public void onSkip(Object o, String fName) {
        try {
            FileOutputStream fos = null;
            fos = new FileOutputStream(fName, true);
            fos.write(o.toString().getBytes());
            fos.write("\r".getBytes());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
