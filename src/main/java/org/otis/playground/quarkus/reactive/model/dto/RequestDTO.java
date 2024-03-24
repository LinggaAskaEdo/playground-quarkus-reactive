package org.otis.playground.quarkus.reactive.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.otis.playground.quarkus.reactive.model.ValidationGroups;

@Data
public class RequestDTO {
    @NotNull(groups = { ValidationGroups.CreateProduct.class, ValidationGroups.UpdateProduct.class })
    @Length(min = 8, max = 50, message = "Title must be between 8 - 50 in length")
    public String title;

    @NotNull(groups = { ValidationGroups.CreateProduct.class, ValidationGroups.UpdateProduct.class })
    @Length(min = 10, max = 100, message = "Description must be between 10 - 100 in length")
    public String description;

    @NotNull(groups = ValidationGroups.CreateShoppingCart.class )
    @Length(min = 5, max = 25, message = "Name must be between 5 - 25 in length")
    public String name;
}
