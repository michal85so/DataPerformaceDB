package domain;

import lombok.*;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity(name = "environment")
@Document(collection = "environment")
public class Environment {

    @Id
    @org.springframework.data.annotation.Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @NonNull
    @Column(nullable = false, length = 45)
    private String producer;

    @NonNull
    @Column(nullable = false, length = 45)
    private String category;

    @NonNull
    @Column(nullable = false, length = 45)
    private String model;

    @NonNull
    @Column(nullable = false)
    private double price;

    @Column(name = "customer_price")
    private double customerPrice;

    private int items;

    private int warranty;

    @PersistenceConstructor
    public Environment(String producer,
                       String category,
                       String model,
                       double price,
                       double customerPrice,
                       int items,
                       int warranty) {
        this.producer = producer;
        this.category = category;
        this.model = model;
        this.price = price;
        this.customerPrice = customerPrice;
        this.items = items;
        this.warranty = warranty;
    }
}
