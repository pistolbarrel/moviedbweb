package com.boes.moviedbweb.dto;

import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MovieDto {
    @NotBlank(message="title is required")
    private String title;
    private String year;
    private String description;
    @javax.validation.constraints.NotNull
    private String actors;
    @javax.validation.constraints.NotNull
    private String directors;
    private String countries;
    @javax.validation.constraints.NotNull
    private String collections;
    // in the form yyyy/mm/dd
    @Pattern(regexp="^\\d{4}-\\d{2}-\\d{2}$", message="date should be in yyyy-mm-dd format")
    private String viewDate;
//    @Pattern(regexp = "^(?:(?:([01]?\\d|2[0-3]):)?([0-5]?\\d):)?([0-5]?\\d)$", message = "duration in HH:MM:SS")
    private String duration;
}
