package com.boes.moviedbweb.repo;

import com.boes.moviedbweb.entity.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ActorRepository extends JpaRepository<Actor, Long> {
    Optional<Actor> findByName(String name);

    List<Actor> findAllByOrderByName();

    @Query(nativeQuery = true,
            value = "select distinct count(movie_id) from movie_actor m where m.actor_id=?1")
    long getCountOfMoviesById(long id);
}
