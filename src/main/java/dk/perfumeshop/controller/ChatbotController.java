package dk.perfumeshop.controller;

import dk.perfumeshop.dto.RecommendationDto;
import dk.perfumeshop.service.RecommendationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    private final RecommendationService recommendationService;

    public ChatbotController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping
    public List<RecommendationDto> recommend(@RequestParam String q) {
        return recommendationService.recommend(q);
    }
}
