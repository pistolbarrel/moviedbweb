package com.boes.moviedbweb.entity;

import com.boes.moviedbweb.utils.MovieHtmlHelper;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Country {

    @Id
    @SequenceGenerator(name = "sequence_country", sequenceName = "s_country", allocationSize = 1)
    @GeneratedValue(generator = "sequence_country")
    private Long countryId;
    private String name;

    public String getLinkString() {
        return MovieHtmlHelper.getCountyLinkString(this);
    }
}
