package org.otis.playground.quarkus.reactive.controller.rest.book;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import org.jboss.resteasy.reactive.RestPath;
import org.otis.playground.quarkus.reactive.model.entity.book.Book;
import org.otis.playground.quarkus.reactive.service.book.BookService;

@Path("/v1/book/books")
public class BookController {
    @Inject
    BookService bookService;

    @GET
    @Path("{id}")
    public Uni<Book> get(@RestPath("id") Long id) {
        return bookService.find(id);
    }

    @POST
    public Uni<Book> create(Book author) {
        return bookService.create(author);
    }
}
