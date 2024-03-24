package org.otis.playground.quarkus.reactive.model.entity.cart;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.jboss.logging.Logger;

import java.time.Duration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "shopping_cart")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@NamedQueries(value = {
        @NamedQuery(name = "ShoppingCart.findAll",
                query = "SELECT c FROM ShoppingCart c LEFT JOIN FETCH c.cartItems item LEFT JOIN FETCH item.product ORDER BY c.id ASC"),
        @NamedQuery(name = "ShoppingCart.getById",
                query = "SELECT c FROM ShoppingCart c LEFT JOIN FETCH c.cartItems item LEFT JOIN FETCH item.product WHERE c.id = ?1")})
public class ShoppingCart extends PanacheEntityBase {
    private static final Logger LOGGER = Logger.getLogger(ShoppingCart.class.getName());

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(name = "name", unique = true)
    public String name;

    @Column(name = "cart_total")
    public int cartTotal;

    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//    @Fetch(FetchMode.SUBSELECT)
//    @JoinColumn
    public final Set<ShoppingCartItem> cartItems = new HashSet<>();

    public void calculateCartTotal() {
        cartTotal = cartItems.stream().mapToInt(ShoppingCartItem::getQuantity).sum();
    }

    public static Uni<ShoppingCart> findByShoppingCartId(Long id) {
        return find("#ShoppingCart.getById", id).firstResult();
   }

    public static Uni<List<ShoppingCart>> getAllShoppingCarts() {
        return find("#ShoppingCart.findAll").list();
    }

    public static Uni<List<PanacheEntityBase>> findAllWithJoinFetch() {
        return find("SELECT c FROM ShoppingCart c LEFT JOIN FETCH c.cartItems").list();
    }

    public static Uni<ShoppingCart> createShoppingCart(ShoppingCart shoppingCart) {
        return Panache
                .withTransaction(shoppingCart::persist)
                .replaceWith(shoppingCart)
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .transform(IllegalStateException::new);
    }

    public static Uni<ShoppingCart> addProductToShoppingCart(Long shoppingCartId, Long productId) {
//        Uni<ShoppingCart> cart = findById(shoppingCartId);
//        Uni<Set<ShoppingCartItem>> cartItemsUni = cart.chain(shoppingCart -> Mutiny.fetch(shoppingCart.cartItems)).onFailure().transform(IllegalStateException::new);;
//        Uni<Product> productUni = Product.findByProductId(productId);
//        Uni<ShoppingCartItem> item = ShoppingCartItem.findByCartIdByProductId(shoppingCartId, productId);
//
//        Uni<Tuple4<ShoppingCart, Set<ShoppingCartItem>, ShoppingCartItem, Product>> responses = Uni.combine()
//                .all().unis(cart, cartItemsUni, item, productUni).asTuple();
//
//        return Panache
//                .withTransaction(() -> responses
//                        .onItem().ifNotNull()
//                        .transform(entity -> {
//                            LOGGER.info("AAA");
//                            if (null == entity.getItem1() || null == entity.getItem4() || null == entity.getItem2()) {
//                                LOGGER.info("BBB");
//                                return null;
//                            }
//
//                            if (null == entity.getItem3()) {
//                                LOGGER.info("CCC");
//                                LOGGER.infof("CART_ID: %s", entity.getItem1().id);
//                                LOGGER.infof("PRODUCT_ID: %s", entity.getItem4().id);
//                                ShoppingCartItem cartItem = ShoppingCartItem.builder()
//                                        .cart(entity.getItem1())
//                                        .product(entity.getItem4())
//                                        .quantity(1)
//                                        .build();
//
//                                entity.getItem2().add(cartItem);
//                            } else {
//                                LOGGER.info("DDD");
//                                entity.getItem3().quantity++;
//                            }
//
//                            LOGGER.info("EEE");
//                            entity.getItem1().calculateCartTotal();
//
//                            LOGGER.info("FFF");
//                            return entity.getItem1();
//
////                            return Response
////                                    .ok(ResponseDTO.builder()
////                                            .status(String.valueOf(OK.getStatusCode()))
////                                            .message(RESPONSE_MESSAGE_OK)
//////                                            .shoppingCart(entity.getItem1())
////                                            .build())
////                                    .build();
//                        }));

        return null;
    }

//    public static Uni<ShoppingCart> deleteProductFromShoppingCart(Long shoppingCartId, Long productId) {
//        Uni<ShoppingCart> cart = findById(shoppingCartId);
//        Uni<Set<ShoppingCartItem>> cartItemsUni = cart.chain(shoppingCart -> Mutiny.fetch(shoppingCart.cartItems)).onFailure().recoverWithNull();
//        Uni<Product> productUni = Product.findByProductId(productId);
//        Uni<ShoppingCartItem> item = ShoppingCartItem.findByCartIdByProductId(shoppingCartId, productId).toUni();
//
//        Uni<Tuple4<ShoppingCart, Set<ShoppingCartItem>, ShoppingCartItem, Product>> responses = Uni.combine().all().unis(cart, cartItemsUni, item, productUni).asTuple();
//
//        return Panache
//                .withTransaction(() -> responses
//                        .onItem().ifNotNull()
//                        .transform(entity -> {
//                            if (null == entity.getItem1() || null == entity.getItem4() || null == entity.getItem3()) {
//                                return null;
//                            }
//
//                            entity.getItem3().quantity--;
//
//                            if (entity.getItem3().quantity == 0) {
//                                entity.getItem2().remove(entity.getItem3());
//                            }
//
//                            entity.getItem1().calculateCartTotal();
//
//                            return entity.getItem1();
//                        }));
//    }
}
