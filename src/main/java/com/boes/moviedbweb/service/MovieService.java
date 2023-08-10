package com.boes.moviedbweb.service;

import com.boes.moviedbweb.entity.*;
import com.boes.moviedbweb.repo.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ActorService actorService;

    @Autowired
    public MovieService(MovieRepository movieRepository,
                        ActorService actorService) {

        this.movieRepository = movieRepository;
        this.actorService = actorService;
    }

    public Movie getMovie(String title, String year) {
        return movieRepository.findByTitleAndYear(title, year)
                .orElseGet(() -> movieRepository.save(
                        Movie.builder().title(title)
                                .year(year)
                                .build()));
    }

    public void addActors(String title, String year, String semicolonDelimitedActorNames) {
        String[] actorNames = semicolonDelimitedActorNames.split(";");
        Set<Actor> newSet = new HashSet<>();
        for (String actorName : actorNames) {
            newSet.add(actorService.getOrCreateActor(actorName.trim()));
        }
        Movie movie = getMovie(title, year);
        movie.addActors(newSet);
        movieRepository.save(movie);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Movie getMovie(Long id) {
        return movieRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Movie does not exist."));
    }

    public String deleteMovie(Long id) {
        Movie movie = getMovie(id);
        String title = movie.getDisplayName();
        Movie.removeAllJoinedDataExceptDates(movie);
        // for a delete, even the dates must go.
        if (movie.getDateViewed() != null) {
            movie.getDateViewed().clear();
        }
        movieRepository.delete(movie);
        return title;
    }

    public void modifyActorsForMovie(long id, Set<Actor> actors) {
        Movie movie = getMovie(id);
        movie.setActors(actors);
        movieRepository.save(movie);
    }

    public void modifyDirectorsForMovie(long id, Set<Director> directors) {
        Movie movie = getMovie(id);
        movie.setDirectors(directors);
        movieRepository.save(movie);
    }

    public void modifyCountriesForMovie(long id, Set<Country> countries) {
        Movie movie = getMovie(id);
        movie.setCountries(countries);
        movieRepository.save(movie);
    }

    public void modifyCollectionsForMovie(long id, Set<Collection> collections) {
        Movie movie = getMovie(id);
        movie.setCollections(collections);
        movieRepository.save(movie);
    }
}
