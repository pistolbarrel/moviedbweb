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
        final int DURATION_INDEX = 2;
        final int COLLECTIONS_INDEX = 3;
        final int TITLE_INDEX = 5;
        final int TITLE_EXTRA_INDEX = 6;
        // the following can be influenced with parseAdjust
        final int DIRECTORS_INDEX = 6;
        final int COUNTRIES_INDEX = 7;
        final int ACTORS_INDEX = 8;
        final int DESCRIPTION_INDEX = 10;

        List<String> extractedStrings = List.of(movieInfo.info.split("\r?\n|\r"));
        int parseAdjust = 0;
        if (extractedStrings.size() >= 12) {
            /*
             Looking for a couple of possibilities here.
             1) The movie has the title and the title Watched lines entered
             2) the description is multi line
             The 1st will cause an index change for most of the parsing
             */
            if (extractedStrings.get(TITLE_EXTRA_INDEX).contains(extractedStrings.get(TITLE_INDEX))) {
                parseAdjust = 1;
            }
        }
        StringBuilder descriptionBuilder = new StringBuilder(extractedStrings.get(DESCRIPTION_INDEX + parseAdjust));
        // the following will not trigger unless extractedStrings.size() >= 12
        for (int idx = DESCRIPTION_INDEX + parseAdjust + 1; idx < extractedStrings.size() - 1; idx++) {
            descriptionBuilder.append(System.getProperty("line.separator"));
            descriptionBuilder.append(extractedStrings.get(idx));
        }

        Title title = new Title(extractedStrings.get(TITLE_INDEX));
        MovieDto dto = new MovieDto(
                title.getName(),
                replaceNoneWithEmpty(title.getYear()),
                replaceNoneWithEmpty(descriptionBuilder.toString()),
                replaceNoneWithEmpty(extractedStrings.get(ACTORS_INDEX + parseAdjust)),
                replaceNoneWithEmpty(extractedStrings.get(DIRECTORS_INDEX + parseAdjust)),
                replaceNoneWithEmpty(
                        extractedStrings.get(COUNTRIES_INDEX + parseAdjust).replace(",", ";")
                ),
                extractedStrings.get(COLLECTIONS_INDEX),
                LocalDate.now().toString(),
                extractedStrings.get(DURATION_INDEX),
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