package com.hanygen.helloworldbatch.processor;
import com.hanygen.helloworldbatch.model.Product;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class FlatFileDelimetedProcessor implements ItemProcessor<Product, Product> {

    @Override
    public Product process(Product product) throws Exception {
        Product opp = new Product();
        opp.setProductId(product.getProductId());
        opp.setProductDesc(product.getProductDesc()+" -> processed");
        opp.setProductName(product.getProductName()+" -> processed");
        opp.setPricce(product.getPricce());
        opp.setUnit(product.getUnit());
        return opp;
    }
}
