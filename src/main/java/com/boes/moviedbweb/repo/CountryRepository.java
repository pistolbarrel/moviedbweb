package com.boes.moviedbweb.repo;

import com.boes.moviedbweb.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByName(String name);

    List<Country> findAllByOrderByName();

    @Query(nativeQuery = true,
            value = "select distinct count(movie_id) from movie_country m where m.country_id=?1")
    long getCountOfMoviesById(long id);
}
