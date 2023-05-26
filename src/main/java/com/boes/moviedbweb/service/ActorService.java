package com.boes.moviedbweb.service;

import com.boes.moviedbweb.entity.Actor;
import com.boes.moviedbweb.repo.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ActorService {

    private final ActorRepository actorRepository;

    @Autowired
    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public Actor getOrCreateActor(String name) {
        return actorRepository.findByName(name).orElseGet(() ->
                actorRepository.save(Actor.builder().name(name).build()));
    }

    public List<Actor> getAll() {
        return actorRepository.findAllByOrderByName();
    }

    public Long getMovieCountById(long id) {
        return actorRepository.getCountOfMoviesById(id);
    }

    public Actor getActor(Long id) {
        return actorRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Actor does not exist."));
    }

    public void deleteActor(Long id) {
        actorRepository.delete(getActor(id));
    }

    public void renameActor(long id, String name) {
        Actor actor = getActor(id);
        actor.setName(name);
        actorRepository.save(actor);
    }
}
