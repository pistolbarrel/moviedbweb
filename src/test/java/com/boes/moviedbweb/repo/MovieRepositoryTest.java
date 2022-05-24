package com.boes.moviedbweb.repo;

import com.boes.moviedbweb.entity.Actor;
import com.boes.moviedbweb.entity.Director;
import com.boes.moviedbweb.entity.Movie;
import com.boes.moviedbweb.service.ActorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Set;

@SpringBootTest
class MovieRepositoryTest {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ActorService actorService;

    @Autowired
    private DirectorRepository directorRepository;

    @Test
    public void saveMovie() {
        Actor a1 = actorService.getOrCreateActor("Juliette Binoche");
        Actor a2 = actorService.getOrCreateActor("Wadeck Stanczak");
        Actor a3 = actorService.getOrCreateActor("Lambert Wilson");
        Actor a4 = actorService.getOrCreateActor("Michel Piccol");
        Actor a5 = actorService.getOrCreateActor("Denis Lavant");
        Actor a6 = actorService.getOrCreateActor("Julie Delpy");

//        Director d1 = directorRepository.findByName("André Téchiné")
//                .orElse(directorRepository.save(Director.builder().name("André Téchiné").build()));
        Director d2 = directorRepository.findByName("Leos Carax")
                .orElse(directorRepository.save(Director.builder().name("Leos Carax").build()));
//        Director d3 = directorRepository.findByName("Philip Kaufman")
//                .orElse(directorRepository.save(Director.builder().name("Philip Kaufman").build()));
//        Director d4 = directorRepository.findByName("Leos Carax")
//                .orElse(directorRepository.save(Director.builder().name("Leos Carax").build()));
//        Director d5 = directorRepository.findByName("Krzysztof Kieślowski")
//                .orElse(directorRepository.save(Director.builder().name("Krzysztof Kieślowski").build()));

        Movie movie = Movie.builder()
                .description("With this giddily romantic, exquisitely stylized sophomore \" +\n" +
                        "                \"feature, Leos Carax cemented his status as one of the boldest filmmakers of his generation. In a \" +\n" +
                        "                \"world ravaged by STBO, a sexually transmitted disease only acquired by people having sex without \" +\n" +
                        "                \"any emotion, a rebellious young man (Denis Lavant) is recruited by a veteran criminal \" +\n" +
                        "                \"(Michel Piccoli) to steal the antidote. He soon falls dangerously in love with his new associate’s \" +\n" +
                        "                \"lover (Juliette Binoche)—an infatuation that catapults him out into the street to run to the \" +\n" +
                        "                \"pounding beat of David Bowie’s “Modern Love” in one of the most exhilarating moments of eighties \" +\n" +
                        "                \"cinema.")
                .title("Mauvais sang")
                .year("1986")
                .actors(Set.of(a1, a2, a3, a4))
                .directors(Set.of(d2))
                .build();

        movieRepository.save(movie);
    }
}