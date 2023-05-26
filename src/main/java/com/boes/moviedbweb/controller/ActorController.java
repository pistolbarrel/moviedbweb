package com.boes.moviedbweb.controller;

import com.boes.moviedbweb.entity.Actor;
import com.boes.moviedbweb.service.ActorService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.NoSuchElementException;

@RestController()
@Slf4j
public class ActorController {
    private final ActorService actorService;

    @Autowired
    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @Operation(summary = "Single Actor")
    @GetMapping("/rest/actor/{actor_id}")
    public Actor getActorById(@PathVariable(value = "actor_id") long id) {
        return actorService.getActor(id);
    }

    @Operation(summary = "Delete an existing Actor")
    @DeleteMapping("/rest/actor/{actor_id}")
    public void deleteActorById(@PathVariable(value = "actor_id") long id) {
        actorService.deleteActor(id);
    }

    @Operation(summary = "Modify an existing Actor")
    @PutMapping("/rest/actor/{actor_id}")
    public void renameActorById(@PathVariable(value = "actor_id") long id,
                                @RequestParam(value = "changeTo") Actor changeTo) {
        actorService.renameActor(id, changeTo.getName());
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
