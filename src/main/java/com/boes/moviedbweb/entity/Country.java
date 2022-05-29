package com.boes.moviedbweb.entity;

import com.boes.moviedbweb.utils.MovieHtmlHelper;
import lombok.*;

import javax.persistence.*;

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

    @Transient
    private Long count = 0L;

    public String getLinkString() {
        return MovieHtmlHelper.getCountyLinkString(this);
    }
}
