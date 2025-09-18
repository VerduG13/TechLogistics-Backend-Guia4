package org.ean.techlogistics.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor
@Entity
@Table(name = "deliveries")
public class Delivery {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private LocalDateTime pickedUpDate;   // cuando pasa a EN_CAMINO

    @Column
    private LocalDateTime deliveryDate;   // cuando pasa a ENTREGADA

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "courier_id")
    private User courier;

    // Delivery posee la FK
    @OneToOne
    @JoinColumn(name = "order_id", unique = true, nullable = false)
    private Order order;
}
