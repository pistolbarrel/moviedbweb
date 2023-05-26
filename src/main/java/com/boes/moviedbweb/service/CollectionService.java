package com.boes.moviedbweb.service;

import com.boes.moviedbweb.entity.Collection;
import com.boes.moviedbweb.repo.CollectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class CollectionService {

    private CollectionRepository collectionRepository;

    @Autowired
    public CollectionService(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    public Collection getOrCreateCollection(String name) {
        Collection collection = collectionRepository.findByName(name)
                .orElseGet(() -> collectionRepository.save(Collection.builder().name(name).build()));
        return collection;
    }

    public List<Collection> getAll() {
        return collectionRepository.findAllByOrderByName();
    }

    public Long getMovieCountById(long id) {
        Long ret = collectionRepository.getCountOfMoviesById(id);
        return ret;
    }

    public void deleteCollection(Collection collection) {
        collectionRepository.delete(collection);
    }

    public Collection getCollection(Long id) {
        return collectionRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Series does not exist."));
    }

    public void deleteCollection(Long id) {
        Collection collection = getCollection(id);
        collectionRepository.delete(collection);
    }

    public void renameCollection(long id, String name) {
        Collection collection = getCollection(id);
        collection.setName(name);
        collectionRepository.save(collection);
    }
}
