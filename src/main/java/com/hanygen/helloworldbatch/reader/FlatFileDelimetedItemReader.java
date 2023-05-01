package com.hanygen.helloworldbatch.reader;

import com.hanygen.helloworldbatch.model.Product;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

@Component
public class FlatFileDelimetedItemReader {

    public org.springframework.batch.item.file.FlatFileItemReader create() {
        FlatFileItemReader reader = new FlatFileItemReader();

        //step 1: let reader know where is the file
        reader.setResource(new FileSystemResource("input/product.csv"));

        //step 2: create line mapper
        reader.setLineMapper(
                new DefaultLineMapper<Product>() {
                    {
                        setLineTokenizer(new DelimitedLineTokenizer() {
                            {
                                setNames(new String[]{"productId", "productName", "ProductDesc", "price", "unit"});
                            }
                        });
                        setFieldSetMapper(new BeanWrapperFieldSetMapper<Product>() {
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

}
