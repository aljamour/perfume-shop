package dk.perfumeshop.dto;

import java.math.BigDecimal;

public record CartLineDto(
        Long sizeId,
        String perfumeName,
        String slug,
        int ml,
        BigDecimal unitPrice,
        int quantity,
        BigDecimal lineTotal
) {
}
