package com.boes.moviedbweb.dto;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovieInfoDtoTest {

    @Test
    void convertToMovieDto() {
        String info = "======================================================\n" +
                "30\n" +
                "2:07:29\n" +
                "Criterion:Foreign-Language Oscar Winners\n" +
                "https://www.criterionchannel.com/foreign-language-oscar-winners/season:1/videos/amour\n" +
                "Amour (2012) Watched\n" +
                "Michael Haneke \n" +
                "France, Austria\n" +
                "Jean-Louis Trintignant; Emmanuelle Riva; Isabelle Huppert\n" +
                "\n" +
                "Georges (Jean-Louis Trintignant) and Anne (Emmanuelle Riva) are cultivated, retired music teachers who have known a lifetime of love within their marriage. Though their bond has survived time’s test, it’s about to meet its greatest challenge when Anne suffers a stroke. Featuring tour-de-force performances from Trintignant, Riva, and Isabelle Huppert, Michael Haneke’s heartrending human drama—winner of both the Palme d’Or and the Academy Award for Best Foreign Language Film—exalts the beautiful, compassionate, and courageous within us all.\n" +
                "======================================================\n";
        MovieInfoDto movieInfoDto = new MovieInfoDto(info, "", false);
        MovieDto dto = MovieInfoDto.convertToMovieDto(movieInfoDto);

        assertEquals("Amour", dto.getTitle());
        assertEquals("Jean-Louis Trintignant; Emmanuelle Riva; Isabelle Huppert", dto.getActors());
        assertEquals("2012", dto.getYear());
        assertEquals("Georges (Jean-Louis Trintignant) and Anne (Emmanuelle Riva) are cultivated, retired music " +
                        "teachers who have known a lifetime of love within their marriage. Though their bond has survived " +
                        "time’s test, it’s about to meet its greatest challenge when Anne suffers a stroke. Featuring " +
                        "tour-de-force performances from Trintignant, Riva, and Isabelle Huppert, Michael Haneke’s " +
                        "heartrending human drama—winner of both the Palme d’Or and the Academy Award for Best Foreign " +
                        "Language Film—exalts the beautiful, compassionate, and courageous within us all.",
                dto.getDescription());
        assertEquals("Michael Haneke ", dto.getDirectors());
        assertEquals("France; Austria", dto.getCountries());
        assertEquals("Criterion:Foreign-Language Oscar Winners", dto.getCollections());
        assertEquals("2023-10-25", dto.getViewDate());
        assertEquals("2:07:29", dto.getDuration());
    }
}