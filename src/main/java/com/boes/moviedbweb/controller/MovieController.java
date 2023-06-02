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

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addViewDateOfToday(@RequestBody MovieCollectionDto movieCollectionDto) {
        Title title = new Title(movieCollectionDto.getTitles().trim());
        if (movieRepository.existsByTitleAndYear(title.getName(), title.getYear())) {
            Movie movie = getMovieByTitleAndYear(title.getName(), title.getYear());
            movie.addDate(getViewDateDBInstances(LocalDate.now()));
            log.info("Added viewDate of today to " + movie.getDisplayName());
            movieRepository.save(movie);
        } else {
            log.info(movieCollectionDto.getTitles().trim() + " was not found!");
            throw new NoSuchElementException("Movie was not found");
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

    @PutMapping(path = "/rest/movies")
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

    @Operation(summary = "All Movies sorted by title")
    @GetMapping("/rest/movies")
    public List<Movie> getMovies() {
        return movieService.getAllMovies();
    }

    @Operation(summary = "All Movies with this actor")
    @GetMapping("/rest/movies/actors/{actor_id}")
    public List<Movie> getMoviesByActor(@PathVariable(value = "actor_id") long id) {
        return movieRepository.findByActorId(id);
    }

    @Operation(summary = "All Movies directed by this director")
    @GetMapping("/rest/movies/directors/{director_id}")
    public List<Movie> getMoviesByDirector(@PathVariable(value = "director_id") long id) {
        return movieRepository.findByDirectorId(id);
    }

    @Operation(summary = "All Movies from thiw country")
    @GetMapping("/rest/movies/countries/{country_id}")
    public List<Movie> getMoviesByCountry(@PathVariable(value = "country_id") long id) {
        return movieRepository.findByCountryIdOOrderByTitle(id);
    }

    @Operation(summary = "All Movies in this collection")
    @GetMapping("/rest/movies/collections/{collection_id}")
    public List<Movie> getMoviesByCollection(@PathVariable(value = "collection_id") long id) {
        return movieRepository.findByCollectionId(id);
    }

    @Operation(summary = "Single Movie")
    @GetMapping("/rest/movies/{movie_id}")
    public Movie getMovieById(@PathVariable(value = "movie_id") long id) {
        return movieService.getMovie(id);
    }

    @Operation(summary = "Delete an existing Movie")
    @DeleteMapping("/rest/movies/{movie_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovieById(@PathVariable(value = "movie_id") long id) {
        String title = movieService.deleteMovie(id);
        log.info("Deleting movie = " + title);
    }

    @Operation(summary = "Actors for an existing movie")
    @GetMapping("/rest/movies/{movie_id}/actors/")
    public Set<Actor> getActorsForMovie(@PathVariable(value = "movie_id") long id) {
        Movie movie = movieService.getMovie(id);
        return movie.getActors();
    }

    @Operation(summary = "Modify the actors for an existing movie")
    @PatchMapping("/rest/movies/{movie_id}/actors/")
    public void modifyActorsForMovie(@PathVariable(value = "movie_id") long id,
                                     @RequestBody Set<Actor> actors) {
        movieService.modifyActorsForMovie(id, actors);
    }

    @Operation(summary = "Collections for an existing movie")
    @GetMapping("/rest/movies/{movie_id}/collections/")
    public Set<Collection> getCollectionsForMovie(@PathVariable(value = "movie_id") long id) {
        Movie movie = movieService.getMovie(id);
        return movie.getCollections();
    }

    @Operation(summary = "Modify the collections for an existing movie")
    @PatchMapping("/rest/movies/{movie_id}/collections/")
    public void modifyCollectionsForMovie(@PathVariable(value = "movie_id") long id,
                                          @RequestBody Set<Collection> collections) {
        movieService.modifyCollectionsForMovie(id, collections);
    }

    @Operation(summary = "Countries for an existing movie")
    @GetMapping("/rest/movies/{movie_id}/countries/")
    public Set<Country> getCountriessForMovie(@PathVariable(value = "movie_id") long id) {
        Movie movie = movieService.getMovie(id);
        return movie.getCountries();
    }

    @Operation(summary = "Modify the countries for an existing movie")
    @PatchMapping("/rest/movies/{movie_id}/countries/")
    public void modifyCountriesForMovie(@PathVariable(value = "movie_id") long id,
                                        @RequestBody Set<Country> countries) {
        movieService.modifyCountriesForMovie(id, countries);
    }

    @Operation(summary = "Directors for an existing movie")
    @GetMapping("/rest/movies/{movie_id}/directors/")
    public Set<Director> getDirectorsForMovie(@PathVariable(value = "movie_id") long id) {
        Movie movie = movieService.getMovie(id);
        return movie.getDirectors();
    }

    @Operation(summary = "Modify the directors for an existing movie")
    @PatchMapping("/rest/movies/{movie_id}/directors/")
    public void modifyDirectorsForMovie(@PathVariable(value = "movie_id") long id,
                                        @RequestBody Set<Director> directors) {
        movieService.modifyDirectorsForMovie(id, directors);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return400(NoSuchElementException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(EntityNotFoundException.class)
    public String return400(EntityNotFoundException ex) {
        return ex.getMessage();
    }
}
