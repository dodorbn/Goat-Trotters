package goat.trotters.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CountryStatDTO {
    private String country;
    private double yes;
    private double no;
}