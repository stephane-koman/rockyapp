package com.rockyapp.rockyappbackend.common.generator;

import com.rockyapp.rockyappbackend.utils.helpers.SequenceGeneratorHelper;
import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.Configurable;
import org.hibernate.id.IdentifierGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.Type;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

@Component
public class OrderSequenceIdentifier implements IdentifierGenerator, Configurable {

    private String prefix;

    @Override
    public void configure(Type type, Properties properties, ServiceRegistry serviceRegistry) throws MappingException {
        prefix = properties.getProperty("prefix");
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object obj) throws HibernateException {
        long seqValue = SequenceGeneratorHelper.generate(session, obj, prefix);

        String seqValueStr = String.valueOf(seqValue);

        if(seqValueStr.length() < 7)
            seqValueStr = "0".repeat(7 - seqValueStr.length()) + seqValueStr;

        return new SimpleDateFormat("yyyyMMdd").format(new Date()) + "_" + seqValueStr;
    }
}