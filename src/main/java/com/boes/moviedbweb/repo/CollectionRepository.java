package com.boes.moviedbweb.repo;

import com.boes.moviedbweb.entity.Collection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CollectionRepository extends JpaRepository<Collection, Long> {
    Optional<Collection> findByName(String name);

    List<Collection> findAllByOrderByName();

    @Query(nativeQuery = true,
            value = "select distinct count(movie_id) from movie_collection m where m.collection_id=?1")
    long getCountOfMoviesById(long id);
}
