package aljamour.perfumeshop.controller;

import aljamour.perfumeshop.model.Category;
import aljamour.perfumeshop.model.Gender;
import aljamour.perfumeshop.model.Perfume;
import aljamour.perfumeshop.repository.CategoryRepository;
import aljamour.perfumeshop.service.PerfumeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CatalogController {

    private final PerfumeService perfumeService;
    private final CategoryRepository categoryRepository;

    public CatalogController(PerfumeService perfumeService, CategoryRepository categoryRepository) {
        this.perfumeService = perfumeService;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/catalog")
    public String catalog(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Gender gender,
            Model model
    ) {
        boolean filtering = (q != null && !q.isBlank()) || gender != null;
        List<Perfume> perfumes = gender != null ? perfumeService.byGender(gender) : perfumeService.search(q);

        model.addAttribute("perfumes", perfumes);
        model.addAttribute("query", q == null ? "" : q);
        model.addAttribute("selectedGender", gender);
        model.addAttribute("filtering", filtering);

        if (!filtering) {
            model.addAttribute("catalogSections", buildCatalogSections());
        }

        return "catalog";
    }

    @GetMapping("/perfumer/{slug}")
    public String detail(@PathVariable String slug, Model model) {
        model.addAttribute("perfume", perfumeService.bySlug(slug));
        return "perfume-detail";
    }

    private Map<Category, List<Perfume>> buildCatalogSections() {
        Map<Category, List<Perfume>> sections = new LinkedHashMap<>();

        List<String> preferredOrder = List.of(
                "popular",
                "office",
                "summer",
                "winter",
                "date-night",
                "signature"
        );

        preferredOrder.forEach(slug ->
                categoryRepository.findBySlug(slug).ifPresent(category -> {
                    List<Perfume> products = category.getPerfumes().stream()
                            .filter(Perfume::isActive)
                            .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
                            .toList();

                    if (!products.isEmpty()) {
                        sections.put(category, products);
                    }
                })
        );

        return sections;
    }
}
