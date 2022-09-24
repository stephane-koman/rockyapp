package com.rockyapp.rockyappbackend.volumes.entity;

import com.rockyapp.rockyappbackend.common.entity.AbstractSocleEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "volumes")
@Builder
public class Volume extends AbstractSocleEntity {

    private static final long serialVersionUID = -7434229446046060140L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "volumes_id_generator")
    @SequenceGenerator(name = "volumes_id_generator", sequenceName = "volumes_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "quantity", nullable = false)
    private Long quantity;

    @Column(name = "mesure", length = 2, nullable = false)
    private String mesure;

    @Column(name = "description")
    private String description;
}
