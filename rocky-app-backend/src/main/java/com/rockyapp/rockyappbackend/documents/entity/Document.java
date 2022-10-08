package com.rockyapp.rockyappbackend.documents.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rockyapp.rockyappbackend.common.entity.SocleEntity;
import com.rockyapp.rockyappbackend.products.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "documents")
public class Document implements SocleEntity {

    private static final long serialVersionUID = 4685622434706021241L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "documents_id_generator")
    @SequenceGenerator(name = "documents_id_generator", sequenceName = "documents_id_seq", allocationSize = 1)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "content")
    @Type(type = "org.hibernate.type.TextType")
    private String content;

    @Column(name = "filename")
    private String fileName;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}
