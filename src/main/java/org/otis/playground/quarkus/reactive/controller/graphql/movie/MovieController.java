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

import java.util.List;

@GraphQLApi
public class MovieController {
    @Query("allMovies")
    @Description("Get all Movies")
    @WithSession
    public Uni<List<MovieDTO>> getAllMovies() {
        return Movie.getAllMovies().onItem().transform(MovieDTO::from);
    }

    @Query
    @Description("Get a movie")
    @WithSession
    public Uni<MovieDTO> getMovie(@Name("movieId") long id) {
        return Movie.findByMovieId(id).onItem().transform(MovieDTO::from);
    }

    @WithSession
    public Uni<List<ActorDTO>> actors(@Source(name = "MovieResponse") MovieDTO movie) {
//        return ActorsMovies.getActorsByMovieQuery(movie.id)
//                .onItem()
//                .transform(actorMovies -> ActorDTO.from((Actor) actorMovies))
//                .collect().asList();

        return ActorsMovies.getActorsByMovieQuery(movie.id)
                .onItem()
                .transform(actorsMovies ->
                        ActorDTO.from(actorsMovies.get(0).actor))
                .collect().asList();
    }

    @Mutation
    @Description("Create a movie")
    @WithSession
    public Uni<MovieDTO> createMovie(Movie movie) {
        return Movie.addMovie(movie).onItem().transform(MovieDTO::from);
    }

    @Mutation
    @Description("Update a movie")
    @WithSession
    public Uni<MovieDTO> updateMovie(@Name("movieId") long id, Movie movie) {
        return Movie.updateMovie(id, movie).onItem().transform(MovieDTO::from);
    }

    @Mutation
    @Description("Delete a movie")
    @WithSession
    public Uni<Boolean> deleteMovie(@Name("movieId") long id) {
        return Movie.deleteMovie(id);
    }

    @Mutation
    @Description("Add actor to movie")
    @WithSession
    public Uni<MovieDTO> addActorToMovie(@Name("movieId") long movieId, @Name("actorId") long actorId) {
        return Movie.addActorToMovie(movieId, actorId)
                .onItem()
                .transform(MovieDTO::from)
                .onFailure().transform(throwable -> new AlreadyExistingException("movieId: " + movieId + " and actorId: " + actorId));
    }
}
