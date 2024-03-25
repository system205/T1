package study.task3.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue
    private Long id;

    private String description;

    @Enumerated
    private Status status;

    public enum Status {
        DELIVERED,
        NOT_DELIVERED
    }
}
