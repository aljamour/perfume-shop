package dk.perfumeshop.service;

import dk.perfumeshop.model.Perfume;
import dk.perfumeshop.model.PerfumeSize;
import dk.perfumeshop.repository.PerfumeRepository;
import dk.perfumeshop.repository.PerfumeSizeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class AdminPerfumeService {

    private final PerfumeRepository perfumeRepository;
    private final PerfumeSizeRepository sizeRepository;

    public AdminPerfumeService(PerfumeRepository perfumeRepository, PerfumeSizeRepository sizeRepository) {
        this.perfumeRepository = perfumeRepository;
        this.sizeRepository = sizeRepository;
    }

    @Transactional(readOnly = true)
    public List<Perfume> all() {
        return perfumeRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Perfume get(Long id) {
        return perfumeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Perfume not found"));
    }

    public Perfume save(Perfume perfume) {
        return perfumeRepository.save(perfume);
    }

    public PerfumeSize addSize(Long perfumeId, int ml) {
        Perfume perfume = get(perfumeId);
        PerfumeSize size = new PerfumeSize();
        size.setMl(ml);
        size.setType("Extrait de Parfum");
        size.setActive(true);
        size.setPerfume(perfume);
        return sizeRepository.save(size);
    }

    public void updateSize(Long sizeId, BigDecimal sellingPrice, Integer stockQuantity, Boolean active) {
        PerfumeSize size = sizeRepository.findById(sizeId)
                .orElseThrow(() -> new IllegalArgumentException("Size not found"));

        size.setSellingPrice(sellingPrice);
        if (stockQuantity != null) {
            size.setStockQuantity(Math.max(0, stockQuantity));
        }
        if (active != null) {
            size.setActive(active);
        }
    }

    public void deactivate(Long perfumeId) {
        Perfume perfume = get(perfumeId);
        perfume.setActive(false);
    }
}
