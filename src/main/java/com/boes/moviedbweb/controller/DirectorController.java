package com.boes.moviedbweb.controller;

import com.boes.moviedbweb.entity.Director;
import com.boes.moviedbweb.service.DirectorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.NoSuchElementException;

@RestController()
@Slf4j
public class DirectorController {
    private final DirectorService directorService;

    @Autowired
    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @Operation(summary = "Create a single Director")
    @PostMapping("/rest/directors")
    @ResponseStatus(HttpStatus.CREATED)
    public Director createDirector(@RequestParam String name) {
        return directorService.getOrCreateDirector(name);
    }

    @Operation(summary = "Single Director")
    @GetMapping("/rest/directors/{director_id}")
    public Director getDirectorById(@PathVariable(value = "director_id") long id) {
        return directorService.getDirector(id);
    }

    @Operation(summary = "Delete an existing Director")
    @DeleteMapping("/rest/directors/{director_id}")
    public void deleteDirectorById(@PathVariable(value = "director_id") long id) {
        directorService.deleteDirector(id);
    }

    @Operation(summary = "Modify an existing Director's name. Use the name field for new name.")
    @PutMapping("/rest/directors/{director_id}")
    public void renameDirectorById(@PathVariable(value = "director_id") long id,
                                   @RequestParam(value = "name") String changeTo) {
        directorService.renameDirector(id, changeTo);
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
