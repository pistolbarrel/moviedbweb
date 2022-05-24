package com.boes.moviedbweb.repo;

import com.boes.moviedbweb.entity.Actor;
import com.boes.moviedbweb.entity.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CountryRepository extends JpaRepository<Country, Long> {
    Optional<Country> findByName(String name);
}
