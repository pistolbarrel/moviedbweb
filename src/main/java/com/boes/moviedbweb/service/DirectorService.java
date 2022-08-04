package com.boes.moviedbweb.service;

import com.boes.moviedbweb.entity.Director;
import com.boes.moviedbweb.repo.DirectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DirectorService {

    private DirectorRepository directorRepository;

    @Autowired
    public DirectorService(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    public Director getOrCreateDirector(String name) {
        Director director = directorRepository.findByName(name)
                .orElseGet(() -> directorRepository.save(Director.builder().name(name).build()));
        return director;
    }

    public List<Director> getAll() {
        return directorRepository.findAllByOrderByName();
    }

    public Long getMovieCountById(long id) {
        return directorRepository.getCountOfMoviesById(id);
    }

    public void deleteDirector(Director director) {
        directorRepository.delete(director);
    }

    public void deleteDirectorById(Long id) {
        Optional<Director> director = directorRepository.findById(id);
        if (director.isPresent()) {
            directorRepository.delete(director.get());
        } else {
            throw new NoSuchElementException("Director does not exist.");
        }
    }
}
