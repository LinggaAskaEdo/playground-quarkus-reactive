package org.otis.playground.quarkus.reactive.repository.cart;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.otis.playground.quarkus.reactive.model.entity.cart.Product;

import java.time.Duration;
import java.util.List;

@ApplicationScoped
public class ProductRepository implements PanacheRepository<Product> {
    public Uni<List<Product>> getAllProducts() {
        return listAll(Sort.by("createdAt"));
    }

    public Uni<Product> findByProductId(Long id) {
        return findById(id);
    }

    public Uni<Product> addProduct(Product product) {
        return Panache
                .withTransaction(product::persist)
                .replaceWith(product)
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .transform(IllegalStateException::new);
    }

    public Uni<Product> updateProduct(Long id, Product product) {
        return Panache
                .withTransaction(() -> findByProductId(id)
                        .onItem().ifNotNull()
                        .transform(entity -> {
                            entity.description = product.description;
                            entity.title = product.title;

                            return entity;
                        })
                        .onFailure()
                        .recoverWithNull());
    }

    public Uni<Boolean> deleteProduct(Long id) {
        return Panache.withTransaction(() -> deleteById(id));
    }
}
