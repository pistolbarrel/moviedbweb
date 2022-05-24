package com.boes.moviedbweb.controller;

import com.boes.moviedbweb.dto.MovieDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DatabaseMaintRestControllerTest {

    @Autowired
    DatabaseMaintRestController controller;

    @Test
    void createMovie() {
        MovieDto movie = movingMovie();
        controller.createMovie(movie);
    }

    @Test
    void createMovieNoTitle() {
        MovieDto movie = movingMovie();
        movie.setTitle(null);
        controller.createMovie(movie);
    }

    private MovieDto movingMovie() {
        return MovieDto.builder()
                .title("Greg's BIG Adventure")
                .year("2222")
                .description("On their long journey to put Bismarck in the rear view mirror, the family encounters stuff.")
                .actors("Aaron Boes; Ann Boes; Greg Boes")
                .directors("Ann Boes")
                .countries("United States")
                .collections("Outta Here!")
                .viewDate("2022-05-11")
                .duration("2:17:38")
                .build();
    }
}