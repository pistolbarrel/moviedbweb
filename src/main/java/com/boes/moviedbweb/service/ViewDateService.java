package com.boes.moviedbweb.service;

import com.boes.moviedbweb.entity.ViewDate;
import com.boes.moviedbweb.repo.ViewDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.NoSuchElementException;

@Service
public class ViewDateService {

    private ViewDateRepository viewDateRepository;

    @Autowired
    public ViewDateService(ViewDateRepository viewDateRepository) {
        this.viewDateRepository = viewDateRepository;
    }

    public ViewDate getOrCreateViewDate(LocalDate date) {
        return viewDateRepository.findByLocalDate(date).orElseGet(() ->
                viewDateRepository.save(ViewDate.builder().localDate(date).build()));
    }

    public ViewDate getViewDate(Long id) {
        return viewDateRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("ViewDate does not exist."));
    }

    public void deleteViewDate(Long id) {
        viewDateRepository.delete(getViewDate(id));
    }
}
