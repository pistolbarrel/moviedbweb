package com.boes.moviedbweb.repo;

import com.boes.moviedbweb.entity.Actor;
import com.boes.moviedbweb.entity.ViewDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface ViewDateRepository extends JpaRepository<ViewDate, Long> {
    Optional<ViewDate> findByLocalDate(LocalDate date);
}
