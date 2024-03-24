package org.otis.playground.quarkus.reactive.model.entity.movie;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Multi;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "actors_movies",
        indexes = {
            @Index(name = "movie_id_index", columnList = "movie_id"),
            @Index(name = "actor_id_index", columnList = "actor_id"),
        })

@NamedQueries(value = {
        @NamedQuery(name = "ActorsMovies.getByMovieId", query = "SELECT c FROM ActorsMovies c JOIN FETCH c.actor where c.movie.id = ?1"),
        @NamedQuery(name = "ActorsMovies.getByActorId", query = "SELECT c FROM ActorsMovies c JOIN FETCH c.movie where c.actor.id = ?1")
})
@Getter
public class ActorsMovies extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Movie movie;

    @OneToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Actor actor;

    public static Multi<List<ActorsMovies>> getActorsByMovieQuery(Long movieId) {
        return find("#ActorsMovies.getByMovieId", movieId).project(ActorsMovies.class).list().toMulti();
    }

    public static Multi<List<ActorsMovies>> getMoviesByActorQuery(Long actorId) {
        return find("#ActorsMovies.getByActorId", actorId).project(ActorsMovies.class).list().toMulti();
    }

    public String toString() {
        return this.getClass().getSimpleName() + "<" + this.id + ">";
    }
}
