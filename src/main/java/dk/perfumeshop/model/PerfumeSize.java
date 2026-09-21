package dk.perfumeshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "perfume_sizes")
public class PerfumeSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int ml;
    private String type;
    private Double price;

    @ManyToOne
    @JoinColumn(name = "perfume_id", nullable = false)
    private Perfume perfume;
}