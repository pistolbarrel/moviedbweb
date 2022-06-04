package com.boes.moviedbweb.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieCollectionDto {
    // semi-colon separated titles in the form 'title (YEAR)'
    private String titles;
    // collection to add to existing movies
    private String collection;
}
