package org.otis.playground.quarkus.reactive.repository.book;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import org.otis.playground.quarkus.reactive.model.entity.book.Publisher;

@ApplicationScoped
public class PublisherRepository implements PanacheRepository<Publisher> {
}
