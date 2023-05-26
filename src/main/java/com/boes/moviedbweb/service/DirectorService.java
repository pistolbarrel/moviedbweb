package com.boes.moviedbweb.service;

import com.boes.moviedbweb.entity.Director;
import com.boes.moviedbweb.repo.DirectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DirectorService {

    private final DirectorRepository directorRepository;

    @Autowired
    public DirectorService(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    public Director getOrCreateDirector(String name) {
        return directorRepository.findByName(name)
                .orElseGet(() -> directorRepository.save(Director.builder().name(name).build()));
    }

    public List<Director> getAll() {
        return directorRepository.findAllByOrderByName();
    }

    public Long getMovieCountById(long id) {
        return directorRepository.getCountOfMoviesById(id);
    }

    public Director getDirector(Long id) {
        return directorRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Series does not exist."));
    }

    public void deleteDirector(Long id) {
        Director director = getDirector(id);
        directorRepository.delete(director);
    }

    public void renameDirector(long id, String name) {
        Director director = getDirector(id);
        director.setName(name);
        directorRepository.save(director);
    }
}
