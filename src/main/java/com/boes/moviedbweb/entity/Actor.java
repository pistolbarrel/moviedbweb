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
public class Actor {

    @Id
    @SequenceGenerator(name = "sequence_actor", sequenceName = "s_actor", allocationSize = 1)
    @GeneratedValue(generator = "sequence_actor")
    private Long actorId;
    private String name;

    @Transient
    private Long count;

    public String getLinkString() {
        return MovieHtmlHelper.getActorLinkString(this);
    }
}
