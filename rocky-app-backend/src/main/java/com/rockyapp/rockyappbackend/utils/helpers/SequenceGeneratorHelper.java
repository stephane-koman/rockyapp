package com.rockyapp.rockyappbackend.utils.helpers;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;

import java.util.stream.Stream;

public class SequenceGeneratorHelper {

    public static long generate(SharedSessionContractImplementor session, Object obj, String prefix) throws HibernateException {

        String query = String.format("select %s from %s",
                session.getEntityPersister(obj.getClass().getName(), obj)
                        .getIdentifierPropertyName(),
                obj.getClass().getSimpleName());

        Stream<String> ids = session.createQuery(query).stream();

        return ids.map(o -> o.replace(prefix + "-", ""))
                .map(o -> {
                    String[] data = o.split("_");
                    if(data.length > 1) return data[1];
                    return o;
                })
                .mapToLong(Long::parseLong)
                .max()
                .orElse(0L) + 1;

    }
}
