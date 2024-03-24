package org.otis.playground.quarkus.reactive.controller.graphql.movie;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.graphql.*;
import org.otis.playground.quarkus.reactive.exception.AlreadyExistingException;
import org.otis.playground.quarkus.reactive.model.dto.ActorDTO;
import org.otis.playground.quarkus.reactive.model.dto.MovieDTO;
import org.otis.playground.quarkus.reactive.model.entity.movie.Actor;
import org.otis.playground.quarkus.reactive.model.entity.movie.ActorsMovies;
import org.otis.playground.quarkus.reactive.model.entity.movie.Movie;

import java.util.Collections;
import java.util.List;

@GraphQLApi
public class ActorController {
    @Query("allActors")
    @Description("Get all Actors")
    @WithSession
    public Uni<List<ActorDTO>> getAllActors() {
        return Actor.getAllActors()
                .onItem().transform(ActorDTO::from);
    }

    @Query
    @Description("Get an actor")
    @WithSession
    public Uni<ActorDTO> getActor(@Name("actorId") long id) {
        return Actor.findByActorId(id).onItem().transform(ActorDTO::from);
    }

    @WithSession
    public Uni<List<MovieDTO>> movies(@Source(name = "ActorResponse") ActorDTO actor) {
        return ActorsMovies.getMoviesByActorQuery(actor.id)
                .onItem()
                .transform(actorMovies -> actorMovies)
                .collect().asList()
                .onItem().transform(actorMovies -> Collections.singletonList(MovieDTO.from((Movie) actorMovies)));
    }

    @Mutation
    @Description("Add movie to actor")
    @WithSession
    public Uni<ActorDTO> addMovieToActor(@Name("movieId") long movieId, @Name("actorId") long actorId) {
        return Actor.addMovieToActor(movieId, actorId)
                .onItem().transform(ActorDTO::from)
                .onFailure().transform(throwable -> new AlreadyExistingException("movieId: " + movieId + " and actorId: " + actorId));
    }
}
