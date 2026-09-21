package dk.perfumeshop.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScentNotes {

    private List<String> top;
    private List<String> heart;
    private List<String> base;
}