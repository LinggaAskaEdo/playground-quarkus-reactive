package org.otis.playground.quarkus.reactive.service.cart;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import org.otis.playground.quarkus.reactive.model.entity.cart.Product;
import org.otis.playground.quarkus.reactive.model.dto.RequestDTO;
import org.otis.playground.quarkus.reactive.model.dto.ResponseDTO;
import org.otis.playground.quarkus.reactive.repository.cart.ProductRepository;

import java.net.URI;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static jakarta.ws.rs.core.Response.Status.OK;
import static org.otis.playground.quarkus.reactive.preference.ConstantPreference.RESPONSE_MESSAGE_OK;

@ApplicationScoped
public class ProductService {
    @Inject
    ProductRepository productRepository;

    @WithSession
    public Uni<Response> getProducts() {
        return productRepository.getAllProducts()
                .onItem().transform(products -> Response
                        .ok(ResponseDTO.builder()
                                .status(String.valueOf(OK.getStatusCode()))
                                .message(RESPONSE_MESSAGE_OK)
                                .products(products)
                                .build())
                        .build());
    }

    @WithSession
    public Uni<Response> getSingleProduct(Long id) {
        return productRepository.findByProductId(id)
                .onItem().ifNotNull().transform(product -> Response
                        .ok(ResponseDTO.builder()
                                .status(String.valueOf(OK.getStatusCode()))
                                .message(RESPONSE_MESSAGE_OK)
                                .product(product)
                                .build())
                        .build())
                .onItem().ifNull().continueWith(Response.ok().status(NOT_FOUND)::build);
    }

    @WithSession
    public Uni<Response> addProduct(RequestDTO request) {
        Product product = Product.builder()
                .title(request.title)
                .description(request.description)
                .build();

        return productRepository.addProduct(product)
                .onItem().transform(entity -> URI.create("/v1/products/" + entity.id))
                .onItem().transform(uri -> Response.created(uri).build())
                .onFailure().transform(IllegalStateException::new);
    }

    @WithSession
    public Uni<Response> updateProduct(Long id, RequestDTO request) {
        Product product = Product.builder()
                .title(request.title)
                .description(request.description)
                .build();

        return productRepository.updateProduct(id, product)
                .onItem().ifNotNull().transform(entity -> Response
                        .ok(ResponseDTO.builder()
                                .status(String.valueOf(OK.getStatusCode()))
                                .message(RESPONSE_MESSAGE_OK)
                                .product(entity)
                                .build())
                        .build())
                .onFailure().transform(IllegalStateException::new);
    }

    @WithSession
    public Uni<Response> deleteProduct(Long id) {
        return productRepository.deleteProduct(id)
                .onItem().transform(entity -> !entity ? Response.serverError().status(NOT_FOUND).build() : Response.ok().status(OK).build());
    }
}
