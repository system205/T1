package study.supplier.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import study.supplier.SupplierApplicationTests;
import study.supplier.dto.Paged;
import study.supplier.entity.Product;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class ProductsControllerTest extends SupplierApplicationTests {

    @Test
    void shouldGetAll4InitialProducts() {
        final Paged<List<Product>> products =
            restTemplate.exchange(url + "/api/v1/products",
                HttpMethod.GET, HttpEntity.EMPTY,
                new ParameterizedTypeReference<Paged<List<Product>>>() {
                }).getBody();

        assertThat(products).isNotNull();
        assertThat(products.content()).hasSize(4);
    }

}
