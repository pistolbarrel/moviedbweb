package com.boes.moviedbweb.controller;

import com.boes.moviedbweb.dto.DurationDirectDto;
import com.boes.moviedbweb.dto.MovieCollectionDto;
import com.boes.moviedbweb.dto.MovieDto;
import com.boes.moviedbweb.dto.MovieInfoDto;
import com.boes.moviedbweb.entity.*;
import com.boes.moviedbweb.repo.MovieRepository;
import com.boes.moviedbweb.service.*;
import com.boes.moviedbweb.utils.MovieUtils;
import com.boes.moviedbweb.utils.Title;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
public class MovieController {

    private final MovieRepository movieRepository;
    private final ActorService actorService;
    private final DirectorService directorService;
    private final CollectionService collectionService;
    private final CountryService countryService;
    private final ViewDateService viewDateService;
    private final MovieService movieService;

    @Autowired
    public MovieController(MovieRepository movieRepository,
                           ActorService actorService,
                           DirectorService directorService,
                           CountryService countryService,
                           CollectionService collectionService,
                           ViewDateService viewDateService,
                           MovieService movieService) {
        this.movieRepository = movieRepository;
        this.actorService = actorService;
        this.directorService = directorService;
        this.countryService = countryService;
        this.collectionService = collectionService;
        this.viewDateService = viewDateService;
        this.movieService = movieService;
    }

    @PutMapping(path = "/rest/duration")
    public void updateDurationDirect(@RequestBody DurationDirectDto durationDirectDto) {
        Movie movie = getMovieByTitleAndYear(durationDirectDto.getMovieTitle(), durationDirectDto.getYear());
        movie.setDuration(MovieUtils.convertHHMMSSToInteger(durationDirectDto.getDuration()));
        movieRepository.save(movie);
    }

    @PutMapping(path = "/rest/addcountryonexisting")
    public void addCountryOnExistingMovie(@Valid @RequestBody MovieDto movieDto) {
        Movie movie = getMovieByTitleAndYear(movieDto.getTitle(), movieDto.getYear());
        movie.addCountries(getCountryDBInstances(movieDto.getCountries()));
        log.info("Added the countries " + movieDto.getCountries().trim() + " to "
                + movie.getDisplayName());
        movieRepository.save(movie);
    }

    @PutMapping("/rest/viewedtoday")
    public void addViewDateOfToday(@RequestBody MovieCollectionDto movieCollectionDto) {
        Title title = new Title(movieCollectionDto.getTitles().trim());
        if (movieRepository.existsByTitleAndYear(title.getName(), title.getYear())) {
            Movie movie = getMovieByTitleAndYear(title.getName(), title.getYear());
            movie.addDate(getViewDateDBInstances(LocalDate.now()));
            log.info("Added viewDate of today to " + movie.getDisplayName());
            movieRepository.save(movie);
        } else {
            log.info(movieCollectionDto.getTitles().trim() + " was not found!");
        }
    }

    @PutMapping("/rest/createamoviefromtext")
    public void createAMovieFromText(@RequestBody MovieInfoDto movieInfoDto) {
        createOrUpdateMovieImpl(MovieInfoDto.convertToMovieDto(movieInfoDto));
    }

    @PutMapping("/rest/updatecollectionsonexisting")
    public void updatecollectiononexisting(@RequestBody MovieCollectionDto movieCollectionDto) {
        // going to look up each movie, if it exists, add the collection to it,
        // else skip it.  Might be nice to return which movies were found.
        String[] titlesWithYears = movieCollectionDto.getTitles().split(";");
        StringBuilder sb = new StringBuilder();
        for (String titleString : titlesWithYears) {
            Title title = new Title(titleString.trim());
            if (!movieRepository.existsByTitleAndYear(title.getName(), title.getYear())) {
                continue;
            }
            sb.append(title.getDisplayName()).append(";");
            Movie movie = getMovieByTitleAndYear(title.getName(), title.getYear());
            movie.addCollections(getCollectionDBInstances(movieCollectionDto.getCollection().trim()));
            movieRepository.save(movie);
        }
        if (sb.toString().isEmpty()) {
            sb.append("None");
        }
        log.info("Added series " + movieCollectionDto.getCollection().trim() + " to " + sb);
    }

    @PutMapping("/rest/moviestowatch")
    public void getmoviestowatch(@RequestBody MovieCollectionDto movieCollectionDto) {
        // going to look up each movie, if it exists and never viewed, report it.
        String[] titlesWithYears = movieCollectionDto.getTitles().split(";");
        StringBuilder sb = new StringBuilder();
        for (String titleString : titlesWithYears) {
            Title title = new Title(titleString.trim());
            if (!movieRepository.existsByTitleAndYear(title.getName(), title.getYear())) {
                sb.append("Not Watchlisted:").append(title.getDisplayName()).append("\n");
                continue;
            }
            Movie movie = getMovieByTitleAndYear(title.getName(), title.getYear());
            if (movie.getDateViewed().isEmpty()) {
                sb.append(movie.getCollections().stream()
                        .map(Collection::getName)
                        .collect(Collectors.joining(";")));

                sb.append(":").append(title.getDisplayName()).append("\n");
            }
        }
        log.info("Movies to watch this month:\n" + sb);
    }

    @PutMapping(path = "/rest/movie")
    public void createMovie(@Valid @RequestBody MovieDto movieDto) {
        createOrUpdateMovieImpl(movieDto);
    }

    private void createOrUpdateMovieImpl(MovieDto movieDto) {
        log.info("Creating of Modifying movie with movieDto =" + movieDto);
        Movie movie = getOrCreateMovieByTitleAndYear(movieDto.getTitle(), movieDto.getYear());
        // This will remove all linked data to a movie. For a newly created
        // db entity, this is a noop.
        if (movieDto.isAbsolute()) {
            Movie.removeAllJoinedDataExceptDates(movie);
        }
        // Only (over)write duration and description if it has no value or absolute is true
        if (!StringUtils.isBlank(movieDto.getDuration())
                && (movie.getDuration() == null || (movieDto.isAbsolute()))) {
            movie.setDuration(MovieUtils.convertHHMMSSToInteger(movieDto.getDuration()));
        }
        if (!StringUtils.isBlank(movieDto.getDescription())
                && (movie.getDescription() == null || (movieDto.isAbsolute()))) {
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

    public Movie getOrCreateMovieByTitleAndYear(String title, String year) {
        return movieRepository.findByTitleAndYear(title, year)
                .orElseGet(() -> Movie.builder().title(title).year(year).build());
    }

    @Operation(summary = "Single Movie")
    @GetMapping("/rest/movie/{movie_id}")
    public Movie getMovieById(@PathVariable(value = "movie_id") long id) {
        return movieService.getMovie(id);
    }

    @Operation(summary = "Delete an existing Movie")
    @DeleteMapping("/rest/movie/{movie_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovieById(@PathVariable(value = "movie_id") long id) {
        String title = movieService.deleteMovie(id);
        log.info("Deleting movie = " + title);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return400(NoSuchElementException ex) {
        return ex.getMessage();
    }
}
