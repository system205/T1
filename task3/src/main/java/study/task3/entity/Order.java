package study.task3.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "orders")
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    private String description;

    @Enumerated
    private Status status;

    public Order(String description) {
        this.description = description;
    }

    public enum Status {
        DELIVERED,
        NOT_DELIVERED
    }

    @Override
    public String toString() {
        return "Order{" +
            "id=" + id +
            ", description='" + description + '\'' +
            ", status=" + status +
            '}';
    }
}
