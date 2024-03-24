package org.otis.playground.quarkus.reactive.controller.rest.book.cart;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.otis.playground.quarkus.reactive.model.entity.cart.ShoppingCart;
import org.otis.playground.quarkus.reactive.model.dto.RequestDTO;
import org.otis.playground.quarkus.reactive.model.ValidationGroups;
import org.otis.playground.quarkus.reactive.service.cart.ShoppingCartService;

import java.util.Set;

@ApplicationScoped
@Path("/v1/cart/carts")
public class ShoppingCartResource {
    @Inject
    ShoppingCartService shoppingCartService;

    @Inject
    Validator validator;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getCarts() {
        return shoppingCartService.getAllShoppingCarts();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<Response> getSingleCart(@PathParam("id") Long id) {
        return shoppingCartService.findByShoppingCartId(id);
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> createShoppingCart(RequestDTO request) throws Exception {
        Set<ConstraintViolation<RequestDTO>> violations = validator.validate(request, ValidationGroups.CreateShoppingCart.class);

        if (!violations.isEmpty()) {
            throw new Exception(new ConstraintViolationException(violations));
        }

        return shoppingCartService.createShoppingCart(request);
    }

    @PUT
    @Path("{cartID}/{productID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<ShoppingCart> update(@PathParam("cartID") Long cartID, @PathParam("productID") Long productID) {
//        return shoppingCartService.addProductToShoppingCart(cartID, productID);
        return ShoppingCart.addProductToShoppingCart(cartID, productID);
    }
}
