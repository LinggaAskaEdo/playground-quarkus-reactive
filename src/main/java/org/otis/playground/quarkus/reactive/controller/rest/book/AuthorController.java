package org.otis.playground.quarkus.reactive.controller.rest.book;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;
import org.otis.playground.quarkus.reactive.model.entity.book.Author;
import org.otis.playground.quarkus.reactive.model.dto.AuthorDTO;
import org.otis.playground.quarkus.reactive.service.book.AuthorService;

@Slf4j
@Path("/v1/book/author")
public class AuthorController {
    @Inject
    AuthorService authorService;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Author> get(@PathParam("id") Long id) {
        return authorService.find(id);
    }

    @GET
    @Path("/vo/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<AuthorDTO> getVO(@PathParam("id") Long id) {
        return authorService.findVO(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Author> create(Author author) {
        return authorService.create(author);
    }
}
