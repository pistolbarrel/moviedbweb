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
public class Collection {

    @Id
    @SequenceGenerator(name = "sequence_collection", sequenceName = "s_collection", allocationSize = 1)
    @GeneratedValue(generator = "sequence_collection")
    private Long collectionId;
    private String name;

    @Transient
    private Long count = 0L;

    public String getLinkString() {
        return MovieHtmlHelper.getCollectionLinkString(this);
    }
}
