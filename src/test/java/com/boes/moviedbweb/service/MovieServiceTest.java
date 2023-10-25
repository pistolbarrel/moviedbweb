package com.boes.moviedbweb.service;

import com.boes.moviedbweb.entity.Movie;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MovieServiceTest {

    @Autowired
    MovieService movieService;

    @Disabled
    @Test
    void printMovie() {
        Movie movie = movieService.getMovie("Greg's Big Adventure", "2022");
        movieService.addActors("Greg's Big Adventure", "2022", "Greg Boes");
        System.out.println("movie = " + movie);
    }
}