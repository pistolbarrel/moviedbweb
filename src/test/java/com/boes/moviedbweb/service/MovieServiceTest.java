package com.boes.moviedbweb.service;

import com.boes.moviedbweb.entity.Movie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MovieServiceTest {

    @Autowired
    MovieService movieService;

    @Test
    void printMovie() {
        Movie movie = movieService.getMovie("Greg's Big Adventure", "2022");
        movieService.addActors("Greg's Big Adventure", "2022", "Greg Boes");
        System.out.println("movie = " + movie);
    }
}