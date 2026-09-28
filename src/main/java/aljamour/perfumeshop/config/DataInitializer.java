package aljamour.perfumeshop.config;

import aljamour.perfumeshop.model.Category;
import aljamour.perfumeshop.model.NoteType;
import aljamour.perfumeshop.model.Perfume;
import aljamour.perfumeshop.model.PerfumeSize;
import aljamour.perfumeshop.model.ScentNote;
import aljamour.perfumeshop.repository.CategoryRepository;
import aljamour.perfumeshop.repository.PerfumeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class DataInitializer implements CommandLineRunner {

    private final PerfumeRepository perfumeRepository;
    private final CategoryRepository categoryRepository;

    public DataInitializer(PerfumeRepository perfumeRepository, CategoryRepository categoryRepository) {
        this.perfumeRepository = perfumeRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Map<String, Category> categories = ensureCategories();

        for (CatalogSeedData.ProductSeed seed : CatalogSeedData.products()) {
            Perfume perfume = perfumeRepository.findByPerfumeNumber(seed.perfumeNumber())
                    .orElseGet(() -> createPerfume(seed));

            ensureSizes(perfume, seed.sizes());

            if (perfume.getCategories().isEmpty()) {
                seed.categorySlugs().forEach(slug -> {
                    Category category = categories.get(slug);
                    if (category != null) perfume.getCategories().add(category);
                });
            }

            if (perfume.getScentNotes().isEmpty()) {
                addKnownNotes(perfume, seed.perfumeNumber());
            }

            perfumeRepository.save(perfume);
        }
    }

    private Map<String, Category> ensureCategories() {
        Map<String, String[]> definitions = new LinkedHashMap<>();
        definitions.put("popular", new String[]{"Mest populære", "Kundernes mest efterspurgte dufte."});
        definitions.put("office", new String[]{"Kontor & hverdag", "Elegante og diskrete dufte til hverdagen."});
        definitions.put("summer", new String[]{"Sommer & ferie", "Friske og lette dufte til varme dage."});
        definitions.put("winter", new String[]{"Efterår & vinter", "Varme, dybe og omsluttende dufte."});
        definitions.put("date-night", new String[]{"Date night", "Varme og sensuelle dufte til aftenen."});
        definitions.put("signature", new String[]{"Signature Collection", "Særlige dufte med markant karakter."});

        Map<String, Category> result = new LinkedHashMap<>();

        definitions.forEach((slug, values) -> {
            Category category = categoryRepository.findBySlug(slug).orElseGet(() -> {
                Category created = new Category();
                created.setName(values[0]);
                created.setSlug(slug);
                created.setDescription(values[1]);
                return categoryRepository.save(created);
            });
            result.put(slug, category);
        });

        return result;
    }

    private Perfume createPerfume(CatalogSeedData.ProductSeed seed) {
        Perfume perfume = new Perfume();
        perfume.setPerfumeNumber(seed.perfumeNumber());
        perfume.setName(seed.name());
        perfume.setBrand(seed.brand());
        perfume.setGender(seed.gender());
        perfume.setInspiredBy(seed.name() + " by " + seed.brand());
        perfume.setSlug(seed.slug());
        perfume.setShortDescription("En inspirationsduft med reference til " + seed.name() + ".");
        perfume.setDescription("Originale mærke- og produktnavne anvendes alene som duftreference. Produktet er en inspirationsduft og butikken er ikke tilknyttet den originale producent.");
        perfume.setActive(true);
        perfume.setFeatured(seed.featured());
        perfume.setNeedsReview(false);
        return perfume;
    }

    private void ensureSizes(Perfume perfume, List<Integer> wantedSizes) {
        Set<Integer> existing = new HashSet<>();
        perfume.getSizes().forEach(size -> existing.add(size.getMl()));

        wantedSizes.stream()
                .filter(ml -> !existing.contains(ml))
                .forEach(ml -> {
                    PerfumeSize size = new PerfumeSize();
                    size.setMl(ml);
                    size.setType("Extrait de Parfum");
                    size.setActive(true);
                    size.setStockQuantity(0);
                    size.setSellingPrice(null);
                    perfume.addSize(size);
                });
    }

    private void addKnownNotes(Perfume perfume, String perfumeNumber) {
        Map<NoteType, List<String>> notes = switch (perfumeNumber) {
            case "001" -> notes("blodappelsin, grapefrugt, mynte", "rose, kanel", "læder, patchouli");
            case "002" -> notes("bergamot, citrus", "marine noter", "cedertræ, patchouli");
            case "038" -> notes("citrus", "aromatiske noter", "trænoter");
            case "068" -> notes("bergamot, ananas", "jasmin", "moskus, trænoter");
            case "079" -> notes("bergamot", "appelsinblomst", "trænoter");
            case "094" -> notes("bergamot, peber", "lavendel", "ambroxan");
            case "140" -> notes("mynte, citrus", "tonkabønne", "vanilje, trænoter");
            case "162M" -> notes("vanilje", "lavendel", "vetiver");
            case "051" -> notes("citrus", "rose, jasmin", "patchouli, vanilje");
            case "080" -> notes("solbær", "rose", "vanilje, patchouli");
            case "085" -> notes("citrus, peber", "jasmin", "patchouli");
            case "089" -> notes("frugtige noter", "jasmin", "patchouli, moskus");
            case "090" -> notes("citrus", "rose", "vanilje, tonkabønne");
            case "122" -> notes("lavendel, citrus", "appelsinblomst", "vanilje, cedertræ");
            case "131" -> notes("mandel", "jasmin", "tonkabønne, kakao");
            case "112" -> notes("bergamot, citrus", "neroli", "rav");
            case "114" -> notes("røgelse", "oud", "hindbær, trænoter");
            case "118" -> notes("safran", "jasmin", "rav, cedertræ");
            case "127" -> notes("krydderier", "oud, sandeltræ", "rav, vanilje");
            case "129" -> notes("citrus", "frugtige noter", "moskus, vanilje");
            case "143" -> notes("kokos", "vanilje", "moskus");
            case "117" -> notes("tobaksblade, krydderier", "tonkabønne", "vanilje, kakao");
            case "111" -> notes("kirsebær", "mandel", "vanilje, trænoter");
            case "144" -> notes("karamel", "honning", "vanilje, moskus");
            case "146" -> notes("frugtige noter", "blomster", "trænoter");
            default -> Map.of();
        };

        notes.forEach((type, values) -> values.forEach(value -> {
            ScentNote note = new ScentNote();
            note.setNoteType(type);
            note.setNoteName(value);
            perfume.addScentNote(note);
        }));
    }

    private Map<NoteType, List<String>> notes(String top, String heart, String base) {
        Map<NoteType, List<String>> result = new EnumMap<>(NoteType.class);
        result.put(NoteType.TOP, split(top));
        result.put(NoteType.HEART, split(heart));
        result.put(NoteType.BASE, split(base));
        return result;
    }

    private List<String> split(String value) {
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isBlank())
                .toList();
    }
}
