package dk.perfumeshop.repository;

import dk.perfumeshop.model.Gender;
import dk.perfumeshop.model.Perfume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PerfumeRepository extends JpaRepository<Perfume, Long> {

    Optional<Perfume> findByPerfumeNumber(String perfumeNumber);

    Optional<Perfume> findBySlugAndActiveTrue(String slug);

    List<Perfume> findByActiveTrueOrderByNameAsc();

    List<Perfume> findTop8ByActiveTrueAndFeaturedTrueOrderByUpdatedAtDesc();

    List<Perfume> findByActiveTrueAndGenderOrderByNameAsc(Gender gender);

    List<Perfume> findByActiveTrueAndNameContainingIgnoreCaseOrActiveTrueAndBrandContainingIgnoreCaseOrActiveTrueAndInspiredByContainingIgnoreCase(
            String name, String brand, String inspiredBy
    );
}
