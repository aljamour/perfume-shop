package aljamour.perfumeshop.controller;

import aljamour.perfumeshop.model.Gender;
import aljamour.perfumeshop.service.PerfumeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class CatalogController {

    private final PerfumeService perfumeService;

    public CatalogController(PerfumeService perfumeService) {
        this.perfumeService = perfumeService;
    }

    @GetMapping("/catalog")
    public String catalog(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Gender gender,
            Model model
    ) {
        var perfumes = gender != null ? perfumeService.byGender(gender) : perfumeService.search(q);
        model.addAttribute("perfumes", perfumes);
        model.addAttribute("query", q == null ? "" : q);
        model.addAttribute("selectedGender", gender);
        return "catalog";
    }

    @GetMapping("/perfumer/{slug}")
    public String detail(@PathVariable String slug, Model model) {
        model.addAttribute("perfume", perfumeService.bySlug(slug));
        return "perfume-detail";
    }
}
