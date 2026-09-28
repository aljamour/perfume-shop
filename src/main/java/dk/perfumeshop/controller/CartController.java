package dk.perfumeshop.controller;

import dk.perfumeshop.service.CartService;
import dk.perfumeshop.repository.PerfumeSizeRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final PerfumeSizeRepository sizeRepository;

    public CartController(CartService cartService, PerfumeSizeRepository sizeRepository) {
        this.cartService = cartService;
        this.sizeRepository = sizeRepository;
    }

    @PostMapping("/add")
    public String add(@RequestParam Long sizeId, @RequestParam(defaultValue = "1") int quantity, HttpSession session) {
        var size = sizeRepository.findById(sizeId).orElseThrow();
        if (!size.isAvailable()) {
            throw new IllegalArgumentException("Denne størrelse kan ikke købes endnu.");
        }

        Map<Long, Integer> cart = cart(session);
        int wanted = cart.getOrDefault(sizeId, 0) + Math.max(1, quantity);
        cart.put(sizeId, Math.min(wanted, size.getStockQuantity()));
        return "redirect:/cart";
    }

    @GetMapping
    public String cartPage(HttpSession session, Model model) {
        var lines = cartService.resolve(cart(session));
        model.addAttribute("lines", lines);
        model.addAttribute("total", cartService.total(lines));
        return "cart";
    }

    @PostMapping("/update")
    public String update(@RequestParam Long sizeId, @RequestParam int quantity, HttpSession session) {
        Map<Long, Integer> cart = cart(session);
        if (quantity <= 0) cart.remove(sizeId);
        else cart.put(sizeId, quantity);
        return "redirect:/cart";
    }

    @PostMapping("/remove")
    public String remove(@RequestParam Long sizeId, HttpSession session) {
        cart(session).remove(sizeId);
        return "redirect:/cart";
    }

    @SuppressWarnings("unchecked")
    private Map<Long, Integer> cart(HttpSession session) {
        Object existing = session.getAttribute("cart");
        if (existing instanceof Map<?, ?>) {
            return (Map<Long, Integer>) existing;
        }
        Map<Long, Integer> cart = new LinkedHashMap<>();
        session.setAttribute("cart", cart);
        return cart;
    }
}
