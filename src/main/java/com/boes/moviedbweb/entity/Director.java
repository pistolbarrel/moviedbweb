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
public class Director {

    @Id
    @SequenceGenerator(name = "sequence_director", sequenceName = "s_director", allocationSize = 1)
    @GeneratedValue(generator = "sequence_director")
    private Long directorId;
    private String name;

    @Transient
    private Long count;

    public String getLinkString() {
        return MovieHtmlHelper.getDirectorLinkString(this);
    }
}
