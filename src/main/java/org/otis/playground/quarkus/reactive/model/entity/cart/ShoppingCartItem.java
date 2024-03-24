package org.otis.playground.quarkus.reactive.model.entity.cart;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Entity
@Cacheable
@Table(name = "shopping_cart_item", indexes = { @Index(name = "shopping_cart_item_cart_product_index", columnList = "cart_id, product_id") })
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode(callSuper = false)
public class ShoppingCartItem extends PanacheEntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @JsonbTransient
    @JsonIgnore
    public Long id;

    @Column(name = "total_price", precision = 21, scale = 2)
    public BigDecimal totalPrice;

    @Column(name = "quantity")
    public Integer quantity;

    @ManyToOne(fetch = FetchType.LAZY)
//    @NotNull
//    @JsonbTransient
    @JsonIgnore
    public ShoppingCart cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Product product;

    public static Uni<ShoppingCartItem> findByCartIdByProductId(Long cartId, Long productId) {
//        return ShoppingCartItem.<ShoppingCartItem> listAll().chain(shoppingCartItems -> shoppingCartItems.).;

        return find("cart.id = ?1 and product.id = ?2", cartId, productId).firstResult();

//        return ShoppingCartItem.<ShoppingCartItem> stream("cart.id = ?1 and product.id = ?2", cartId, productId);
    }
}
