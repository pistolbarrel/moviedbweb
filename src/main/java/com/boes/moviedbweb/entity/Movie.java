package com.boes.moviedbweb.entity;

import com.boes.moviedbweb.utils.MovieHtmlHelper;
import com.boes.moviedbweb.utils.MovieUtils;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Movie {

    @Id
    @SequenceGenerator(
            name = "sequence_movie",
            sequenceName = "s_movie",
            allocationSize = 1
    )
    @GeneratedValue(generator = "sequence_movie")
    private Long movieId;

    private String title;
    private String year;
    private Integer duration;

    @ManyToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "MovieViewdate",
            joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "movieId"),
            inverseJoinColumns = @JoinColumn(name = "date_id", referencedColumnName = "dateId")
    )
    private Set<ViewDate> dateViewed;

    @Column(length = 4096)
    private String description;


    @ManyToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "MovieActor",
            joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "movieId"),
            inverseJoinColumns = @JoinColumn(name = "actor_id", referencedColumnName = "actorId")
    )
    private Set<Actor> actors;

    @ManyToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "MovieDirector",
            joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "movieId"),
            inverseJoinColumns = @JoinColumn(name = "director_id", referencedColumnName = "directorId")
    )
    private Set<Director> directors;


    @ManyToMany()
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "MovieCollection",
            joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "movieId"),
            inverseJoinColumns = @JoinColumn(name = "collection_id", referencedColumnName = "collectionId")
    )
    private Set<Collection> collections;


    @ManyToMany(cascade = CascadeType.REMOVE)
    @LazyCollection(LazyCollectionOption.FALSE)
    @JoinTable(name = "MovieCountry",
            joinColumns = @JoinColumn(name = "movie_id", referencedColumnName = "movieId"),
            inverseJoinColumns = @JoinColumn(name = "country_id", referencedColumnName = "countryId")
    )
    private Set<Country> countries;

    @Transient
    private String lastViewedDate;


    public void addActors(Set<Actor> addActors) {
        if (actors == null) actors = new HashSet<>();
        actors.addAll(addActors);
    }

    public void addDirectors(Set<Director> addDirectors) {
        if (directors == null) directors = new HashSet<>();
        directors.addAll(addDirectors);
    }

    public void addCollections(Set<Collection> addCollection) {
        if (collections == null) collections = new HashSet<>();
        collections.addAll(addCollection);
    }

    public void addCountries(Set<Country> addCountries) {
        if (countries == null) countries = new HashSet<>();
        countries.addAll(addCountries);
    }

    public void addDate(Set<ViewDate> viewDates) {
        if (dateViewed == null) dateViewed = new HashSet<>();
        dateViewed.addAll(viewDates);
    }

    static public void removeAllJoinedDataExceptDates(Movie movie) {
        if (movie.getCollections() != null) {
            movie.getCollections().clear();
        }
        if (movie.getCountries() != null) {
            movie.getCountries().clear();
        }
        if (movie.getActors() != null) {
            movie.getActors().clear();
        }
        if (movie.getDirectors() != null) {
            movie.getDirectors().clear();
        }
    }

    public String getDisplayName() {
        return new String(title + " (" + year + ")");
    }

    public String getTitleBoxString() {
        return MovieHtmlHelper.getTitleBoxString(title, year, String.valueOf(movieId), duration,
                collections, dateViewed, countries);
    }

    public String getDirectorLinkString() {
        return MovieHtmlHelper.getDirectorsLinkString(directors);
    }

    public String getActorLinkString() {
        return MovieHtmlHelper.getActorLinkString(actors);
    }

    public String getYearLinkString() {
        return MovieHtmlHelper.getYearLinkString(year);
    }

    public String getDisplayDuration() {
        return MovieUtils.getDisplayDuration(duration);
    }

    public String isSeen() {
        return dateViewed != null && !dateViewed.isEmpty() ? "seen" : "unseen";
    }
}

