package com.boes.moviedbweb.entity;

import com.boes.moviedbweb.utils.MovieHtmlHelper;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ViewDate {

    @Id
    @SequenceGenerator(name = "sequence_viewDate", sequenceName = "s_viewDate", allocationSize = 1)
    @GeneratedValue(generator = "sequence_viewDate")
    private Long dateId;
    private LocalDate localDate;
}
