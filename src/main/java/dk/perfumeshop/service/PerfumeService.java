package dk.perfumeshop.service;

import dk.perfumeshop.model.Gender;
import dk.perfumeshop.model.Perfume;
import dk.perfumeshop.repository.PerfumeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PerfumeService {

    private final PerfumeRepository perfumeRepository;

    public PerfumeService(PerfumeRepository perfumeRepository) {
        this.perfumeRepository = perfumeRepository;
    }

    public List<Perfume> findAllActive() {
        return perfumeRepository.findByActiveTrueOrderByNameAsc();
    }

    public List<Perfume> featured() {
        return perfumeRepository.findTop8ByActiveTrueAndFeaturedTrueOrderByUpdatedAtDesc();
    }

    public Perfume bySlug(String slug) {
        return perfumeRepository.findBySlugAndActiveTrue(slug)
                .orElseThrow(() -> new IllegalArgumentException("Parfumen blev ikke fundet."));
    }

    public List<Perfume> byGender(Gender gender) {
        return perfumeRepository.findByActiveTrueAndGenderOrderByNameAsc(gender);
    }

    public List<Perfume> search(String query) {
        if (query == null || query.isBlank()) {
            return findAllActive();
        }
        String q = query.trim();
        return perfumeRepository
                .findByActiveTrueAndNameContainingIgnoreCaseOrActiveTrueAndBrandContainingIgnoreCaseOrActiveTrueAndInspiredByContainingIgnoreCase(q, q, q);
    }
}
