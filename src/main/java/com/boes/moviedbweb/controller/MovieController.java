package com.boes.moviedbweb.controller;

import com.boes.moviedbweb.entity.Actor;
import com.boes.moviedbweb.entity.Collection;
import com.boes.moviedbweb.entity.Director;
import com.boes.moviedbweb.entity.Movie;
import com.boes.moviedbweb.repo.MovieRepository;
import com.boes.moviedbweb.service.ActorService;
import com.boes.moviedbweb.service.CollectionService;
import com.boes.moviedbweb.service.DirectorService;
import com.boes.moviedbweb.service.MovieService;
import com.boes.moviedbweb.utils.MovieHtmlHelper;
import com.boes.moviedbweb.utils.MovieUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MovieController {

    private MovieRepository movieRepository;
    private ActorService actorService;
    private DirectorService directorService;
    private CollectionService collectionService;

    private MovieUtils movieUtils = new MovieUtils();

    @Autowired
    public MovieController(MovieRepository movieRepository, ActorService actorService, DirectorService directorService, CollectionService collectionService) {
        this.movieRepository = movieRepository;
        this.actorService = actorService;
        this.directorService = directorService;
        this.collectionService = collectionService;
    }

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

    @GetMapping("/alldirectors")
    public List<Director> getAllDirectors(Model model) {
        List<Director> directors = directorService.getAll();
        model.addAttribute("searched",
                MovieHtmlHelper.getSearchedValue("All Directors", String.valueOf(directors.stream().count())));
        model.addAttribute("entities", directors);
        return directors;
    }

    @GetMapping("/allactors")
    public List<Actor> getAllActors(Model model) {
        List<Actor> actors = actorService.getAll();
        model.addAttribute("searched",
                MovieHtmlHelper.getSearchedValue("All Actors", String.valueOf(actors.stream().count())));
        model.addAttribute("entities", actors);
        return actors;
    }

    @GetMapping("/allcollections")
    public List<Collection> getAllCollections(Model model) {
        List<Collection> collections = collectionService.getAll();
        model.addAttribute("searched",
                MovieHtmlHelper.getSearchedValue("All Actors", String.valueOf(collections.stream().count())));
        model.addAttribute("entities", collections);
        return collections;
    }


    @GetMapping("/director")
    public List<Movie> getByDirectorId(@RequestParam(value = "id", required = true) long id, Model model) {
        return getMovies(id, model, movieRepository);
    }

    static List<Movie> getMovies(@RequestParam(value = "id", required = true) long id, Model model,
                                 MovieRepository movieRepository)
    {
        List<Movie> movies = movieRepository.findByDirectorId(id);
        String searchedOn = movies.get(0).getDirectors().stream().
                filter(a -> a.getDirectorId() == id)
                .map(Director::getName)
                .collect(Collectors.joining());
        model.addAttribute("searched",
                MovieHtmlHelper.getSearchedValue(searchedOn, String.valueOf(movies.stream().count())));
        model.addAttribute("movies", movies);
        model.addAttribute("filterdBy", id);
        return movies;
    }


    @GetMapping("/actor")
    public List<Movie> getByActorId(@RequestParam(value = "id", required = true) long id, Model model) {
        List<Movie> movies = movieRepository.findByActorId(id);
        String searchedOn = movies.get(0).getActors().stream().
                filter(a -> a.getActorId() == id)
                .map(Actor::getName)
                .collect(Collectors.joining());
        model.addAttribute("searched",
                MovieHtmlHelper.getSearchedValue(searchedOn, String.valueOf(movies.stream().count())));
        model.addAttribute("movies", movies);
        model.addAttribute("filterdBy", id);
        return movies;
    }

    @GetMapping("/series")
    public List<Movie> getBySeriesId(@RequestParam(value = "id", required = true) long id, Model model) {
        List<Movie> movies = movieRepository.findByCollectionId(id);
        String searchedOn = movies.get(0).getCollections().stream().
                filter(a -> a.getCollectionId() == id)
                .map(Collection::getName)
                .collect(Collectors.joining());
        model.addAttribute("searched",
                MovieHtmlHelper.getSearchedValue(searchedOn, String.valueOf(movies.stream().count())));
        model.addAttribute("movies", movies);
        model.addAttribute("filterdBy", id);
        return movies;
    }

    @GetMapping("/year")
    public List<Movie> getByYear(@RequestParam(value = "id", required = true) String id, Model model) {
        List<Movie> movies = movieRepository.findByYearOrderByTitle(id);
        model.addAttribute("searched",
                MovieHtmlHelper.getSearchedValue(id, String.valueOf(movies.stream().count())));
        model.addAttribute("movies", movies);
        model.addAttribute("filterdBy", id);
        return movies;
    }
}
