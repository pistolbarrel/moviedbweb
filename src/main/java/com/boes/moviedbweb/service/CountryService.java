package com.boes.moviedbweb.service;

import com.boes.moviedbweb.entity.Country;
import com.boes.moviedbweb.repo.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CountryService {

    private final CountryRepository countryRepository;

    @Autowired
    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Country getOrCreateCountry(String name) {
        return countryRepository.findByName(name)
                .orElseGet(() -> countryRepository.save(Country.builder().name(name).build()));
    }

    public List<Country> getAll() {
        return countryRepository.findAllByOrderByName();
    }

    public Long getMovieCountById(long id) {
        return countryRepository.getCountOfMoviesById(id);
    }


    public Country getCountry(Long id) {
        return countryRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Country does not exist."));
    }

    public void deleteCountry(Long id) {
        Country country = getCountry(id);
        countryRepository.delete(country);
    }

    public void renameCountry(long id, String name) {
        Country country = getCountry(id);
        country.setName(name);
        countryRepository.save(country);
    }
}
