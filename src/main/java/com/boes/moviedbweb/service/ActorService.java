package com.boes.moviedbweb.service;

import com.boes.moviedbweb.entity.Actor;
import com.boes.moviedbweb.repo.ActorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ActorService {

    private ActorRepository actorRepository;

    @Autowired
    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    public Actor getOrCreateActor(String name) {
        Actor actor = actorRepository.findByName(name)
                .orElseGet(() -> actorRepository.save(Actor.builder().name(name).build()));
        return actor;
    }

    public List<Actor> getAll() {
        return actorRepository.findAllByOrderByName();
    }

    public Long getMovieCountById(long id) {
        return actorRepository.getCountOfMoviesById(id);
    }

    public void deleteActor(Actor actor) {
        actorRepository.delete(actor);
    }

    public void deleteActorById(Long id) {
        Optional<Actor> actor = actorRepository.findById(id);
        if (actor.isPresent()) {
            actorRepository.delete(actor.get());
        } else {
            throw new NoSuchElementException("Actor does not exist.");
        }
    }
}
