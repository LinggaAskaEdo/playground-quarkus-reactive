package org.otis.playground.quarkus.reactive.repository.book;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.PersistenceException;
import org.hibernate.reactive.mutiny.Mutiny;
import org.otis.playground.quarkus.reactive.model.entity.book.Book;

import java.time.Duration;

@ApplicationScoped
public class BookRepository implements PanacheRepository<Book> {
    public Uni<Book> getBook(Long id) {
        return find("select b from Book b left join fetch b.author where b.id = ?1", id).firstResult();
    }

    public Uni<Book> findAndFetch(Long id) {
        return findById(id)
                .call(book -> Mutiny.fetch(book.author))
                .call(book -> Mutiny.fetch(book.publishers));
    }

    public Uni<Book> save(Book book) {
        return Panache.withTransaction(book::persist)
                .replaceWith(book)
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure().transform(PersistenceException::new);
    }
}
