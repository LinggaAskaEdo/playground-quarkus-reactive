package org.otis.playground.quarkus.reactive.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.otis.playground.quarkus.reactive.model.entity.cart.Product;
import org.otis.playground.quarkus.reactive.model.entity.cart.ShoppingCart;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDTO {
    public String status;
    public String message;
    public String type;
    public String error;
    private List<ErrorMessageDTO> errors;
    public Product product;
    public List<Product> products;
    public ShoppingCart shoppingCart;
    public List<ShoppingCart> shoppingCarts;
}
