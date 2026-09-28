package dk.perfumeshop.config;

import dk.perfumeshop.model.*;
import dk.perfumeshop.repository.CategoryRepository;
import dk.perfumeshop.repository.PerfumeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

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
    public void run(String... args) {
        if (perfumeRepository.count() > 0) {
            return;
        }

        Map<String, Category> categories = new LinkedHashMap<>();
        categories.put("popular", category("Mest populære", "popular", "Kundernes mest efterspurgte dufte."));
        categories.put("office", category("Kontor & hverdag", "office", "Elegante og diskrete dufte til hverdagen."));
        categories.put("summer", category("Sommer & ferie", "summer", "Friske og lette dufte til varme dage."));
        categories.put("winter", category("Efterår & vinter", "winter", "Varme, dybe og omsluttende dufte."));
        categories.put("date-night", category("Date night", "date-night", "Varme og sensuelle dufte til aftenen."));
        categories.put("signature", category("Signature Collection", "signature", "Særlige dufte med markant karakter."));
        categoryRepository.saveAll(categories.values());

        save(perfume("001", "One Million", "Paco Rabanne", Gender.MEN, "one-million", true,
                List.of("popular", "date-night"), List.of(15, 30, 70), categories,
                notes("blodappelsin, grapefrugt, mynte", "rose, kanel", "læder, patchouli")));

        save(perfume("002", "Acqua di Gio", "Giorgio Armani", Gender.MEN, "acqua-di-gio", true,
                List.of("popular", "summer"), List.of(15, 30, 70), categories,
                notes("bergamot, citrus", "marine noter", "cedertræ, patchouli")));

        save(perfume("038", "Bleu de Chanel", "Chanel", Gender.MEN, "bleu-de-chanel", true,
                List.of("popular", "office"), List.of(15, 30, 70), categories,
                notes("citrus", "aromatiske noter", "trænoter")));

        save(perfume("068", "Aventus", "Creed", Gender.MEN, "aventus", true,
                List.of("popular", "signature"), List.of(15, 30, 70), categories,
                notes("bergamot, ananas", "jasmin", "moskus, trænoter")));

        save(perfume("079", "MYSLF", "Yves Saint Laurent", Gender.MEN, "myslf", true,
                List.of("popular", "office"), List.of(15, 30, 70), categories,
                notes("bergamot", "appelsinblomst", "trænoter")));

        save(perfume("094", "Sauvage", "Christian Dior", Gender.MEN, "sauvage", true,
                List.of("popular", "office"), List.of(15, 30, 70), categories,
                notes("bergamot, peber", "lavendel", "ambroxan")));

        save(perfume("140", "Éros", "Versace", Gender.MEN, "eros", true,
                List.of("popular", "date-night"), List.of(15, 30, 70), categories,
                notes("mynte, citrus", "tonkabønne", "vanilje, trænoter")));

        save(perfume("162M", "Valentino Uomo Born In Roma Intense", "Valentino", Gender.MEN, "valentino-uomo-born-in-roma-intense", true,
                List.of("popular", "date-night"), List.of(15, 30, 70), categories,
                notes("vanilje", "lavendel", "vetiver")));

        save(perfume("051", "Coco Mademoiselle", "Chanel", Gender.WOMEN, "coco-mademoiselle", true,
                List.of("popular", "office"), List.of(15, 30, 70), categories,
                notes("citrus", "rose, jasmin", "patchouli, vanilje")));

        save(perfume("080", "Sì", "Giorgio Armani", Gender.WOMEN, "si", true,
                List.of("popular", "office"), List.of(15, 30, 70), categories,
                notes("solbær", "rose", "vanilje, patchouli")));

        save(perfume("085", "Chance", "Chanel", Gender.WOMEN, "chance", false,
                List.of("office"), List.of(15, 30, 70), categories,
                notes("citrus, peber", "jasmin", "patchouli")));

        save(perfume("089", "Mon Paris", "Yves Saint Laurent", Gender.WOMEN, "mon-paris", true,
                List.of("popular", "date-night"), List.of(15, 30, 70), categories,
                notes("frugtige noter", "jasmin", "patchouli, moskus")));

        save(perfume("090", "Poison Girl", "Christian Dior", Gender.WOMEN, "poison-girl", true,
                List.of("popular", "date-night"), List.of(15, 30, 70), categories,
                notes("citrus", "rose", "vanilje, tonkabønne")));

        save(perfume("122", "Libre", "Yves Saint Laurent", Gender.WOMEN, "libre", true,
                List.of("popular", "signature"), List.of(15, 30, 70), categories,
                notes("lavendel, citrus", "appelsinblomst", "vanilje, cedertræ")));

        save(perfume("131", "Good Girl", "Carolina Herrera", Gender.WOMEN, "good-girl", true,
                List.of("popular", "date-night"), List.of(15, 30, 70), categories,
                notes("mandel", "jasmin", "tonkabønne, kakao")));

        save(perfume("112", "Neroli Portofino", "Tom Ford", Gender.UNISEX, "neroli-portofino", false,
                List.of("summer"), List.of(15, 50), categories,
                notes("bergamot, citrus", "neroli", "rav")));

        save(perfume("114", "Ombre Nomade", "Louis Vuitton", Gender.UNISEX, "ombre-nomade", true,
                List.of("signature", "winter"), List.of(15, 30, 70), categories,
                notes("røgelse", "oud", "hindbær, trænoter")));

        save(perfume("118", "Baccarat Rouge 540", "Maison Francis Kurkdjian", Gender.UNISEX, "baccarat-rouge-540", true,
                List.of("signature", "date-night"), List.of(15, 50), categories,
                notes("safran", "jasmin", "rav, cedertræ")));

        save(perfume("127", "Oud Wood", "Tom Ford", Gender.UNISEX, "oud-wood", true,
                List.of("office", "winter", "signature"), List.of(15, 50), categories,
                notes("krydderier", "oud, sandeltræ", "rav, vanilje")));

        save(perfume("129", "Erba Pura", "Xerjoff", Gender.UNISEX, "erba-pura", true,
                List.of("summer", "signature"), List.of(15, 50), categories,
                notes("citrus", "frugtige noter", "moskus, vanilje")));

        save(perfume("143", "Vanille Powder", "Matière Première", Gender.UNISEX, "vanille-powder", false,
                List.of("winter", "date-night"), List.of(15, 50), categories,
                notes("kokos", "vanilje", "moskus")));

        save(perfume("117", "Tobacco Vanille", "Tom Ford", Gender.UNISEX, "tobacco-vanille", true,
                List.of("winter", "date-night"), List.of(15, 50), categories,
                notes("tobaksblade, krydderier", "tonkabønne", "vanilje, kakao")));

        save(perfume("111", "Lost Cherry", "Tom Ford", Gender.UNISEX, "lost-cherry", true,
                List.of("date-night", "signature"), List.of(15, 50), categories,
                notes("kirsebær", "mandel", "vanilje, trænoter")));

        save(perfume("144", "Bianco Latte", "Giardini di Toscana", Gender.UNISEX, "bianco-latte", true,
                List.of("winter", "date-night"), List.of(15, 50), categories,
                notes("karamel", "honning", "vanilje, moskus")));

        save(perfume("146", "Balmain Rouge", "Pierre Balmain", Gender.UNISEX, "balmain-rouge", false,
                List.of("signature"), List.of(15, 50), categories,
                notes("frugtige noter", "blomster", "trænoter")));
    }

    private Category category(String name, String slug, String description) {
        Category category = new Category();
        category.setName(name);
        category.setSlug(slug);
        category.setDescription(description);
        return category;
    }

    private Perfume perfume(
            String number,
            String name,
            String brand,
            Gender gender,
            String slug,
            boolean featured,
            List<String> categorySlugs,
            List<Integer> sizes,
            Map<String, Category> categories,
            Map<NoteType, List<String>> notes
    ) {
        Perfume perfume = new Perfume();
        perfume.setPerfumeNumber(number);
        perfume.setName(name);
        perfume.setBrand(brand);
        perfume.setGender(gender);
        perfume.setInspiredBy(name + " by " + brand);
        perfume.setSlug(slug);
        perfume.setShortDescription("En elegant inspirationsduft med karakter og en luksuriøs profil.");
        perfume.setDescription("Denne duft er en inspirationsduft med reference til den angivne original. Varemærker og produktnavne bruges alene som duftreference; butikken er ikke tilknyttet de originale producenter.");
        perfume.setActive(true);
        perfume.setFeatured(featured);
        perfume.setNeedsReview(false);

        categorySlugs.forEach(slugValue -> perfume.getCategories().add(categories.get(slugValue)));

        for (Integer ml : sizes) {
            PerfumeSize size = new PerfumeSize();
            size.setMl(ml);
            size.setType("Extrait de Parfum");
            size.setActive(true);
            size.setStockQuantity(0);
            size.setSellingPrice(null);
            perfume.addSize(size);
        }

        notes.forEach((type, values) -> values.forEach(value -> {
            ScentNote note = new ScentNote();
            note.setNoteType(type);
            note.setNoteName(value);
            perfume.addScentNote(note);
        }));

        return perfume;
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

    private void save(Perfume perfume) {
        perfumeRepository.save(perfume);
    }
}
