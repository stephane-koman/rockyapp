package com.rockyapp.rockyappbackend.payments.mapper;

import com.rockyapp.rockyappbackend.common.mapper.AbstractSocleMapper;
import com.rockyapp.rockyappbackend.common.mapper.SocleMapper;
import com.rockyapp.rockyappbackend.payments.dto.PaymentInvoiceDTO;
import com.rockyapp.rockyappbackend.payments.entity.Payment;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PaymentInvoiceMapper extends AbstractSocleMapper<Payment, PaymentInvoiceDTO> implements SocleMapper<Payment, PaymentInvoiceDTO> {

    @Override
    public Payment mapToEntity(PaymentInvoiceDTO model, Payment entity) {
        return null;
    }

    @Override
    public PaymentInvoiceDTO mapFromEntity(Payment entity) {
        PaymentInvoiceDTO paymentInvoiceDTO = new PaymentInvoiceDTO();
        BeanUtils.copyProperties(entity, paymentInvoiceDTO, "active");
        paymentInvoiceDTO.setActive(entity.getActive() == 1);
        return paymentInvoiceDTO;
    }
}
