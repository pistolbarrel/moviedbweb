package com.boes.moviedbweb.dto;

import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DurationDirectDto {
    String duration;
    String movieTitle;
    String year;
}
