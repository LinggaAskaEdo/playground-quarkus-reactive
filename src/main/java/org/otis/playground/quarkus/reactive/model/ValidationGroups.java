package org.otis.playground.quarkus.reactive.model;

import jakarta.validation.groups.Default;

public interface ValidationGroups {
    interface CreateProduct extends Default {}
    interface UpdateProduct extends Default{}
    interface CreateShoppingCart extends Default{}
}
