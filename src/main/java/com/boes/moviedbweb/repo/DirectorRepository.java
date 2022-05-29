package com.boes.moviedbweb.repo;

import com.boes.moviedbweb.entity.Director;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DirectorRepository extends JpaRepository<Director, Long> {
    Optional<Director> findByName(String name);

    List<Director> findAllByOrderByName();

    @Query(nativeQuery = true,
            value = "select distinct count(movie_id) from movie_director m where m.director_id=?1")
    long getCountOfMoviesById(long id);
}
