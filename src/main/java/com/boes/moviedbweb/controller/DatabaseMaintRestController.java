package com.boes.moviedbweb.controller;

import com.boes.moviedbweb.dto.DurationDirectDto;
import com.boes.moviedbweb.dto.MovieDto;
import com.boes.moviedbweb.entity.*;
import com.boes.moviedbweb.entity.Collection;
import com.boes.moviedbweb.repo.*;
import com.boes.moviedbweb.service.*;
import com.boes.moviedbweb.utils.MovieUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class DatabaseMaintRestController {


    private MovieRepository movieRepository;
    private ActorService actorService;
    private DirectorService directorService;
    private CollectionService collectionService;
    private CountryService countryService;
    private ViewDateService viewDateService;

    private MovieUtils movieUtils = new MovieUtils();

    @Autowired
    public DatabaseMaintRestController(MovieRepository movieRepository,
                                       ActorService actorService,
                                       DirectorService directorService,
                                       CountryService countryService,
                                       CollectionService collectionService,
                                       ViewDateService viewDateService)
    {
        this.movieRepository = movieRepository;
        this.actorService = actorService;
        this.directorService = directorService;
        this.countryService = countryService;
        this.collectionService = collectionService;
        this.viewDateService = viewDateService;
    }

    @PutMapping(path = "/rest/duration")
    public void updateDurationDirect(@RequestBody DurationDirectDto durationDirectDto) {
        Movie movie = getMovieByTitleAndYear(durationDirectDto.getMovieTitle(), durationDirectDto.getYear());
        movie.setDuration(MovieUtils.convertHHMMSSToInteger(durationDirectDto.getDuration()));
        movieRepository.save(movie);
    }

    @PutMapping(path = "/rest/viewdate")
    public void updateViewDate(@Valid @RequestBody MovieDto movieDto) {
        Movie movie = getMovieByTitleAndYear(movieDto.getTitle(), movieDto.getYear());
        movie.addDate(getViewDateDBInstances(movieDto.getViewDate()));
        movieRepository.save(movie);
    }

    @PutMapping(path = "/rest/addcountryonexisting")
    public void addCountryOnExistingMovie(@Valid @RequestBody MovieDto movieDto) {
        Movie movie = getMovieByTitleAndYear(movieDto.getTitle(), movieDto.getYear());
        movie.addCountries(getCountryDBInstances(movieDto.getCountries()));
        movieRepository.save(movie);
    }

    @PutMapping("/rest/addviewdate")
    public void updateViewDateWithMovieId(@RequestParam(value = "id", required = true) long id,
                                                 @RequestParam(value = "date", required = true) String date,
                                                 Model model) {
        Movie movie = getMovieById(id);
        movie.addDate(getViewDateDBInstances(date));
        movieRepository.save(movie);
    }

    @PutMapping("/rest/viewedagaintoday")
    public void updateViewDateWithMovieId(@RequestParam(value = "id", required = true) long id,
                                                 Model model) {
        Movie movie = getMovieById(id);
        movie.addDate(getViewDateDBInstances(LocalDate.now()));
        movieRepository.save(movie);
    }

    @PutMapping(path = "/rest/movie")
    public void createMovie(@Valid @RequestBody MovieDto movieDto) {
        Movie movie = getOrCreateMovieByTitleAndYear(movieDto.getTitle(), movieDto.getYear());
        if (!StringUtils.isBlank(movieDto.getDuration())) {
            movie.setDuration(MovieUtils.convertHHMMSSToInteger(movieDto.getDuration()));
        }
        if (!StringUtils.isBlank(movieDto.getDescription())) {
            movie.setDescription(movieDto.getDescription());
        }
        if (!StringUtils.isBlank(movieDto.getActors())) {
            movie.addActors(getActorDBInstances(movieDto.getActors()));
        }
        if (!StringUtils.isBlank(movieDto.getDirectors())) {
            movie.addDirectors(getDirectorDBInstances(movieDto.getDirectors()));
        }
        if (!StringUtils.isBlank(movieDto.getCollections())) {
            movie.addCollections(getCollectionDBInstances(movieDto.getCollections()));
        }
        if (!StringUtils.isBlank(movieDto.getCountries())) {
            movie.addCountries(getCountryDBInstances(movieDto.getCountries()));
        }
        if (!StringUtils.isBlank(movieDto.getViewDate())) {
            movie.addDate(getViewDateDBInstances(movieDto.getViewDate()));
        }
        movieRepository.save(movie);
    }

    private Set<Country> getCountryDBInstances(String delimitedNames) {
        String[] names = delimitedNames.split(";");
        Set<Country> newSet = new HashSet<>();
        for (String name : names) {
            newSet.add(countryService.getOrCreateCountry(name.trim()));
        }
        return newSet;
    }

    private Set<Actor> getActorDBInstances(String delimitedNames) {
        String[] actorNames = delimitedNames.split(";");
        Set<Actor> newSet = new HashSet<>();
        for (String actorName : actorNames) {
            newSet.add(actorService.getOrCreateActor(actorName.trim()));
        }
        return newSet;
    }

    private Set<ViewDate> getViewDateDBInstances(LocalDate date) {
        Set<ViewDate> newSet = new HashSet<>();
        newSet.add(viewDateService.getOrCreateViewDate(date));
        return newSet;
    }

    private Set<Director> getDirectorDBInstances(String delimitedNames) {
        String[] names = delimitedNames.split(";");
        Set<Director> newSet = new HashSet<>();
        for (String name : names) {
            newSet.add(directorService.getOrCreateDirector(name.trim()));
        }
        return newSet;
    }

    private Set<Collection> getCollectionDBInstances(String delimitedNames) {
        String[] names = delimitedNames.split(";");
        Set<Collection> newSet = new HashSet<>();
        for (String name : names) {
            newSet.add(collectionService.getOrCreateCollection(name.trim()));
        }
        return newSet;
    }

    private Set<ViewDate> getViewDateDBInstances(String delimitedDates) {
        String[] dates = delimitedDates.split(";");
        Set<ViewDate> newSet = new HashSet<>();
        for (String date : dates) {
            newSet.add(viewDateService.getOrCreateViewDate(LocalDate.parse(date.trim())));
        }
        return newSet;
    }

    public Movie getMovieByTitleAndYear(String title, String year) {
        return movieRepository.findByTitleAndYear(title, year).orElseThrow(() ->
                new NoSuchElementException("Movie does not exist."));
    }

    public Movie getMovieById(long id) {
        return movieRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Movie does not exist."));
    }

    public Movie getOrCreateMovieByTitleAndYear(String title, String year) {
        return movieRepository.findByTitleAndYear(title, year)
                .orElseGet(() -> Movie.builder().title(title).year(year).build());
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return400(NoSuchElementException ex) {
        return ex.getMessage();
    }

}
