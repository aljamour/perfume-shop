package aljamour.perfumeshop.service;

import aljamour.perfumeshop.dto.CartLineDto;
import aljamour.perfumeshop.dto.CheckoutForm;
import aljamour.perfumeshop.model.CustomerOrder;
import aljamour.perfumeshop.model.OrderItem;
import aljamour.perfumeshop.repository.CustomerOrderRepository;
import aljamour.perfumeshop.repository.PerfumeSizeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
public class OrderService {

    private final CustomerOrderRepository orderRepository;
    private final PerfumeSizeRepository sizeRepository;

    public OrderService(CustomerOrderRepository orderRepository, PerfumeSizeRepository sizeRepository) {
        this.orderRepository = orderRepository;
        this.sizeRepository = sizeRepository;
    }

    public CustomerOrder create(CheckoutForm form, List<CartLineDto> lines) {
        if (lines.isEmpty()) {
            throw new IllegalArgumentException("Kurven er tom.");
        }

        CustomerOrder order = new CustomerOrder();
        order.setFirstName(form.getFirstName());
        order.setLastName(form.getLastName());
        order.setEmail(form.getEmail());
        order.setPhone(form.getPhone());
        order.setAddress(form.getAddress());
        order.setPostalCode(form.getPostalCode());
        order.setCity(form.getCity());

        BigDecimal total = BigDecimal.ZERO;

        for (CartLineDto line : lines) {
            var size = sizeRepository.findById(line.sizeId())
                    .orElseThrow(() -> new IllegalArgumentException("En vare findes ikke længere."));

            if (!size.isAvailable() || size.getStockQuantity() < line.quantity()) {
                throw new IllegalArgumentException("En vare er ikke længere på lager i det ønskede antal.");
            }

            OrderItem item = new OrderItem();
            item.setPerfumeName(size.getPerfume().getName());
            item.setPerfumeNumber(size.getPerfume().getPerfumeNumber());
            item.setSizeMl(size.getMl());
            item.setQuantity(line.quantity());
            item.setUnitPrice(size.getSellingPrice());
            order.addItem(item);

            size.setStockQuantity(size.getStockQuantity() - line.quantity());
            total = total.add(size.getSellingPrice().multiply(BigDecimal.valueOf(line.quantity())));
        }

        order.setTotal(total);
        return orderRepository.save(order);
    }
}
