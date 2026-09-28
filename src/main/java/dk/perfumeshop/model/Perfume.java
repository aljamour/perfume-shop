package dk.perfumeshop.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "perfumes")
public class Perfume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "perfume_number", unique = true, nullable = false, length = 30)
    private String perfumeNumber;

    @Column(nullable = false)
    private String name;

    private String brand;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender = Gender.UNISEX;

    @Column(name = "inspired_by")
    private String inspiredBy;

    @Column(unique = true, nullable = false)
    private String slug;

    @Column(length = 300)
    private String shortDescription;

    @Column(length = 2500)
    private String description;

    private String imageUrl;

    private boolean active = true;
    private boolean featured = false;
    private boolean needsReview = false;

    @OneToMany(mappedBy = "perfume", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("ml ASC")
    private List<PerfumeSize> sizes = new ArrayList<>();

    @OneToMany(mappedBy = "perfume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ScentNote> scentNotes = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "perfume_categories",
            joinColumns = @JoinColumn(name = "perfume_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addSize(PerfumeSize size) {
        sizes.add(size);
        size.setPerfume(this);
    }

    public void addScentNote(ScentNote note) {
        scentNotes.add(note);
        note.setPerfume(this);
    }
}
