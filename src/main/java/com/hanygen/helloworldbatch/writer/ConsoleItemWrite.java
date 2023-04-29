package com.hanygen.helloworldbatch.writer;

import org.springframework.batch.item.support.AbstractItemStreamItemWriter;

import java.util.List;

public class ConsoleItemWrite extends AbstractItemStreamItemWriter {
    @Override
    public void write(List list) throws Exception {
        list.stream().forEach(System.out::println);
        System.out.println(" *** writing each chunk *** ");
    }
}
