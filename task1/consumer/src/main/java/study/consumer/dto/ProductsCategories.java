package study.consumer.dto;

import lombok.Getter;

@Getter
public class ProductsCategories {
    Iterable<Product> products;
    Iterable<Category> categories;

    public ProductsCategories(Iterable<Product> products, Iterable<Category> categories) {
        this.products = products;
        this.categories = categories;
    }
}
