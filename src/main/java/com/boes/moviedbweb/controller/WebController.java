package com.boes.moviedbweb.controller;

import com.boes.moviedbweb.entity.Collection;
import com.boes.moviedbweb.entity.*;
import com.boes.moviedbweb.repo.MovieRepository;
import com.boes.moviedbweb.service.ActorService;
import com.boes.moviedbweb.service.CollectionService;
import com.boes.moviedbweb.service.CountryService;
import com.boes.moviedbweb.service.DirectorService;
import com.boes.moviedbweb.utils.MovieHtmlHelper;
import com.boes.moviedbweb.utils.MovieUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class WebController {

    private final MovieRepository movieRepository;
    private final ActorService actorService;
    private final DirectorService directorService;
    private final CollectionService collectionService;
    private final CountryService countryService;

    @Autowired
    public WebController(MovieRepository movieRepository, ActorService actorService,
                         DirectorService directorService, CollectionService collectionService,
                         CountryService countryService) {
        this.movieRepository = movieRepository;
        this.actorService = actorService;
        this.directorService = directorService;
        this.collectionService = collectionService;
        this.countryService = countryService;
    }

    @Operation(summary = "Returns all of the movies in the database.")
    @GetMapping("/movies")
    public List<Movie> getAllMovies(Model model) {
        List<Movie> movies = movieRepository.findAllByOrderByTitleAsc();
        long seen = numberSeen(movies);
        model.addAttribute("searched",
                MovieHtmlHelper.getSearchedValue("All Movies",
                        createSeenUnseenString(movies.size(), seen)));
        model.addAttribute("movies", movies);
        return movies;
    }

    @GetMapping("/unseenmovies")
    public List<Movie> getUnseenMovies(Model model) {
        List<Movie> movies = movieRepository.searchAllByDateViewedIsNull();
        model.addAttribute("searched",
                MovieHtmlHelper.getSearchedValue("All Unseen Movies", String.valueOf((long) movies.size())));
        model.addAttribute("movies", movies);
        return movies;
    }

    @Operation(summary = "Returns all of the directors in the database.")
    @GetMapping("/alldirectors")
    public List<Director> getAllDirectors(Model model) {
        List<Director> directors = directorService.getAll();
        // This loop sets the count of movies. If the count is zero,
        // log it for possible deletion.
        for (Director director : directors) {
            director.setCount(directorService.getMovieCountById(director.getDirectorId()));
            if (director.getCount() == 0) {
                log.warn("No movies found with Director: " + director);
            }
        }
        directors.removeIf(d -> d.getCount() == 0);
        model.addAttribute("searched",
                MovieHtmlHelper.getSearchedValue("All Directors", String.valueOf((long) directors.size())));
        model.addAttribute("entities", directors);
        return directors;
    }

    @Operation(summary = "Returns all of the actors in the database.")
    @GetMapping("/allactors")
    public List<Actor> getAllActors(Model model) {
        List<Actor> actors = actorService.getAll();
        // This loop sets the count of movies. If the count is zero,
        // log it for possible deletion.
        for (Actor actor : actors) {
            actor.setCount(actorService.getMovieCountById(actor.getActorId()));
            if (actor.getCount() == 0) {
                log.warn("No movies found with Actor: " + actor);
            }
        }
        actors.removeIf(a -> a.getCount() == 0);
        model.addAttribute("searched",
                MovieHtmlHelper.getSearchedValue("All Actors", String.valueOf((long) actors.size())));
        model.addAttribute("entities", actors);
        return actors;
    }

    @Operation(summary = "Returns all of the collections in the database.")
    @GetMapping("/allcollections")
    public List<Collection> getAllCollections(Model model) {
        List<Collection> collections = collectionService.getAll();
        // This loop sets the count of movies. If the count is zero,
        // log it for possible deletion.
        for (Collection collection : collections) {
            collection.setCount(collectionService.getMovieCountById(collection.getCollectionId()));
            if (collection.getCount() == 0) {
                log.warn("No movies found with Collection: " + collection);
            }
        }
        collections.removeIf(c -> c.getCount() == 0);
        model.addAttribute("searched",
                MovieHtmlHelper.getSearchedValue("All Collections", String.valueOf((long) collections.size())));
        model.addAttribute("entities", collections);
        return collections;
    }

    @Operation(summary = "Returns all of the countries in the database.")
    @GetMapping("/allcountries")
    public List<Country> getAllCountries(Model model) {
        List<Country> countries = countryService.getAll();
        // This loop sets the count of movies. If the count is zero,
        // log it for possible deletion.
        for (Country country : countries) {
            country.setCount(countryService.getMovieCountById(country.getCountryId()));
            if (country.getCount() == 0) {
                log.warn("No movies found with Country: " + country);
            }
        }
        countries.removeIf(c -> c.getCount() == 0);
        model.addAttribute("searched",
                MovieHtmlHelper.getSearchedValue("All Countries", String.valueOf((long) countries.size())));
        model.addAttribute("entities", countries);
        return countries;
    }

    @Operation(summary = "Returns the movies viewed in the last 30 days.")
    @GetMapping("/viewedinlast30days")
    public List<Movie> getViewedInLast30Days(Model model) {
        List<Movie> movies = movieRepository.findViewedInLast30Days();
        for (Movie movie : movies) {
            movie.setLastViewedDate(MovieUtils.getLastViewedDate(movie.getDateViewed()));
        }
        movies = movies.stream().distinct().collect(Collectors.toList());
        movies.sort(Comparator.comparing(Movie::getLastViewedDate));
        Collections.reverse(movies);
        model.addAttribute("searched",
                MovieHtmlHelper.getSearchedValue("Movies Viewed in last 30 Days",
                        String.valueOf((long) movies.size())));
        model.addAttribute("movies", movies);
        return movies;

    }


    @Operation(summary = "Returns the movies by id.")
    @GetMapping("/movie")
    public List<Movie> getMovieById(@Parameter(description = "id of the movie to be searched")
                                    @RequestParam(value = "id") long id) {
        return List.of(movieRepository.getById(id));
    }

    @Operation(summary = "Returns the movies by the given director.")
    @GetMapping("/director")
    public List<Movie> getByDirectorId(@Parameter(description = "id of the director to be searched")
                                       @RequestParam(value = "id") long id, Model model) {
        List<Movie> movies = movieRepository.findByDirectorId(id);
        String searchedOn = movies.get(0).getDirectors().stream().
                filter(a -> a.getDirectorId() == id)
                .map(Director::getName)
                .collect(Collectors.joining());
        updateModel(String.valueOf(id), model, movies, searchedOn);
        return movies;
    }


    @Operation(summary = "Returns the movies by the given actor.")
    @GetMapping("/actor")
    public List<Movie> getByActorId(@Parameter(description = "id of the actor to be searched")
                                    @RequestParam(value = "id") long id, Model model) {
        List<Movie> movies = movieRepository.findByActorId(id);
        String searchedOn = movies.get(0).getActors().stream().
                filter(a -> a.getActorId() == id)
                .map(Actor::getName)
                .collect(Collectors.joining());
        updateModel(String.valueOf(id), model, movies, searchedOn);
        return movies;
    }

    @Operation(summary = "Returns the movies in a given collection.")
    @GetMapping("/series") // aka Collections
    public List<Movie> getBySeriesId(@Parameter(description = "id of the collection to be searched")
                                     @RequestParam(value = "id") long id, Model model) {
        List<Movie> movies = movieRepository.findByCollectionId(id);
        String searchedOn = movies.get(0).getCollections().stream().
                filter(a -> a.getCollectionId() == id)
                .map(Collection::getName)
                .collect(Collectors.joining());
        updateModel(String.valueOf(id), model, movies, searchedOn);
        return movies;
    }

    @Operation(summary = "Returns the movies released in a specified year.")
    @GetMapping("/year")
    public List<Movie> getByYear(@Parameter(description = "the four digit year to be searched")
                                 @RequestParam(value = "id") String id, Model model) {
        List<Movie> movies = movieRepository.findByYearOrderByTitle(id);
        updateModel(id, model, movies, id);
        return movies;
    }

    @Operation(summary = "Returns the movies from a given country.")
    @GetMapping("/country")
    public List<Movie> getByCountryId(@Parameter(description = "id of the collection to be searched")
                                      @RequestParam(value = "id") String id, Model model) {
        long localId = Long.parseLong(id);
        List<Movie> movies = movieRepository.findByCountryIdOOrderByTitle(localId);
        String searchedOn = movies.get(0).getCountries().stream().
                filter(a -> a.getCountryId() == localId)
                .map(Country::getName)
                .collect(Collectors.joining());
        updateModel(id, model, movies, searchedOn);
        return movies;
    }

    private void updateModel(String id, Model model, List<Movie> movies, String searchedOn) {
        long seen = numberSeen(movies);
        String seenUnseen = createSeenUnseenString(movies.size(), seen);
        model.addAttribute("searched",
                MovieHtmlHelper.getSearchedValue(searchedOn, seenUnseen));
        model.addAttribute("movies", movies);
        model.addAttribute("filterdBy", id);
    }

    private long numberSeen(List<Movie> movies) {
        return movies.stream()
                .filter(m -> (long) m.getDateViewed().size() != 0)
                .count();
    }

    private String createSeenUnseenString(long total, long seen) {
        String ret;

        if (total == seen)
            ret = String.valueOf(total);
        else if (seen == 0)
            ret = "0," + total;
        else
            ret = seen + "/" + total + " - " + (total - seen);

        return ret;
    }
}
