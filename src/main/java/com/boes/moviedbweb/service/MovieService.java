package com.boes.moviedbweb.service;

import com.boes.moviedbweb.entity.*;
import com.boes.moviedbweb.repo.CountryRepository;
import com.boes.moviedbweb.repo.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
public class MovieService {

    private MovieRepository movieRepository;
    private ActorService actorService;
    private DirectorService directorService;
    private CollectionService collectionService;
    private CountryRepository countryRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository,
                        ActorService actorService,
                        DirectorService directorService,
                        CollectionService collectionService,
                        CountryRepository countryRepository) {

        this.movieRepository = movieRepository;
        this.actorService = actorService;
        this.directorService = directorService;
        this.collectionService = collectionService;
        this.countryRepository = countryRepository;
    }

    public Movie getMovie(String title, String year) {
        return movieRepository.findByTitleAndYear(title, year)
                .orElseGet(() -> movieRepository.save(
                        Movie.builder().title(title)
                                .year(year)
                                .build()));
    }

    public Movie addDescription(String title, String year, String description) {
        Movie movie =  movieRepository.findByTitleAndYear(title, year)
                .orElseGet(() -> movieRepository.save(
                        Movie.builder().title(title)
                                .year(year)
                                .build()));
        movie.setDescription(description);
        movieRepository.save(movie);
        return movie;
    }

    public Movie addActors(String title, String year, String semicolonDelimitedActorNames) {
        String[] actorNames = semicolonDelimitedActorNames.split(";");
        Set<Actor> newSet = new HashSet<>();
        for (String actorName : actorNames) {
            newSet.add(actorService.getOrCreateActor(actorName.trim()));
        }
        Movie movie = getMovie(title, year);
        movie.addActors(newSet);
        movieRepository.save(movie);
        return movie;
    }

    public Movie addDirectors(String title, String year, String semicolonDelimitedDirectorNames) {
        String[] names = semicolonDelimitedDirectorNames.split(";");
        Set<Director> newSet = new HashSet<>();
        for (String name : names) {
            newSet.add(directorService.getOrCreateDirector(name.trim()));
        }
        Movie movie = getMovie(title, year);
        movie.addDirectors(newSet);
        movieRepository.save(movie);
        return movie;
    }

    public Movie addCollections(String title, String year, String semicolonDelimitedCollectionNames) {
        String[] names = semicolonDelimitedCollectionNames.split(";");
        Set<Collection> newSet = new HashSet<>();
        for (String name : names) {
            newSet.add(collectionService.getOrCreateCollection(name.trim()));
        }
        Movie movie = getMovie(title, year);
        movie.addCollections(newSet);
        movieRepository.save(movie);
        return movie;
    }

    public Movie addCountries(String title, String year, String semicolonDelimitedNames) {
        String[] names = semicolonDelimitedNames.split(";");
        Set<Country> newSet = new HashSet<>();
        for (String name : names) {
            newSet.add(getCountry(name.trim()));
        }
        Movie movie = getMovie(title, year);
        movie.addCountries(newSet);
        movieRepository.save(movie);
        return movie;
    }

    public Country getCountry(String name) {
        return countryRepository.findByName(name)
                .orElseGet(() -> countryRepository.save(
                        Country.builder().name(name)
                                .build()));

    }

    public void update(Movie movie) {
        movieRepository.save(movie);
    }

    public Iterable<Movie> lookup() {
        return movieRepository.findAll();
    }

    public List<Movie> getAllMovies(Model model) {
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
