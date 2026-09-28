package dk.perfumeshop.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "perfume_sizes")
public class PerfumeSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int ml;

    @Column(nullable = false)
    private String type = "Extrait de Parfum";

    @Column(precision = 10, scale = 2)
    private BigDecimal sellingPrice;

    @Column(nullable = false)
    private int stockQuantity = 0;

    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "perfume_id", nullable = false)
    private Perfume perfume;

    public boolean isAvailable() {
        return active && stockQuantity > 0 && sellingPrice != null;
    }
}
