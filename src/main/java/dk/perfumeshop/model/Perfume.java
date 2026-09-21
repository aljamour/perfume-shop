package dk.perfumeshop.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "perfumes")
public class Perfume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "perfume_number", unique = true, nullable = false)
    private String perfumeNumber;

    @Column(nullable = false)
    private String name;

    private String brand;

    private String gender;

    @Column(name = "inspired_by")
    private String inspiredBy;

    @Column(length = 2000)
    private String description;

    private String image;

    private boolean needsReview;

    @OneToMany(
            mappedBy = "perfume",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<PerfumeSize> sizes = new ArrayList<>();
}