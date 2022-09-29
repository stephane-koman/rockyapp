package com.rockyapp.rockyappbackend.product_types.entity;

import com.rockyapp.rockyappbackend.common.entity.AbstractSocleEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_types")
@Builder
public class ProductType extends AbstractSocleEntity {

    private static final long serialVersionUID = -5920177767451398144L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_types_id_generator")
    @SequenceGenerator(name = "product_types_id_generator", sequenceName = "product_types_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

}
