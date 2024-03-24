package org.otis.playground.quarkus.reactive.controller.rest.cart;

import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.otis.playground.quarkus.reactive.model.dto.RequestDTO;
import org.otis.playground.quarkus.reactive.model.ValidationGroups;
import org.otis.playground.quarkus.reactive.service.cart.ProductService;

import java.util.Set;

@Path("/v1/cart/products")
public class ProductController {
    @Inject
    ProductService productService;

    @Inject
    Validator validator;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getProducts() {
        return productService.getProducts();
    }

    @GET
    @Path("{id}")
    public Uni<Response> getSingleProduct(@PathParam("id") Long id) {
        return productService.getSingleProduct(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> add(RequestDTO request) throws Exception {
        Set<ConstraintViolation<RequestDTO>> violations = validator.validate(request, ValidationGroups.CreateProduct.class);

        if (!violations.isEmpty()) {
            throw new Exception(new ConstraintViolationException(violations));
        }

        return productService.addProduct(request);
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> update(@PathParam("id") Long id, RequestDTO request) throws Exception {
        Set<ConstraintViolation<RequestDTO>> violations = validator.validate(request, ValidationGroups.UpdateProduct.class);

        if (!violations.isEmpty()) {
            throw new Exception(new ConstraintViolationException(violations));
        }

        return productService.updateProduct(id, request);
    }

    @DELETE
    @Path("{id}")
    public Uni<Response> delete(@PathParam("id") Long id) {
        return productService.deleteProduct(id);
    }
}
