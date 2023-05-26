package com.boes.moviedbweb.controller;

import com.boes.moviedbweb.entity.Collection;
import com.boes.moviedbweb.service.CollectionService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.NoSuchElementException;

@RestController()
@Slf4j
public class CollectionController {
    private final CollectionService collectionService;

    @Autowired
    public CollectionController(CollectionService collectionService) {
        this.collectionService = collectionService;
    }

    @Operation(summary = "Create a single Collection")
    @PostMapping("/rest/collections")
    @ResponseStatus(HttpStatus.CREATED)
    public Collection createCollection(@RequestParam String name) {
        return collectionService.getOrCreateCollection(name);
    }

    @Operation(summary = "Single Collection")
    @GetMapping("/rest/collections/{collection_id}")
    public Collection getCollectionById(@PathVariable(value = "collection_id") long id) {
        return collectionService.getCollection(id);
    }

    @Operation(summary = "Delete an existing Collection")
    @DeleteMapping("/rest/collections/{collection_id}")
    public void deleteCollectionById(@PathVariable(value = "collection_id") long id) {
        collectionService.deleteCollection(id);
    }

    @Operation(summary = "Modify an existing Collection")
    @PutMapping("/rest/collections/{collection_id}")
    public void renameCollectionById(@PathVariable(value = "collection_id") long id,
                                     @RequestParam(value = "changeTo") Collection changeTo) {
        collectionService.renameCollection(id, changeTo.getName());
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
