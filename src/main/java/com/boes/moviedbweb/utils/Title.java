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
        year = titleAndYear.substring(titleAndYear.indexOf('(') + 1, titleAndYear.indexOf(')'));
        name = titleAndYear.substring(0, titleAndYear.indexOf('(') - 1);
    }
}
