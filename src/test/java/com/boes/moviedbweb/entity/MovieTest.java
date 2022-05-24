package com.boes.moviedbweb.entity;

import com.boes.moviedbweb.utils.MovieHtmlHelper;
import com.boes.moviedbweb.utils.MovieUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

class MovieTest {

    @Test
    void setDuration() {
        Movie movie = Movie.builder()
                .title("One")
                .year("2222")
                .build();

        movie.setDuration(MovieUtils.convertHHMMSSToInteger("1:07:30"));

        int a = 42;
    }

    @Test
    void testGetDisplayDuration()
    {
        Movie movie = Movie.builder()
                .duration(4550)
                .build();

        String result = MovieUtils.getDisplayDuration(movie.getDuration());
        System.out.println("result = " + result);
        int a = 42;
    }
}