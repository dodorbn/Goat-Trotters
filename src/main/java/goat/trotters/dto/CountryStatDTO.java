package goat.trotters.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class CountryStatDTO {
    private String country;
    private Map<String, Long> counts;
}