package net.okyildiz.parametre.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParameterRepository extends JpaRepository<ParameterEntity, String> {
    Optional<List<ParameterEntity>> findAllByType(String type);
}
