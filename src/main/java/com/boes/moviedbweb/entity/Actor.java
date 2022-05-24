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
public class Actor {

    @Id
    @SequenceGenerator(name = "sequence_actor", sequenceName = "s_actor", allocationSize = 1)
    @GeneratedValue(generator = "sequence_actor")
    private Long actorId;
    private String name;

    public String getLinkString() {
        return MovieHtmlHelper.getActorLinkString(this);
    }
}
