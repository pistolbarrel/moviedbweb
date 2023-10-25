package com.boes.moviedbweb.entity;

import com.boes.moviedbweb.utils.MovieUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovieTest {

    @Test
    void setDuration() {
        Movie movie = Movie.builder()
                .title("One")
                .year("2222")
                .build();

        movie.setDuration(MovieUtils.convertHHMMSSToInteger("1:07:30"));

        int a = 42;
        assertEquals("One", movie.getTitle());
        assertEquals("2222", movie.getYear());
        assertEquals(4050, movie.getDuration());
    }

    @Test
    void testGetDisplayDuration()
    {
        Movie movie = Movie.builder()
                .duration(4050)
                .build();

        String result = MovieUtils.getDisplayDuration(movie.getDuration());
        assertEquals("1:07:30", result);
    }
}