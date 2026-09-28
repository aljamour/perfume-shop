package dk.perfumeshop.dto;

public record RecommendationDto(
        String name,
        String brand,
        String slug,
        String reason
) {
}
