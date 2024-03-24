package org.otis.playground.quarkus.reactive.repository.book;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.PersistenceException;
import org.hibernate.reactive.mutiny.Mutiny;
import org.otis.playground.quarkus.reactive.model.entity.book.Author;
import org.otis.playground.quarkus.reactive.model.dto.AuthorDTO;

import java.time.Duration;

@ApplicationScoped
public class AuthorRepository implements PanacheRepository<Author> {
    //If an author has >= 1 book, got error:
    //HR000057: java.util.concurrent.CompletionException: org.hibernate.LazyInitializationException:
    //HR000056: Collection cannot be initialized: org.otis.playground.quarkus.reactive.entity.book.Author.books
    // - Fetch the collection using 'Mutiny.fetch', 'Stage.fetch', or 'fetch join' in HQL
    public Uni<Author> findAndFetch(Long id) {
        return findById(id);
    }

    // I solved the issue with this function, but I want to reuse Author - for some reason
    public Uni<AuthorDTO> findFetchAndConvertVO(Long id) {
        return findById(id)
                .call(author -> Mutiny.fetch(author.getBooks()))
                .map(author -> AuthorDTO.builder()
                        .id(author.getId())
                        .authorName(author.getAuthorName())
//                        .books(author.getBooks())
                        .build()
                );
    }

    public Uni<Author> save(Author author) {
        return Panache.withTransaction(author::persist)
                .replaceWith(author)
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .transform(PersistenceException::new);
    }
}
