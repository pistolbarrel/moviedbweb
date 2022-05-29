package com.boes.moviedbweb.service;

import com.boes.moviedbweb.entity.Country;
import com.boes.moviedbweb.repo.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {

    private CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Country getOrCreateCountry(String name) {
        Country country = countryRepository.findByName(name)
                .orElseGet(() -> countryRepository.save(Country.builder().name(name).build()));
        return country;
    }

    public List<Country> getAll() {
        return countryRepository.findAllByOrderByName();
    }

    public Long getMovieCountById(long id) {
        return countryRepository.getCountOfMoviesById(id);
    }
}
