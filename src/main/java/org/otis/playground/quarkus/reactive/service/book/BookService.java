package org.otis.playground.quarkus.reactive.service.book;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.otis.playground.quarkus.reactive.model.entity.book.Book;
import org.otis.playground.quarkus.reactive.repository.book.BookRepository;

@ApplicationScoped
public class BookService {
    @Inject
    BookRepository bookRepository;

    @WithSession
    @WithTransaction
    public Uni<Book> find(Long id) {
        return bookRepository.getBook(id);
    }

    public Uni<Book> findVO(Long id) {
        return bookRepository.findAndFetch(id);
    }

    public Uni<Book> findByName(String name) {
        return bookRepository.find("book_name", name).firstResult();
    }

    public Uni<Book> create(Book book) {
        return findByName(book.bookName)
                .invoke(result -> {
                    if (result != null) {
                        throw new RuntimeException("Duplicate"); //Duplicated
                    }
                })
                .replaceWith(book)
                .call(bookRepository::save);
    }
}
