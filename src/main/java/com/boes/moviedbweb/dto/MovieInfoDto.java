package com.boes.moviedbweb.dto;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieInfoDto {
    private String info;
    private String viewDate;

    public static final MovieDto convertToMovieDto(MovieInfoDto movieInfo) {
        List<String> extractedStrings = List.of(movieInfo.info.split("\r?\n|\r"));
        String s = extractedStrings.get(5);

        String extractYear = s.substring(s.indexOf('(') + 1, s.indexOf(')'));
        String extractTitle = s.substring(0, s.indexOf('(') - 1);
        s = extractedStrings.get(7);
        String fixedCountries = s.replace(",", ";");
        MovieDto dto = new MovieDto(
                extractTitle,
                extractYear,
                extractedStrings.get(10),   // description
                extractedStrings.get(8),    // actors
                extractedStrings.get(6),    // directors
                fixedCountries,
                extractedStrings.get(3),    // collections
                LocalDate.now().toString(), // viewDate
                extractedStrings.get(2)     // duration
        );
        if (!StringUtils.isBlank(movieInfo.viewDate)) {
            dto.setViewDate(movieInfo.viewDate);
        }
        return dto;
    }
}