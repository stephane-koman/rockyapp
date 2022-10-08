package com.rockyapp.rockyappbackend.products.entity;

import com.rockyapp.rockyappbackend.common.entity.AbstractSocleEntity;
import com.rockyapp.rockyappbackend.documents.entity.Document;
import com.rockyapp.rockyappbackend.product_types.entity.ProductType;
import com.rockyapp.rockyappbackend.volumes.entity.Volume;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "products")
public class Product extends AbstractSocleEntity {

    private static final long serialVersionUID = -8197985238891824955L;

    @Id
    @GeneratedValue(generator = "product-sequence", strategy = GenerationType.SEQUENCE)
    @GenericGenerator(
            name = "product-sequence",
            strategy = "com.rockyapp.rockyappbackend.common.generator.ProductSequenceIdentifier",
            parameters = @org.hibernate.annotations.Parameter(name = "prefix", value = "prod")
    )
    @Column(name = "product_id", nullable = false, length = 250)
    private String id;

    @Column(name = "product_code", nullable = false, length = 250)
    private String code;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "description")
    @Type(type = "org.hibernate.type.TextType")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_type_id", nullable = false)
    private ProductType productType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "volume_id", nullable = false)
    private Volume volume;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product", cascade = CascadeType.ALL)
    private Set<Document> documents = new HashSet<>();

}