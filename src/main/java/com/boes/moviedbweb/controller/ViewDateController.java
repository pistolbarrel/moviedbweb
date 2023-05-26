package com.boes.moviedbweb.controller;

import com.boes.moviedbweb.entity.ViewDate;
import com.boes.moviedbweb.service.ViewDateService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDate;
import java.util.NoSuchElementException;

@RestController()
@Slf4j
public class ViewDateController {
    private final ViewDateService viewDateService;

    @Autowired
    public ViewDateController(ViewDateService viewDateService) {
        this.viewDateService = viewDateService;
    }

    @Operation(summary = "Create a single ViewDate")
    @PostMapping("/rest/viewDates")
    @ResponseStatus(HttpStatus.CREATED)
    public ViewDate createViewDate(@RequestParam LocalDate date) {
        return viewDateService.getOrCreateViewDate(date);
    }

    @Operation(summary = "Single ViewDate")
    @GetMapping("/rest/viewDates/{viewDate_id}")
    public ViewDate getViewDateById(@PathVariable(value = "viewDate_id") long id) {
        return viewDateService.getViewDate(id);
    }

    @Operation(summary = "Delete an existing ViewDate")
    @DeleteMapping("/rest/viewDates/{viewDate_id}")
    public void deleteViewDateById(@PathVariable(value = "viewDate_id") long id) {
        viewDateService.deleteViewDate(id);
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
