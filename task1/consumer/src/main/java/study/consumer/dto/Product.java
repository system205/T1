package study.consumer.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Product {
    @Null(message = "Id must be not specified for product")
    private Long id;
    @NotBlank(message = "Description is mandatory for product")
    private String description;
    @NotBlank(message = "Name is mandatory for product")
    private String name;

    @NotNull
    @DecimalMin(value = "0.0", message = "Price must be greater than 0")
    @DecimalMax(value = "1000000.0", message = "Price must be less than 1000000")
    private Double price;

    @Valid
    private Category category;

    @Override
    public String toString() {
        return String.format(
            "Product[name='%s', description='%s', , price='%f']",
             name, description, price);
    }
}
