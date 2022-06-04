package com.boes.moviedbweb.dto;

import com.boes.moviedbweb.utils.Title;
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
    private boolean absolute;

    public static MovieDto convertToMovieDto(MovieInfoDto movieInfo) {
        List<String> extractedStrings = List.of(movieInfo.info.split("\r?\n|\r"));
        StringBuilder descriptionBuilder = new StringBuilder();
        if (extractedStrings.size() >= 12) {
            for (int idx = 10; idx < extractedStrings.size() - 1; idx++) {
                descriptionBuilder.append(extractedStrings.get(idx));
                descriptionBuilder.append(System.getProperty("line.separator"));
            }
        }

        Title title = new Title(extractedStrings.get(5));
        String s = extractedStrings.get(7);
        String fixedCountries = s.replace(",", ";");
        MovieDto dto = new MovieDto(
                title.getName(),
                replaceNoneWithEmpty(title.getYear()),
                replaceNoneWithEmpty(descriptionBuilder.toString()),  // description
                replaceNoneWithEmpty(extractedStrings.get(8)),    // actors
                replaceNoneWithEmpty(extractedStrings.get(6)),    // directors
                replaceNoneWithEmpty(fixedCountries),
                extractedStrings.get(3),    // collections
                LocalDate.now().toString(), // viewDate
                extractedStrings.get(2),    // duration
                movieInfo.isAbsolute()
        );
        if (!StringUtils.isBlank(movieInfo.viewDate)) {
            dto.setViewDate(replaceNoneWithEmpty(movieInfo.viewDate));
        }
        return dto;
    }

    static String replaceNoneWithEmpty(String input) {
        return input.equals("NONE") ? "" : input;
    }
}