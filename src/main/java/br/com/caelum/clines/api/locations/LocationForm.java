package br.com.caelum.clines.api.locations;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
@Getter
public class LocationForm {
    @NotBlank
    private String country;

    @NotBlank
    @Size(max = 2)
    private String state;

    @NotBlank
    private String city;
}
