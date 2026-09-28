package dk.perfumeshop.repository;

import dk.perfumeshop.model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {
    List<CustomerOrder> findTop10ByOrderByCreatedAtDesc();
}
