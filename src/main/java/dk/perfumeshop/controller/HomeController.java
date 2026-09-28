package dk.perfumeshop.controller;

import dk.perfumeshop.service.PerfumeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final PerfumeService perfumeService;

    public HomeController(PerfumeService perfumeService) {
        this.perfumeService = perfumeService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("featured", perfumeService.featured());
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
