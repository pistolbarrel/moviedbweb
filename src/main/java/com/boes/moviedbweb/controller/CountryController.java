package com.boes.moviedbweb.controller;

import com.boes.moviedbweb.entity.Country;
import com.boes.moviedbweb.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.NoSuchElementException;

@RestController()
@Slf4j
public class CountryController {
    private final CountryService countryService;

    @Autowired
    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @Operation(summary = "Create a single Country")
    @PostMapping("/rest/countries")
    @ResponseStatus(HttpStatus.CREATED)
    public Country createCountry(@RequestParam String name) {
        return countryService.getOrCreateCountry(name);
    }

    @Operation(summary = "Single Country")
    @GetMapping("/rest/countries/{country_id}")
    public Country getCountryById(@PathVariable(value = "country_id") long id) {
        return countryService.getCountry(id);
    }

    @Operation(summary = "Delete an existing Country")
    @DeleteMapping("/rest/countries/{country_id}")
    public void deleteCountryById(@PathVariable(value = "country_id") long id) {
        countryService.deleteCountry(id);
    }

    @Operation(summary = "Modify an existing Country's name. Use the name field for new name.")
    @PutMapping("/rest/countries/{country_id}")
    public void renameCountryById(@PathVariable(value = "country_id") long id,
                                  @RequestParam(value = "name") String changeTo) {
        countryService.renameCountry(id, changeTo);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    public String return400(NoSuchElementException ex) {
        return ex.getMessage();
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public String return400(SQLIntegrityConstraintViolationException ex) {
        return ex.getMessage();
    }
}
