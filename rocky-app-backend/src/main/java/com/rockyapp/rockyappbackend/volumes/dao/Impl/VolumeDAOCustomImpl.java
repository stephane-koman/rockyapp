package com.rockyapp.rockyappbackend.volumes.dao.Impl;

import com.rockyapp.rockyappbackend.common.dao.SocleDAO;
import com.rockyapp.rockyappbackend.utils.enums.VolumeEnum;
import com.rockyapp.rockyappbackend.utils.helpers.ArrayHelper;
import com.rockyapp.rockyappbackend.utils.helpers.DaoHelper;
import com.rockyapp.rockyappbackend.volumes.dao.VolumeDAOCustom;
import com.rockyapp.rockyappbackend.volumes.dto.VolumeSearchCriteriaDTO;
import com.rockyapp.rockyappbackend.volumes.entity.Volume;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static com.rockyapp.rockyappbackend.utils.helpers.DaoHelper.createPredicate;
import static com.rockyapp.rockyappbackend.utils.helpers.DaoHelper.createPredicate;

@Component
public class VolumeDAOCustomImpl extends SocleDAO implements VolumeDAOCustom {
    @Override
    public Page<Volume> search(VolumeSearchCriteriaDTO criteriaDTO, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Volume> cq = cb.createQuery(Volume.class);
        Root<Volume> volumeRoot = cq.from(Volume.class);

        List<Predicate> predicates = new ArrayList<>();

        if(criteriaDTO == null) criteriaDTO = new VolumeSearchCriteriaDTO();

        Predicate deleteP = createPredicate(0, volumeRoot.get(VolumeEnum.DELETE.getValue()), cb);
        predicates.add(deleteP);

        if (StringUtils.isNotEmpty(criteriaDTO.getText_search())) {
            Predicate quantityP = NumberUtils.isDigits(criteriaDTO.getText_search()) ? createPredicate(NumberUtils.toInt(criteriaDTO.getText_search()), volumeRoot.get(VolumeEnum.QUANTITY.getValue()), cb) : null;
            Predicate mesureP = DaoHelper.createPredicate(criteriaDTO.getText_search(), volumeRoot.get(VolumeEnum.MESURE.getValue()), cb);
            Predicate combineP = cb.or(quantityP, mesureP);
            predicates.add(combineP);
        }

        if (criteriaDTO.getQuantity() != null) {
            Predicate quantityP = createPredicate(Math.toIntExact(criteriaDTO.getQuantity()), volumeRoot.get(VolumeEnum.QUANTITY.getValue()), cb);
            predicates.add(quantityP);
        }

        if ( criteriaDTO.getMesure() != null && StringUtils.isNotEmpty(criteriaDTO.getMesure().name())) {
            Predicate emailP = DaoHelper.createPredicate(criteriaDTO.getMesure().name(), volumeRoot.get(VolumeEnum.MESURE.getValue()), cb);
            predicates.add(emailP);
        }

        if (ArrayHelper.verifyIntIsBoolean(criteriaDTO.getActive())) {
            Predicate activeP = createPredicate(criteriaDTO.getActive(), volumeRoot.get(VolumeEnum.ACTIVE.getValue()), cb);
            predicates.add(activeP);
        }

        Predicate[] finalPredicates = new Predicate[predicates.size()];
        predicates.toArray(finalPredicates);

        cq.select(volumeRoot)
                .where(finalPredicates)
                .orderBy(QueryUtils.toOrders(pageable.getSort(), volumeRoot, cb))
                .groupBy(volumeRoot.get(VolumeEnum.ID.getValue()));

        return DaoHelper.returnResults(entityManager, cq, pageable);
    }
}
