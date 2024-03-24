package org.otis.playground.quarkus.reactive.service.cart;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.Response;
import org.otis.playground.quarkus.reactive.model.entity.cart.ShoppingCart;
import org.otis.playground.quarkus.reactive.model.dto.RequestDTO;
import org.otis.playground.quarkus.reactive.model.dto.ResponseDTO;

import java.net.URI;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;
import static jakarta.ws.rs.core.Response.Status.OK;
import static org.otis.playground.quarkus.reactive.preference.ConstantPreference.RESPONSE_MESSAGE_OK;

@ApplicationScoped
public class ShoppingCartService {
    @WithSession
    public Uni<Response> getAllShoppingCarts() {
        return ShoppingCart.getAllShoppingCarts()
                .onItem().transform(shoppingCarts -> Response
                        .ok(ResponseDTO.builder()
                                .status(String.valueOf(OK.getStatusCode()))
                                .message(RESPONSE_MESSAGE_OK)
                                .shoppingCarts(shoppingCarts)
                                .build())
                        .build());
    }

    @WithSession
    public Uni<Response> findByShoppingCartId(Long id) {
        return ShoppingCart.findByShoppingCartId(id)
                .onItem().ifNotNull().transform(cart -> Response
                        .ok(ResponseDTO.builder()
                                .status(String.valueOf(OK.getStatusCode()))
                                .message(RESPONSE_MESSAGE_OK)
                                .shoppingCart(cart)
                                .build())
                        .build())
                .onItem().ifNull().continueWith(Response.ok().status(NOT_FOUND)::build);
    }

    @WithSession
    public Uni<Response> createShoppingCart(RequestDTO request) {
        ShoppingCart shoppingCart = ShoppingCart.builder()
                .name(request.name)
                .build();

        return ShoppingCart.createShoppingCart(shoppingCart)
                .onItem().transform(entity -> URI.create("/v1/carts/" + entity.id))
                .onItem().transform(uri -> Response.created(uri).build())
                .onFailure().transform(IllegalStateException::new);
    }

//    @WithSession
//    public Uni<Response> addProductToShoppingCart(Long cartID, Long productID) {
//        return ShoppingCart.addProductToShoppingCart(cartID, productID)
//                .onItem().transform(shoppingCart -> Response
//                        .ok(ResponseDTO.builder()
//                                .status(String.valueOf(OK.getStatusCode()))
//                                .message(RESPONSE_MESSAGE_OK)
//                                .shoppingCart(shoppingCart)
//                                .build())
//                        .build());
//    }
}
