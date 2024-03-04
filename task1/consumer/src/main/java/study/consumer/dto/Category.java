package study.consumer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    @Null(message = "Id must be not specified for category")
    private Long id;
    @NotBlank(message = "Name is mandatory for category")
    private String name;
}
