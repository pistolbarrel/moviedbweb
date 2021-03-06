package com.boes.moviedbweb.repo;

import com.boes.moviedbweb.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Optional<Movie> findByTitleAndYear(String title, String year);
    List<Movie> findByYearOrderByTitle(String year);
    public List<Movie> findAllByOrderByTitleAsc();

    boolean existsByTitleAndYear(String title, String year);

    @Query(nativeQuery = true,
            value = "select * from movie m where m.movie_id in (select movie_id from movie_collection where collection_id=?1) order by m.title")
    List<Movie> findByCollectionId(long id);

    @Query(nativeQuery = true,
            value = "select * from movie m where m.movie_id in (select movie_id from movie_actor where actor_id=?1) order by m.year")
    List<Movie> findByActorId(long id);

    @Query(nativeQuery = true,
            value = "select * from movie m where m.movie_id in (select movie_id from movie_director where director_id=?1) order by m.year")
    List<Movie> findByDirectorId(long id);

    @Query(nativeQuery = true,
            value = "select * from movie m where m.movie_id in (select movie_id from movie_country where country_id=?1) order by m.title")
    List<Movie> findByCountryIdOOrderByTitle(long id);

    @Query(nativeQuery = true,
            value = "select m.* from movie m where m.movie_id in \n" +
                    "(select mvd.movie_id from movie_viewdate mvd where mvd.date_id in \n" +
                    "(select vd.date_id from view_date vd " +
                    "where local_date BETWEEN CURDATE() - INTERVAL 30 DAY AND CURDATE()))")
    List<Movie> findViewedInLast30Days();

    List<Movie> searchAllByDateViewedIsNull();
}
