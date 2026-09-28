package dk.perfumeshop.controller;

import dk.perfumeshop.dto.CheckoutForm;
import dk.perfumeshop.service.CartService;
import dk.perfumeshop.service.OrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/checkout")
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;

    public CheckoutController(CartService cartService, OrderService orderService) {
        this.cartService = cartService;
        this.orderService = orderService;
    }

    @GetMapping
    public String checkout(HttpSession session, Model model) {
        var lines = cartService.resolve(cart(session));
        if (lines.isEmpty()) return "redirect:/cart";
        model.addAttribute("checkoutForm", new CheckoutForm());
        model.addAttribute("lines", lines);
        model.addAttribute("total", cartService.total(lines));
        return "checkout";
    }

    @PostMapping
    public String placeOrder(
            @Valid @ModelAttribute CheckoutForm checkoutForm,
            BindingResult bindingResult,
            HttpSession session,
            Model model
    ) {
        var lines = cartService.resolve(cart(session));
        if (bindingResult.hasErrors()) {
            model.addAttribute("lines", lines);
            model.addAttribute("total", cartService.total(lines));
            return "checkout";
        }

        var order = orderService.create(checkoutForm, lines);
        session.removeAttribute("cart");
        return "redirect:/checkout/success/" + order.getId();
    }

    @GetMapping("/success/{orderId}")
    public String success(@PathVariable Long orderId, Model model) {
        model.addAttribute("orderId", orderId);
        return "order-success";
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
