package com.rockyapp.rockyappbackend.invoices.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class InvoiceItemId implements Serializable {

    private static final long serialVersionUID = 4107321409952613249L;

    @Column(name = "invoice_id", nullable = false, length = 250)
    private String invoiceId;

    @Column(name = "product_id", nullable = false, length = 250)
    private String productId;

    @Column(name = "volume_id", nullable = false)
    private Long volumeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        InvoiceItemId entity = (InvoiceItemId) o;
        return Objects.equals(this.productId, entity.productId) &&
                Objects.equals(this.volumeId, entity.volumeId) &&
                Objects.equals(this.invoiceId, entity.invoiceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, volumeId, invoiceId);
    }

}