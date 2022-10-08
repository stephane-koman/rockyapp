package com.rockyapp.rockyappbackend.orders.entity;

import com.rockyapp.rockyappbackend.common.entity.AbstractSocleEntity;
import com.rockyapp.rockyappbackend.customers.entity.Customer;
import com.rockyapp.rockyappbackend.payments.entity.Payment;
import com.rockyapp.rockyappbackend.users.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order extends AbstractSocleEntity {

    private static final long serialVersionUID = -2168355007508950744L;

    @Id
    @GeneratedValue(generator = "invoice-sequence", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "order-sequence",
            strategy = "com.rockyapp.rockyappbackend.common.generator.OrderSequenceIdentifier",
            parameters = @org.hibernate.annotations.Parameter(name = "prefix", value = "ORD")
    )
    @Column(name = "order_id", nullable = false, length = 250)
    private String id;

    @Column(name = "price")
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @OneToMany(mappedBy = "order")
    private Set<OrderItem> orderItems;

    @OneToMany(mappedBy = "order")
    private Set<Payment> payments;

}