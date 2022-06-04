package com.boes.moviedbweb.service;

import com.boes.moviedbweb.entity.ViewDate;
import com.boes.moviedbweb.repo.ViewDateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ViewDateService {

    private ViewDateRepository viewDateRepository;

    @Autowired
    public ViewDateService(ViewDateRepository viewDateRepository) {
        this.viewDateRepository = viewDateRepository;
    }

    public ViewDate getOrCreateViewDate(LocalDate date) {
        ViewDate viewDate = viewDateRepository.findByLocalDate(date)
                .orElseGet(() -> viewDateRepository.save(ViewDate.builder().localDate(date).build()));
        return viewDate;
    }

    public void deleteViewDate(ViewDate viewDate) {
        viewDateRepository.delete(viewDate);
    }
}
