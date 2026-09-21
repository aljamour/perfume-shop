package dk.perfumeshop.repository;

import dk.perfumeshop.model.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfumeRepository extends JpaRepository<Perfume, Long> {

    Optional<Perfume> findByPerfumeNumber(String perfumeNumber);
}