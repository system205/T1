package study.consumer.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Product {

    private String description;
    private String name;
    private Double price;
    private Category category;

    @Override
    public String toString() {
        return String.format(
            "Product[name='%s', description='%s', , price='%f']",
             name, description, price);
    }
}
