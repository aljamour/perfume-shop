package aljamour.perfumeshop.controller.admin;

import aljamour.perfumeshop.model.Gender;
import aljamour.perfumeshop.model.Perfume;
import aljamour.perfumeshop.repository.CustomerOrderRepository;
import aljamour.perfumeshop.service.AdminPerfumeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final AdminPerfumeService adminPerfumeService;
    private final CustomerOrderRepository orderRepository;

    public AdminController(AdminPerfumeService adminPerfumeService, CustomerOrderRepository orderRepository) {
        this.adminPerfumeService = adminPerfumeService;
        this.orderRepository = orderRepository;
    }

    @GetMapping
    public String dashboard(Model model) {
        var perfumes = adminPerfumeService.all();
        model.addAttribute("perfumes", perfumes);
        model.addAttribute("orders", orderRepository.findTop10ByOrderByCreatedAtDesc());
        model.addAttribute("activeCount", perfumes.stream().filter(Perfume::isActive).count());
        model.addAttribute("productCount", perfumes.size());
        model.addAttribute("orderCount", orderRepository.count());
        return "admin/dashboard";
    }

    @GetMapping("/perfumes/new")
    public String createForm(Model model) {
        Perfume perfume = new Perfume();
        perfume.setGender(Gender.UNISEX);
        perfume.setActive(true);
        model.addAttribute("perfume", perfume);
        model.addAttribute("genders", Gender.values());
        return "admin/perfume-form";
    }

    @GetMapping("/perfumes/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("perfume", adminPerfumeService.get(id));
        model.addAttribute("genders", Gender.values());
        return "admin/perfume-form";
    }

    @PostMapping("/perfumes/save")
    public String save(
            @RequestParam(required = false) Long id,
            @RequestParam String perfumeNumber,
            @RequestParam String name,
            @RequestParam(required = false) String brand,
            @RequestParam Gender gender,
            @RequestParam(required = false) String inspiredBy,
            @RequestParam String slug,
            @RequestParam(required = false) String shortDescription,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String imageUrl,
            @RequestParam(defaultValue = "false") boolean active,
            @RequestParam(defaultValue = "false") boolean featured
    ) {
        Perfume perfume = id == null ? new Perfume() : adminPerfumeService.get(id);
        perfume.setPerfumeNumber(perfumeNumber);
        perfume.setName(name);
        perfume.setBrand(brand);
        perfume.setGender(gender);
        perfume.setInspiredBy(inspiredBy);
        perfume.setSlug(slug);
        perfume.setShortDescription(shortDescription);
        perfume.setDescription(description);
        perfume.setImageUrl(imageUrl);
        perfume.setActive(active);
        perfume.setFeatured(featured);
        adminPerfumeService.save(perfume);
        return "redirect:/admin";
    }

    @PostMapping("/perfumes/{id}/sizes")
    public String addSize(@PathVariable Long id, @RequestParam int ml) {
        adminPerfumeService.addSize(id, ml);
        return "redirect:/admin/perfumes/" + id + "/edit";
    }

    @PostMapping("/sizes/{sizeId}")
    public String updateSize(
            @PathVariable Long sizeId,
            @RequestParam Long perfumeId,
            @RequestParam(required = false) BigDecimal sellingPrice,
            @RequestParam(defaultValue = "0") Integer stockQuantity,
            @RequestParam(defaultValue = "false") Boolean active
    ) {
        adminPerfumeService.updateSize(sizeId, sellingPrice, stockQuantity, active);
        return "redirect:/admin/perfumes/" + perfumeId + "/edit";
    }

    @PostMapping("/perfumes/{id}/deactivate")
    public String deactivate(@PathVariable Long id) {
        adminPerfumeService.deactivate(id);
        return "redirect:/admin";
    }
}
