package one.digitalinnovation.beerstock.repository;

import one.digitalinnovation.beerstock.entity.BeerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BeerRepository extends JpaRepository<BeerEntity, Long> {

    Optional<BeerEntity> findByName(String name);
}
