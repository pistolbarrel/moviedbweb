package com.boes.moviedbweb.utils;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Title {
    private String name;
    private String year;

    public Title(String titleAndYear) {
        name = extractTitle(titleAndYear);
        year = extractYear(titleAndYear);
    }

    static public String extractYear(String titleAndYear) {
        return MovieUtils.replaceNoneWithEmpty(
                titleAndYear.substring(titleAndYear.lastIndexOf('(') + 1, titleAndYear.lastIndexOf(')'))
        );
    }

    static public String extractTitle(String titleAndYear) {
        return titleAndYear.substring(0, titleAndYear.lastIndexOf('(') - 1);
    }

    public String getDisplayName() {
        return name + " (" + year + ")";
    }
}
