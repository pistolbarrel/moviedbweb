package com.boes.moviedbweb.service;

import com.boes.moviedbweb.entity.Director;
import com.boes.moviedbweb.repo.DirectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
