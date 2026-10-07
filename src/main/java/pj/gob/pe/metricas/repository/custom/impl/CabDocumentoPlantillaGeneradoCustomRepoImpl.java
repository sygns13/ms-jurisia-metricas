package pj.gob.pe.metricas.repository.custom.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pj.gob.pe.metricas.model.entities.CabDocumentoPlantillaGenerado;
import pj.gob.pe.metricas.repository.custom.CabDocumentoPlantillaGeneradoCustomRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Mismo esquema de filtros dinámicos que {@link CabDocumentoGeneradoCustomRepoImpl}. */
@Repository
public class CabDocumentoPlantillaGeneradoCustomRepoImpl implements CabDocumentoPlantillaGeneradoCustomRepo {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<CabDocumentoPlantillaGenerado> getDocumentosPlantillaGenerados(
            Map<String, Object> filters, Map<String, Object> filtersFecha, Pageable pageable) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<CabDocumentoPlantillaGenerado> query = cb.createQuery(CabDocumentoPlantillaGenerado.class);
        Root<CabDocumentoPlantillaGenerado> root = query.from(CabDocumentoPlantillaGenerado.class);
        query.select(root).where(buildPredicate(root, cb, filters, filtersFecha));

        if (pageable.getSort().isSorted()) {
            List<Order> orders = new ArrayList<>();
            pageable.getSort().forEach(order -> orders.add(order.isAscending()
                    ? cb.asc(root.get(order.getProperty()))
                    : cb.desc(root.get(order.getProperty()))));
            query.orderBy(orders);
        }

        List<CabDocumentoPlantillaGenerado> resultList = entityManager.createQuery(query)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<CabDocumentoPlantillaGenerado> countRoot = countQuery.from(CabDocumentoPlantillaGenerado.class);
        countQuery.select(cb.count(countRoot)).where(buildPredicate(countRoot, cb, filters, filtersFecha));

        Long totalElements = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(resultList, pageable, totalElements);
    }

    @Override
    public List<CabDocumentoPlantillaGenerado> getListDocumentosPlantillaGenerados(
            Map<String, Object> filters, Map<String, Object> filtersFecha) {

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        CriteriaQuery<CabDocumentoPlantillaGenerado> query = cb.createQuery(CabDocumentoPlantillaGenerado.class);
        Root<CabDocumentoPlantillaGenerado> root = query.from(CabDocumentoPlantillaGenerado.class);
        query.select(root).where(buildPredicate(root, cb, filters, filtersFecha));

        return entityManager.createQuery(query).getResultList();
    }

    /** Filtros AND por igualdad; las claves "list_x" generan un IN y las fechas filtran por regDate. */
    private Predicate buildPredicate(Root<CabDocumentoPlantillaGenerado> root, CriteriaBuilder cb,
                                     Map<String, Object> filters, Map<String, Object> filtersFecha) {

        List<Predicate> predicates = new ArrayList<>();

        filters.forEach((key, value) -> {
            if (value == null) return;
            if (key.startsWith("list_")) {
                List<?> valores = (List<?>) value;
                if (!valores.isEmpty()) {
                    CriteriaBuilder.In<Object> in = cb.in(root.get(key.replace("list_", "")));
                    valores.forEach(in::value);
                    predicates.add(in);
                }
            } else {
                predicates.add(cb.equal(root.get(key), value));
            }
        });

        if (filtersFecha != null) {
            if (filtersFecha.get("fechaInicio") != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("regDate"), (LocalDate) filtersFecha.get("fechaInicio")));
            }
            if (filtersFecha.get("fechaFin") != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("regDate"), (LocalDate) filtersFecha.get("fechaFin")));
            }
        }

        return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
    }
}
