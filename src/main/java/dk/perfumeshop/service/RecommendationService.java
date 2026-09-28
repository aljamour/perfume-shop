package dk.perfumeshop.service;

import dk.perfumeshop.dto.RecommendationDto;
import dk.perfumeshop.model.Perfume;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

@Service
public class RecommendationService {

    private final PerfumeService perfumeService;

    public RecommendationService(PerfumeService perfumeService) {
        this.perfumeService = perfumeService;
    }

    public List<RecommendationDto> recommend(String message) {
        String query = message == null ? "" : message.toLowerCase(Locale.ROOT);
        List<ScoredPerfume> scored = new ArrayList<>();

        for (Perfume perfume : perfumeService.findAllActive()) {
            int score = 0;
            StringBuilder reason = new StringBuilder();

            if (contains(query, "herre", "mand", "men") && perfume.getGender().name().equals("MEN")) {
                score += 4;
                reason.append("matcher herredufte; ");
            }
            if (contains(query, "kvinde", "dame", "women") && perfume.getGender().name().equals("WOMEN")) {
                score += 4;
                reason.append("matcher kvindedufte; ");
            }
            if (query.contains("unisex") && perfume.getGender().name().equals("UNISEX")) {
                score += 4;
                reason.append("er unisex; ");
            }

            if (perfume.getBrand() != null && !perfume.getBrand().isBlank() &&
                    query.contains(perfume.getBrand().toLowerCase(Locale.ROOT))) {
                score += 5;
                reason.append("matcher dit brandønske; ");
            }

            for (var category : perfume.getCategories()) {
                String name = category.getName().toLowerCase(Locale.ROOT);
                String slug = category.getSlug().toLowerCase(Locale.ROOT);
                if (query.contains(name) || query.contains(slug.replace("-", " "))) {
                    score += 4;
                    reason.append("passer til ").append(category.getName()).append("; ");
                }
            }

            for (var note : perfume.getScentNotes()) {
                if (query.contains(note.getNoteName().toLowerCase(Locale.ROOT))) {
                    score += 3;
                    reason.append("indeholder ").append(note.getNoteName()).append("; ");
                }
            }

            if (query.contains("popul") && perfume.isFeatured()) {
                score += 2;
                reason.append("er udvalgt; ");
            }

            if (score > 0) {
                scored.add(new ScoredPerfume(perfume, score, cleanReason(reason)));
            }
        }

        if (scored.isEmpty()) {
            return perfumeService.featured().stream()
                    .limit(4)
                    .map(p -> new RecommendationDto(p.getName(), p.getBrand(), p.getSlug(), "Et udvalgt sted at starte."))
                    .toList();
        }

        return scored.stream()
                .sorted(Comparator.comparingInt(ScoredPerfume::score).reversed())
                .limit(4)
                .map(s -> new RecommendationDto(s.perfume().getName(), s.perfume().getBrand(), s.perfume().getSlug(), s.reason()))
                .toList();
    }

    private boolean contains(String query, String... terms) {
        for (String term : terms) {
            if (query.contains(term)) return true;
        }
        return false;
    }

    private String cleanReason(StringBuilder builder) {
        String result = builder.toString().trim();
        return result.endsWith(";") ? result.substring(0, result.length() - 1) + "." : result;
    }

    private record ScoredPerfume(Perfume perfume, int score, String reason) {}
}
