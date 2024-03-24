package org.otis.playground.quarkus.reactive.service.book;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.otis.playground.quarkus.reactive.model.entity.book.Author;
import org.otis.playground.quarkus.reactive.model.dto.AuthorDTO;
import org.otis.playground.quarkus.reactive.repository.book.AuthorRepository;

@ApplicationScoped
public class AuthorService {
    @Inject
    AuthorRepository authorRepository;

    @WithSession
    public Uni<Author> find(Long id) {
        return authorRepository.findAndFetch(id);
    }

    @WithSession
    public Uni<AuthorDTO> findVO(Long id) {
        return authorRepository.findFetchAndConvertVO(id);
    }

    public Uni<Author> findByName(String name) {
        return authorRepository.find("author_name", name).firstResult();
    }

    public Uni<Author> create(Author author) {
        return findByName(author.authorName)
                .invoke(result -> {
                    if (result != null) {
                        throw new RuntimeException("Duplicate"); //Duplicated
                    }
                })
                .replaceWith(author)
                .call(authorRepository::save);
    }
}
