package com.boes.moviedbweb.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MovieController {

    private MovieRepository movieRepository;
    private ActorService actorService;
    private DirectorService directorService;
    private CollectionService collectionService;
    private CountryService countryService;

    private MovieUtils movieUtils = new MovieUtils();

    @Autowired
    public MovieController(MovieRepository movieRepository, ActorService actorService,
                           DirectorService directorService, CollectionService collectionService,
                           CountryService countryService) {
        this.movieRepository = movieRepository;
        this.actorService = actorService;
        this.directorService = directorService;
        this.collectionService = collectionService;
        this.countryService = countryService;
    }

    @Operation(summary = "Returns a list of all of the movies in the database.")
    @GetMapping("/movies")
    public List<Movie> getAllMovies(Model model) {
        List<Movie> movies = movieRepository.findAllByOrderByTitleAsc();
        model.addAttribute("searched",
                MovieHtmlHelper.getSearchedValue("All Movies", String.valueOf(movies.stream().count())));
        model.addAttribute("movies", movies);
        return movies;
    }

    @GetMapping("/unseenmovies")
    public List<Movie> getUnseenMovies(Model model) {
        List<Movie> movies = movieRepository.searchAllByDateViewedIsNull();
        model.addAttribute("searched", "All Unseen Movies (" + movies.stream().count() + ")");
        model.addAttribute("movies", movies);
        return movies;
    }

    @Operation(summary = "Returns a list of all the directors that exist in the database.")
    @GetMapping("/alldirectors")
    public List<Director> getAllDirectors(Model model) {
        List<Director> directors = directorService.getAll();
        // This loop sets the count of movies. If the count is zero,
        // it removes the entry from the list. This is covering up
        // that I'm not keeping my database 'tidy'. SO, tidy up as we go.
        Iterator<Director> itr = directors.iterator();
        while (itr.hasNext()) {
            Director director = itr.next();
            director.setCount(directorService.getMovieCountById(director.getDirectorId()));
            if (director.getCount() == 0) {
                directorService.deleteDirector(director);
                itr.remove();
            }
        }
        model.addAttribute("searched",
                MovieHtmlHelper.getSearchedValue("All Directors", String.valueOf(directors.stream().count())));
        model.addAttribute("entities", directors);
        return directors;
    }

    @Operation(summary = "Returns a list of all the actors that exist in the database.")
    @GetMapping("/allactors")
    public List<Actor> getAllActors(Model model) {
        List<Actor> actors = actorService.getAll();
        // This loop sets the count of movies. If the count is zero,
        // it removes the entry from the list. This is covering up
        // that I'm not keeping my database 'tidy'.
        Iterator<Actor> itr = actors.iterator();
        while (itr.hasNext()) {
            Actor actor = itr.next();
            actor.setCount(actorService.getMovieCountById(actor.getActorId()));
            if (actor.getCount() == 0) {
                actorService.deleteActor(actor);
                itr.remove();
            }
        }
        model.addAttribute("searched",
                MovieHtmlHelper.getSearchedValue("All Actors", String.valueOf(actors.stream().count())));
        model.addAttribute("entities", actors);
        return actors;
    }

    @Operation(summary = "Returns a list of all the collections that exist in the database.")
    @GetMapping("/allcollections")
    public List<Collection> getAllCollections(Model model) {
        List<Collection> collections = collectionService.getAll();
        // This loop sets the count of movies. If the count is zero,
        // it removes the entry from the list. This is covering up
        // that I'm not keeping my database 'tidy'.
        Iterator<Collection> itr = collections.iterator();
        while (itr.hasNext()) {
            Collection collection = itr.next();
            collection.setCount(collectionService.getMovieCountById(collection.getCollectionId()));
            if (collection.getCount() == 0) {
                collectionService.deleteCollection(collection);
                itr.remove();
            }
        }
        model.addAttribute("searched",
                MovieHtmlHelper.getSearchedValue("All Collections", String.valueOf(collections.stream().count())));
        model.addAttribute("entities", collections);
        return collections;
    }

    @Operation(summary = "Returns a list of all the countries that exist in the database.")
    @GetMapping("/allcountries")
    public List<Country> getAllCountries(Model model) {
        List<Country> countries = countryService.getAll();
        // This loop sets the count of movies. If the count is zero,
        // it removes the entry from the list. This is covering up
        // that I'm not keeping my database 'tidy'.
        Iterator<Country> itr = countries.iterator();
        while (itr.hasNext()) {
            Country country = itr.next();
            country.setCount(countryService.getMovieCountById(country.getCountryId()));
            if (country.getCount() == 0) {
                countryService.deleteCountry(country);
                itr.remove();
            }
        }
        model.addAttribute("searched",
                MovieHtmlHelper.getSearchedValue("All Countries", String.valueOf(countries.stream().count())));
        model.addAttribute("entities", countries);
        return countries;
    }


    @Operation(summary = "Returns a list of all the movies by the given director.")
    @GetMapping("/director")
    public List<Movie> getByDirectorId(@Parameter(description = "id of the director to be searched")
                                       @RequestParam(value = "id", required = true) long id, Model model) {
        return getMovies(id, model, movieRepository);
    }

    public List<Movie> getMovies(@RequestParam(value = "id", required = true) long id, Model model,
                                 MovieRepository movieRepository)
    {
        List<Movie> movies = movieRepository.findByDirectorId(id);
        String searchedOn = movies.get(0).getDirectors().stream().
                filter(a -> a.getDirectorId() == id)
                .map(Director::getName)
                .collect(Collectors.joining());
        updateModel(String.valueOf(id), model, movies, searchedOn);
        return movies;
    }


    @Operation(summary = "Returns a list of all the movies by the given actor.")
    @GetMapping("/actor")
    public List<Movie> getByActorId(@Parameter(description = "id of the actor to be searched")
                                    @RequestParam(value = "id", required = true) long id, Model model) {
        List<Movie> movies = movieRepository.findByActorId(id);
        String searchedOn = movies.get(0).getActors().stream().
                filter(a -> a.getActorId() == id)
                .map(Actor::getName)
                .collect(Collectors.joining());
        updateModel(String.valueOf(id), model, movies, searchedOn);
        return movies;
    }

    @Operation(summary = "Returns a list of all the movies in a given collection.")
    @GetMapping("/series") // aka Collections
    public List<Movie> getBySeriesId(@Parameter(description = "id of the collection to be searched")
                                     @RequestParam(value = "id", required = true) long id, Model model) {
        List<Movie> movies = movieRepository.findByCollectionId(id);
        String searchedOn = movies.get(0).getCollections().stream().
                filter(a -> a.getCollectionId() == id)
                .map(Collection::getName)
                .collect(Collectors.joining());
        updateModel(String.valueOf(id), model, movies, searchedOn);
        return movies;
    }

    @Operation(summary = "Returns a list of all the movies released in a specified year.")
    @GetMapping("/year")
    public List<Movie> getByYear(@Parameter(description = "the four digit year to be searched")
                                 @RequestParam(value = "id", required = true) String id, Model model) {
        List<Movie> movies = movieRepository.findByYearOrderByTitle(id);
        updateModel(id, model, movies, id);
        return movies;
    }

    @Operation(summary = "Returns a list of all the movies from a given country.")
    @GetMapping("/country")
    public List<Movie> getByCountryId(@Parameter(description = "id of the collection to be searched")
                                      @RequestParam(value = "id", required = true) String id, Model model) {
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
        model.addAttribute("searched",
                MovieHtmlHelper.getSearchedValue(searchedOn, String.valueOf(movies.stream().count())));
        model.addAttribute("movies", movies);
        model.addAttribute("filterdBy", id);
    }

}
