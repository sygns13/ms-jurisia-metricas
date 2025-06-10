package pj.gob.pe.metricas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericRepo<T, ID>  extends JpaRepository<T, ID> {
    // Este repositorio genérico puede ser utilizado para operaciones comunes
    // en todas las entidades que extiendan de él, como findAll, findById, save, delete, etc.
}
