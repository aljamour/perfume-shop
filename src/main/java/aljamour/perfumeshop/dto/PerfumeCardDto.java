package aljamour.perfumeshop.dto;

import aljamour.perfumeshop.model.Perfume;

import java.math.BigDecimal;
import java.util.List;

public record PerfumeCardDto(
        Long id,
        String perfumeNumber,
        String name,
        String brand,
        String inspiredBy,
        String gender,
        String slug,
        String imageUrl,
        BigDecimal fromPrice,
        List<Integer> sizes
) {
    public static PerfumeCardDto from(Perfume perfume) {
        var availableSizes = perfume.getSizes().stream()
                .filter(s -> s.isActive())
                .map(s -> s.getMl())
                .sorted()
                .toList();

        var minimumPrice = perfume.getSizes().stream()
                .filter(s -> s.isActive() && s.getSellingPrice() != null)
                .map(s -> s.getSellingPrice())
                .min(BigDecimal::compareTo)
                .orElse(null);

        return new PerfumeCardDto(
                perfume.getId(),
                perfume.getPerfumeNumber(),
                perfume.getName(),
                perfume.getBrand(),
                perfume.getInspiredBy(),
                perfume.getGender().getDisplayName(),
                perfume.getSlug(),
                perfume.getImageUrl(),
                minimumPrice,
                availableSizes
        );
    }
}
