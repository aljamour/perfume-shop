package aljamour.perfumeshop.controller;

import aljamour.perfumeshop.dto.PerfumeCardDto;
import aljamour.perfumeshop.service.PerfumeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/perfumes")
public class ApiPerfumeController {

    private final PerfumeService perfumeService;

    public ApiPerfumeController(PerfumeService perfumeService) {
        this.perfumeService = perfumeService;
    }

    @GetMapping
    public List<PerfumeCardDto> all(@RequestParam(required = false) String q) {
        return perfumeService.search(q).stream().map(PerfumeCardDto::from).toList();
    }

    @GetMapping("/{slug}")
    public PerfumeCardDto one(@PathVariable String slug) {
        return PerfumeCardDto.from(perfumeService.bySlug(slug));
    }
}
