package dk.perfumeshop.service;

import dk.perfumeshop.dto.CartLineDto;
import dk.perfumeshop.model.PerfumeSize;
import dk.perfumeshop.repository.PerfumeSizeRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CartService {

    private final PerfumeSizeRepository sizeRepository;

    public CartService(PerfumeSizeRepository sizeRepository) {
        this.sizeRepository = sizeRepository;
    }

    public List<CartLineDto> resolve(Map<Long, Integer> cart) {
        List<CartLineDto> lines = new ArrayList<>();

        for (var entry : cart.entrySet()) {
            PerfumeSize size = sizeRepository.findById(entry.getKey()).orElse(null);
            if (size == null || !size.isAvailable()) {
                continue;
            }

            int quantity = Math.max(1, Math.min(entry.getValue(), size.getStockQuantity()));
            BigDecimal lineTotal = size.getSellingPrice().multiply(BigDecimal.valueOf(quantity));

            lines.add(new CartLineDto(
                    size.getId(),
                    size.getPerfume().getName(),
                    size.getPerfume().getSlug(),
                    size.getMl(),
                    size.getSellingPrice(),
                    quantity,
                    lineTotal
            ));
        }

        return lines;
    }

    public BigDecimal total(List<CartLineDto> lines) {
        return lines.stream()
                .map(CartLineDto::lineTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
